package com.course.entity;

import com.course.entity.enums.EPermission;
import com.course.entity.enums.ERole;
import lombok.*;

import javax.persistence.*;

@Table
@Entity(name = "permissions")
@Setter
@Getter
@Builder
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
