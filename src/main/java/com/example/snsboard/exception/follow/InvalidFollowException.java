package com.example.snsboard.exception.follow;

import com.example.snsboard.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class InvalidFollowException extends ClientErrorException {

  public InvalidFollowException() {
    super(HttpStatus.BAD_REQUEST, "Invalid follow request.");
  }

  public InvalidFollowException(String errorMessage) {
    super(HttpStatus.BAD_REQUEST, errorMessage);
  }
}
