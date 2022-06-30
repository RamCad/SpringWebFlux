package poc.rc.webflux.config;

import java.util.function.BiFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import poc.rc.webflux.dto.InputFailedValidation;
import poc.rc.webflux.exception.InputValidationException;
import reactor.core.publisher.Mono;

@Configuration
public class RouterConfig {

  @Autowired
  private RequestHandler requestHandler;

  /**
   *  remove router from below serverResponseRouterFunction
   *  @Bean can be removed
   *  Can be  private method
   */
  @Bean
  public RouterFunction<ServerResponse> routeRouterPath() {
    return RouterFunctions.route()
        .path("router", this::serverResponseRouterFunction)
        .build();
  }

  /**
   * There can be Multiple RouterFunctions
   */
  @Bean
  public RouterFunction<ServerResponse> serverResponseRouterFunction() {
    return RouterFunctions.route()
        .GET("router/square/{input}", RequestPredicates.path("*/1?"), requestHandler::squareHandler)
        .GET("router/square/{input}", req -> ServerResponse.badRequest().bodyValue("Only 10 to 19 allowed"))
        .GET("router/table/{input}", requestHandler::tableHandler)
        .GET("router/table/{input}/stream", requestHandler::tableStreamHandler)
        .POST("router/multiply", requestHandler::multiplyHandler)
        .GET("router/square/{input}/validate", requestHandler::squareHandlerWithValidation)
        .onError(InputValidationException.class, exceptionHandler())
        .build();
  }

  private BiFunction<Throwable, ServerRequest, Mono<ServerResponse>> exceptionHandler() {
    return (err, req) -> {
      InputValidationException ex = (InputValidationException) err;
      InputFailedValidation res = new InputFailedValidation();
      res.setInput(ex.getInput());
      res.setMessage(ex.getMessage());
      res.setErrorCode(ex.getErrorCode());
      return ServerResponse.badRequest().bodyValue(res);

    };
  }
}
