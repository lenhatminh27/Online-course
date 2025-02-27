package com.course.dto.intergration;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Data
public class WalletResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    BigDecimal balance;
}
