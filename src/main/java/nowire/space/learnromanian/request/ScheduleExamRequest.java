package nowire.space.learnromanian.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleExamRequest implements Serializable {
    @Range(min = 2024, max = 2100)
    private Integer year;
    @Range(min = 1, max = 12)
    private Integer month;
    @Range(min = 1, max = 31)
    private Integer dayOfMonth;
    @Range(min = 0, max = 24)
    private Integer hour;
    @Range(min = 1, max = 60)
    private Integer minute;

}
