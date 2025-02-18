package com.course.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "course_discounts")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseDiscountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private CourseEntity course;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "discount", nullable = false)
    private Double discount = 0D; // % 0 => 100

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;
}
