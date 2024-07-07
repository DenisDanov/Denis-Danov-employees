package bg.duosoft.sirmatask.util;

import bg.duosoft.sirmatask.dtos.EmployeeDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ReadCsvUtil {

    public static List<EmployeeDTO> readCsvFile(MultipartFile file, Validator validator) throws IOException, CsvException {
        List<EmployeeDTO> employees = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            List<String[]> lines = reader.readAll();
            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addDeserializer(Date.class, new CustomDateDeserializer());
            mapper.registerModule(module);

            for (String[] line : lines.subList(1, lines.size())) {
                EmployeeDTO employee = new EmployeeDTO();
                if (line[0].equalsIgnoreCase("null") || line[1].equalsIgnoreCase("null") || !StringUtils.hasText(line[0]) || !StringUtils.hasText(line[1])) {
                    throw new ValidationException("IDs cannot be NULL.");
                } else if (line[2].equalsIgnoreCase("null") || !StringUtils.hasText(line[2])) {
                    throw new ValidationException("Date from cannot be NULL or empty.");
                }
                employee.setEmpID(Long.parseLong(line[0]));
                employee.setProjectID(Long.parseLong(line[1]));
                employee.setDateFrom(mapper.readValue("\"" + line[2] + "\"", Date.class));
                employee.setDateTo(mapper.readValue("\"" + line[3] + "\"", Date.class));

                Set<ConstraintViolation<EmployeeDTO>> violations = validator.validate(employee);

                if (violations.isEmpty()) {
                    employees.add(employee);
                } else {
                    throw new ValidationException(violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining()));
                }

            }
        }

        return employees;
    }

}
