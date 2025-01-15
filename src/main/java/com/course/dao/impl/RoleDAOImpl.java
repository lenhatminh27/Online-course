package com.course.dao.impl;

import com.course.common.utils.HibernateUtils;
import com.course.dao.RoleDAO;
import com.course.entity.RoleEntity;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class RoleDAOImpl implements RoleDAO {

    @Override
    public boolean existsById(Long id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(r) > 0 FROM RoleEntity r WHERE r.id = :id";
            Query<Boolean> query = session.createQuery(hql, Boolean.class);
            query.setParameter("id", id);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<RoleEntity> findAll() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM RoleEntity";
            return session.createQuery(hql, RoleEntity.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public RoleEntity update(RoleEntity role) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(role);
            session.getTransaction().commit();
            return role;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public RoleEntity findById(Long id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            RoleEntity role = session.get(RoleEntity.class, id);
            if (role == null) {
                return null;
            }
            return role;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
