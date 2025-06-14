package dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ErrorValidationResponseDTO {
    private int status;
    private String message;
    private Map<String, String> errors;
}