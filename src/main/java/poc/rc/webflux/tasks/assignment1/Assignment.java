package poc.rc.webflux.tasks.assignment1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class Assignment {

  @Autowired
  private ReqHandler reqHandler;

  @Bean
  public RouterFunction<ServerResponse> serverResRouterFunction() {
    return RouterFunctions.route()
        .GET("calculator/{inp1}/{inp2}", isOperation("+"), reqHandler::addHandler)
        .GET("calculator/{inp1}/{inp2}", isOperation("-"), reqHandler::subHandler)
        .GET("calculator/{inp1}/{inp2}", isOperation("*"), reqHandler::mulHandler)
        .GET("calculator/{inp1}/{inp2}", isOperation("/"), reqHandler::divHandler)
//        .GET("calculator/{inp1}/{inp2}", RequestPredicates.path("*/?/?"), reqHandler::calcHandler)
        .build();
  }

  private RequestPredicate isOperation(String operation) {
    return RequestPredicates.headers(headers ->
        operation.equals(headers.asHttpHeaders().toSingleValueMap().get("OP")));
  }
}
