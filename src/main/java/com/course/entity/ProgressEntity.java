package com.course.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "progress")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProgressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private AccountEntity account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_lesson_id")
    private CourseLessonEntity courseLesson;

    @Column(name = "completed_at")
    @CreationTimestamp
    private LocalDateTime completedAt;
}
