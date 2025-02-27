package com.course.dto.intergration;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessQrResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    String qr;

    Long tranId;

}
