package org.example.springwithmockcodegen;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("request-mapping")
public class TestController {
  @GetMapping("get-mapping")
  public ResponseDto responseDto() {
    return new ResponseDto("123", "456");
  }
}
