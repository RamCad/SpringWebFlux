package poc.rc.webflux.exceptionhandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import poc.rc.webflux.dto.InputFailedValidation;
import poc.rc.webflux.exception.InputValidationException;

@ControllerAdvice
public class InputValidationHandler {

  @ExceptionHandler(InputValidationException.class)
  public ResponseEntity<InputFailedValidation> handleException(InputValidationException ex) {
    InputFailedValidation inputFailedValidation = new InputFailedValidation();
    inputFailedValidation.setErrorCode(ex.getErrorCode());
    inputFailedValidation.setInput(ex.getInput());
    inputFailedValidation.setMessage(ex.getMessage());
    return ResponseEntity.badRequest().body(inputFailedValidation);
  }
}
