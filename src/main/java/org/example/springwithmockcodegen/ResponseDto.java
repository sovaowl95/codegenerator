package org.example.springwithmockcodegen;

import lombok.Data;

@Data
public class ResponseDto {
  private final String number;
  private final String number1;

  public ResponseDto(String number, String number1) {
    this.number = number;
    this.number1 = number1;
  }
}
