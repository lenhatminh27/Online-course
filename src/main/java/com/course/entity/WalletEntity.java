package com.course.entity;

import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "wallets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletEntity {

    @Id
    private Long id; // This will share the same ID as AccountEntity

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // Maps the ID of AccountEntity
    @JoinColumn(name = "id") // Ensures the foreign key and primary key are the same
    private AccountEntity account;

    @Column(name = "balance")
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
