package bg.duosoft.sirmatask.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class PairDataDTO {
    long totalOverlap;
    Map<Long,Long> projectsData;
}
