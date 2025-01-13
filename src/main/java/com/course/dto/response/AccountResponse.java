package com.course.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String email;

    private String avatar;

    private List<String> roles;

}
