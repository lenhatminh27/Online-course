package com.course.config;

import com.course.common.utils.HibernateUtils;
import com.course.config.adapter.LocalDateTimeAdapter;
import com.course.core.bean.annotations.Bean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

public class WebConfiguration {

    @Bean
    public EntityManager entityManager() {
        final var sessionFactory = HibernateUtils.getSessionFactory();
        return sessionFactory.createEntityManager();
    }

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

}
