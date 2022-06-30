package poc.rc.webflux.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import poc.rc.webflux.dto.MultiplyRequestDto;
import poc.rc.webflux.dto.Response;
import poc.rc.webflux.exception.InputValidationException;
import poc.rc.webflux.service.ReactiveMathService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RequestHandler {
  @Autowired
  private ReactiveMathService mathService;

  public Mono<ServerResponse> squareHandler(ServerRequest serverRequest) {
    Integer input = Integer.valueOf(serverRequest.pathVariable("input"));
    Mono<Response> res = mathService.findSquare(input);
    return ServerResponse.ok().body(res, Response.class);
  }

  public Mono<ServerResponse> tableHandler(ServerRequest serverRequest) {
    Integer input = Integer.valueOf(serverRequest.pathVariable("input"));
    Flux<Response> res = mathService.multiplicationTable(input);
    return ServerResponse.ok().body(res, Response.class);
  }

  public Mono<ServerResponse> tableStreamHandler(ServerRequest serverRequest) {
    Integer input = Integer.valueOf(serverRequest.pathVariable("input"));
    Flux<Response> res = mathService.multiplicationTable(input);
    return ServerResponse.ok()
        .contentType(MediaType.TEXT_EVENT_STREAM)
        .body(res, Response.class);
  }

  public Mono<ServerResponse> multiplyHandler(ServerRequest serverRequest) {
    Mono<MultiplyRequestDto> requestDtoMono = serverRequest
        .bodyToMono(MultiplyRequestDto.class);
    Mono<Response> res = mathService.multiply(requestDtoMono);
    return ServerResponse.ok().body(res, Response.class);
  }

  public Mono<ServerResponse> squareHandlerWithValidation(ServerRequest serverRequest) {
    Integer input = Integer.valueOf(serverRequest.pathVariable("input"));
    if(input < 10 || input > 20) {
      /**
       * InputFailedValidation res = new InputFailedValidation();
       *       return ServerResponse.badRequest().bodyValue(res);
       */
      return  Mono.error(new InputValidationException(input));
    }
    Mono<Response> res = mathService.findSquare(input);
    return ServerResponse.ok().body(res, Response.class);
  }
}
