package poc.rc.webflux.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import poc.rc.webflux.dto.Response;
import poc.rc.webflux.service.MathService;

@RestController
@RequestMapping("/math")
public class MathController {

  @Autowired
  private MathService mathService;

  @GetMapping("/square/{input}")
  public Response findSquare(@PathVariable int input) {
    return mathService.findSquare(input);
  }

  @GetMapping("/table/{input}")
  public List<Response> multiplication(@PathVariable int input) {
    return mathService.multiplicationTable(input);
  }
}
