package bg.duosoft.sirmatask.controllers;

import bg.duosoft.sirmatask.dtos.EmployeePairDTO;
import bg.duosoft.sirmatask.services.EmployeeService;
import bg.duosoft.sirmatask.util.ReadCsvUtil;
import com.opencsv.exceptions.CsvException;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    private final Validator validator;

    @PostMapping("/pair")
    public ResponseEntity<?> getEmployeePair(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty.");
        } else if (!Objects.equals(file.getContentType(), "text/csv")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is not csv.");
        }
        try {
            EmployeePairDTO result = employeeService.findPairEmployees(ReadCsvUtil.readCsvFile(file, validator));
            return result == null ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("Could not find pair of employees.") : ResponseEntity.ok(result);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing file, date format is not supported.");
        } catch (ValidationException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (CsvException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing file, file is not in valid format.");
        }
    }

}
