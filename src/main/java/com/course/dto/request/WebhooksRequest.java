package com.course.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WebhooksRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    Long id;

    String gateway;

    Date transactionDate;

    String accountNumber;

    String code;

    String content;

    String transferType;

    BigDecimal transferAmount;

    BigDecimal accumulated;

    String subAccount;

    String referenceCode;

    String description;
}
