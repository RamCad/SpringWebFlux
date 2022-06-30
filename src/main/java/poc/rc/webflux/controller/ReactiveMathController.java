package poc.rc.webflux.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import poc.rc.webflux.dto.MultiplyRequestDto;
import poc.rc.webflux.dto.Response;
import poc.rc.webflux.service.ReactiveMathService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/reactive-math")
public class ReactiveMathController {

  @Autowired
  private ReactiveMathService reactiveMathService;

  @GetMapping("/square/{input}")
  public Mono<Response> findSquare(@PathVariable int input) {
    return reactiveMathService.findSquare(input);
  }

  @GetMapping("/table/{input}")
  public Flux<Response> multiplication(@PathVariable int input) {
    return reactiveMathService.multiplicationTable(input);
  }

  @GetMapping(value = "/table/{input}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<Response> multiplicationStream(@PathVariable int input) {
    return reactiveMathService.multiplicationTable(input);
  }

  @PostMapping("/multiply")
  public Mono<Response> multiply(@RequestBody Mono<MultiplyRequestDto> request,
      @RequestHeader Map<String, String> headers) {
    System.out.println(headers);
    return reactiveMathService.multiply(request);
  }
}
