package backend.spectrum.dguonoff.domain.reservation.dto;

import backend.spectrum.dguonoff.dao.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class HostUserInfo {
    String id;
    Role role;
    String sid;
    String name;
    String major;
    String email;
}
