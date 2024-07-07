package bg.duosoft.sirmatask.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeePairDTO {
    private EmployeeDTO employee1;
    private EmployeeDTO employee2;
    private PairDataDTO pairDataDTO;
}
