package com.course.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Table(name = "refresh_token")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "refresh_token", nullable = false, columnDefinition = "MEDIUMTEXT")
    String refreshToken;

    @Column(name = "revoked")
    boolean revoked;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    AccountEntity account;

}
