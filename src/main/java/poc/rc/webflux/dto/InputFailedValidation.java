package poc.rc.webflux.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class InputFailedValidation {

  private int errorCode;
  private int input;
  private String message;
}
