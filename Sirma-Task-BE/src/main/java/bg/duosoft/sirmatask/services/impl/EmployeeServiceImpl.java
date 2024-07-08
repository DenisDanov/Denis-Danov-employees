package bg.duosoft.sirmatask.services.impl;

import bg.duosoft.sirmatask.dtos.EmployeeDTO;
import bg.duosoft.sirmatask.dtos.EmployeePairDTO;
import bg.duosoft.sirmatask.dtos.PairDataDTO;
import bg.duosoft.sirmatask.services.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static bg.duosoft.sirmatask.util.CalculateOverlapDaysUtil.calculateOverlapDays;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Override
    public EmployeePairDTO findPairEmployees(List<EmployeeDTO> employees) {
        Map<Long, List<EmployeeDTO>> projectEmployeesMap = employees.stream()
                .collect(Collectors.groupingBy(EmployeeDTO::getProjectID));

        Map<String, PairDataDTO> pairDataMap = new LinkedHashMap<>();

        for (List<EmployeeDTO> projectEmployees : projectEmployeesMap.values()) {
            for (int i = 0; i < projectEmployees.size(); i++) {
                for (int j = i + 1; j < projectEmployees.size(); j++) {
                    EmployeeDTO e1 = projectEmployees.get(i);
                    EmployeeDTO e2 = projectEmployees.get(j);
                    long overlapDays = calculateOverlapDays(e1, e2);
                    if (overlapDays > 0) {
                        String pairKey = e1.getEmpID() < e2.getEmpID()
                                ? e1.getEmpID() + "," + e2.getEmpID()
                                : e2.getEmpID() + "," + e1.getEmpID();
                        PairDataDTO pairData = pairDataMap.getOrDefault(pairKey, new PairDataDTO(0L, new HashMap<>()));
                        pairData.setTotalOverlap(pairData.getTotalOverlap() + overlapDays);
                        pairData.getProjectsData().put(e1.getProjectID(),overlapDays);
                        pairDataMap.put(pairKey, pairData);
                    }
                }
            }
        }

        String[] empIds = new String[2];
        long maxDuration = 0;
        Map<Long,Long> projectsData = new HashMap<>();
        for (Map.Entry<String, PairDataDTO> entry : pairDataMap.entrySet()) {
            if (entry.getValue().getTotalOverlap() > maxDuration) {
                maxDuration = entry.getValue().getTotalOverlap();
                empIds[0] = entry.getKey().split(",")[0];
                empIds[1] = entry.getKey().split(",")[1];
                projectsData = entry.getValue().getProjectsData();
            }
        }
        if (empIds[0] != null && empIds[1] != null) {
            Long empId1 = Long.parseLong(empIds[0]);
            Long empId2 = Long.parseLong(empIds[1]);
            EmployeeDTO employee1 = employees.stream().filter(e -> e.getEmpID().equals(empId1)).findFirst().orElse(null);
            EmployeeDTO employee2 = employees.stream().filter(e -> e.getEmpID().equals(empId2)).findFirst().orElse(null);

            return new EmployeePairDTO(employee1, employee2, new PairDataDTO(maxDuration, projectsData));
        }
        return null;
    }
}
