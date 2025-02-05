    package com.course.entity;

    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;
    import org.hibernate.annotations.CreationTimestamp;
    import org.hibernate.annotations.UpdateTimestamp;

    import javax.persistence.*;
    import java.time.LocalDateTime;
    import java.util.ArrayList;
    import java.util.List;

    @Entity
    @Table(name = "blog_statistic")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class BlogStatisticEntity {
        @Id
        private Long id;

        @OneToOne(fetch = FetchType.EAGER)
        @MapsId // Maps the ID of BlogEntity
        @JoinColumn(name = "id") // Ensures the foreign key and primary key are the same
        private BlogEntity blog;

        @Column(name = "likes")
        private Long likes = 0L;

        @Column(name = "views")
        private Long views = 0L;

        @Column(name = "create_at", updatable = false)
        @CreationTimestamp
        private LocalDateTime createAt = LocalDateTime.now();

        @Column(name = "updated_at")
        @UpdateTimestamp
        private LocalDateTime updatedAt;


        @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE})
        @JoinTable(name = "account_blog_statistic",
                joinColumns = @JoinColumn(name = "blog_statistic_id"),
                inverseJoinColumns = @JoinColumn(name = "account_id")
        )
        List<AccountEntity> accounts = new ArrayList<>();

    }
