package poc.rc.webflux.dto;

import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class Response {

  private Date date = new Date();
  private int output;

  public Response(int output) {
    this.output = output;
  }
}
