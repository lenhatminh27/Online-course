package com.course.entity;

import com.course.entity.enums.ETransaction;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private AccountEntity account;

    @Column(name = "amount")
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(name = "min_date")
    private LocalDateTime minDate;

    @Column(name = "max_date")
    private LocalDateTime maxDate;

    @Column(name = "transaction_description")
    private String transactionDescription;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ETransaction status;
}
