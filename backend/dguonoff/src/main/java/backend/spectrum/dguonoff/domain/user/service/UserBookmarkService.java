package backend.spectrum.dguonoff.domain.user.service;

import backend.spectrum.dguonoff.dao.Bookmark;
import backend.spectrum.dguonoff.dao.Facility;
import backend.spectrum.dguonoff.dao.User;
import backend.spectrum.dguonoff.domain.fixedSchedule.dto.common.FacilityKeyDTO;
import backend.spectrum.dguonoff.domain.facility.repository.FacilityRepository;
import backend.spectrum.dguonoff.domain.user.dto.UserBookmarkDTO;
import backend.spectrum.dguonoff.domain.user.exception.UserNotFoundException;
import backend.spectrum.dguonoff.domain.user.repository.UserRepository;
import backend.spectrum.dguonoff.global.error.Exception.BusinessException;
import backend.spectrum.dguonoff.global.statusCode.ErrorCode;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserBookmarkService {
    private final UserRepository userRepository;
    private final FacilityRepository facilityRepository;

    public List<UserBookmarkDTO> getUserBookmarks(String userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.NOT_EXIST_USER));
        List<Facility> bookmarkedFacilities = user.getBookmarks().stream().map(Bookmark::getFacility)
                .collect(Collectors.toList());
        return bookmarkedFacilities.stream()
                .map(facility -> UserBookmarkDTO.builder()
                                .facilityName(facility.getName())
                                .buildingName(facility.getBuilding().getName())
                                .facilityState(facility.getState())
                                .facilityCode(facility.getCode())
                                .build()
                ).collect(Collectors.toList());
    }

    public void enrollBookmark(String userId, FacilityKeyDTO request) {
        Bookmark newBookmark = fromUserIdAndFacilityId(userId, request);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.NOT_EXIST_USER));
        Optional<Bookmark> dupBookmark = user.getBookmarks().stream()
                .filter(bookmark -> bookmark.getFacility().getBuilding().getName().equals(request.getBuildingName())
                        && bookmark.getFacility().getCode().equals(request.getCode())).findAny();
        if (dupBookmark.isPresent())
            throw new BusinessException(ErrorCode.DUP_BOOKMARK);
        user.addBookmark(newBookmark);
        userRepository.save(user);
    }

    public void removeBookmark(String userId, FacilityKeyDTO request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.NOT_EXIST_USER));
        List<Bookmark> bookmarks = user.getBookmarks();
        for (Iterator<Bookmark> itr = bookmarks.iterator(); itr.hasNext();) {
            Bookmark bookmark = itr.next();
            if (bookmark.getFacility().getCode().equals(request.getCode()) &&
                    bookmark.getFacility().getBuilding().getName().equals(request.getBuildingName())) {
                itr.remove();
                userRepository.save(user);
                return ;
            }
        }
        throw new BusinessException(ErrorCode.NO_BOOKMARK);
    }

    private Bookmark fromUserIdAndFacilityId(String userId, FacilityKeyDTO facilityKey) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.NOT_EXIST_USER));
        Facility facility = facilityRepository
                .findByBuilding_NameAndCode(facilityKey.getBuildingName(), facilityKey.getCode())
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_EXIST_FACILITY));
        return new Bookmark(user, facility);
    }
}
