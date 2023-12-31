package backend.spectrum.dguonoff.domain.fixedSchedule.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PeriodDTO<T extends Comparable> {
    private T start;
    private T end;
}
