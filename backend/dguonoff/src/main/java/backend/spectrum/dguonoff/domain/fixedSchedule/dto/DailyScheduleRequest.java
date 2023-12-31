package backend.spectrum.dguonoff.domain.fixedSchedule.dto;

import backend.spectrum.dguonoff.domain.fixedSchedule.dto.common.FacilityKeyDTO;
import backend.spectrum.dguonoff.domain.fixedSchedule.dto.common.PeriodDTO;
import java.time.DayOfWeek;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DailyScheduleRequest {
    private final FacilityKeyDTO facility;
    private final DayOfWeek day;
    private final PeriodDTO<LocalDate> effectiveDate;
}
