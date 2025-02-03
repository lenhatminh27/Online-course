package com.course.dto.request;

import com.course.entity.AccountEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountProfileRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private String firstName;
    private String lastName;
    private String phoneNumber = "";
    private String address = "";
    private String avatar = "";
    private String dateOfBirth = "";
}
