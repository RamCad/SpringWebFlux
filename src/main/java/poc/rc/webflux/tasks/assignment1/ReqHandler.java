package poc.rc.webflux.tasks.assignment1;

import java.util.Map;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class ReqHandler {

  public Mono<ServerResponse> calcHandler(ServerRequest serverRequest) {
    Integer inp1 = Integer.valueOf(serverRequest.pathVariable("inp1"));
    Integer inp2 = Integer.valueOf(serverRequest.pathVariable("inp2"));
    Map<String, String> headers = serverRequest.headers().asHttpHeaders().toSingleValueMap();
    String op = headers.get("OP");
    Integer result = Integer.parseInt(inp1 + op + inp2);
    return ServerResponse.ok().bodyValue(result);
  }

  public Mono<ServerResponse> addHandler(ServerRequest req) {
    return process(req, (a, b) -> ServerResponse.ok().bodyValue(a + b));
  }

  public Mono<ServerResponse> subHandler(ServerRequest req) {
    return process(req, (a, b) -> ServerResponse.ok().bodyValue(a - b));
  }

  public Mono<ServerResponse> mulHandler(ServerRequest req) {
    return process(req, (a, b) -> ServerResponse.ok().bodyValue(a * b));
  }

  public Mono<ServerResponse> divHandler(ServerRequest req) {
    return process(req, (a, b) -> b != 0 ? ServerResponse.ok().bodyValue(a / b)
        : ServerResponse.badRequest().bodyValue("inp2 cannot be Zero(0)"));
  }

  private Mono<ServerResponse> process(ServerRequest req,
      BiFunction<Integer, Integer, Mono<ServerResponse>> calcLogic) {
    int a = Integer.valueOf(req.pathVariable("inp1"));
    int b = Integer.valueOf(req.pathVariable("inp2"));
    return calcLogic.apply(a, b);
  }


}
