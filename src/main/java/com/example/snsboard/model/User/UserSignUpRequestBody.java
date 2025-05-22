package com.example.snsboard.model.User;

import jakarta.validation.constraints.NotEmpty;

public record UserSignUpRequestBody(@NotEmpty String username, @NotEmpty String password) {}
