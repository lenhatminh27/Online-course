package com.course.entity;

import com.course.entity.enums.EPermission;
import lombok.*;

import javax.persistence.*;


@Entity
@Table(name = "permissions")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PermissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private EPermission name;
}
