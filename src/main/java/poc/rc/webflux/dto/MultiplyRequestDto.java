package poc.rc.webflux.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class MultiplyRequestDto {
  private int first;
  private int second;
}
