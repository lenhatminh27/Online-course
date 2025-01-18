package com.course.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public final class RegisterRequest implements Serializable {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String confirmPassword;
}
