package com.example.snsboard.exception.user;

import com.example.snsboard.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class UserNotAllowedException extends ClientErrorException {

  public UserNotAllowedException() {
    super(HttpStatus.FORBIDDEN, "User not allowed.");
  }
}
