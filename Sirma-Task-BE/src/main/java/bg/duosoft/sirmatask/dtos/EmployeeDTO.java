package bg.duosoft.sirmatask.dtos;

import bg.duosoft.sirmatask.util.CustomDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Min;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

import java.util.Date;

@Data
public class EmployeeDTO {

    @NotNull(message = "IDs cannot be NULL")
    @Min(value = 1, message = "IDs cannot be negative.")
    private Long empID;

    @NotNull(message = "IDs cannot be NULL")
    @Min(value = 1, message = "IDs cannot be negative.")
    private Long projectID;

    @NotNull
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private Date dateFrom;

    @JsonDeserialize(using = CustomDateDeserializer.class)
    private Date dateTo;
}
