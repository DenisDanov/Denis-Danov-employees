package bg.duosoft.sirmatask.services;

import bg.duosoft.sirmatask.dtos.EmployeeDTO;
import bg.duosoft.sirmatask.dtos.EmployeePairDTO;

import java.util.List;

public interface EmployeeService {
    EmployeePairDTO findPairEmployees(List<EmployeeDTO> employees);
}
