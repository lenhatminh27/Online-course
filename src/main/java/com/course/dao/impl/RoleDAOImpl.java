package com.course.dao.impl;

import com.course.common.utils.HibernateUtils;
import com.course.dao.RoleDAO;
import com.course.entity.RoleEntity;
import com.course.entity.enums.ERole;
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


    @Override
    public RoleEntity findByName(ERole name) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM RoleEntity a WHERE a.name = :name";
            return session.createQuery(hql, RoleEntity.class)
                    .setParameter("name", name)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<RoleEntity> findByNameLike(List<String> names) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String queryStr = "SELECT r FROM RoleEntity r WHERE ";
            // Tạo một điều kiện LIKE cho mỗi tên trong danh sách
            for (int i = 0; i < names.size(); i++) {
                queryStr += "r.name LIKE :name" + i;
                if (i < names.size() - 1) {
                    queryStr += " OR "; // Dùng OR nếu có nhiều tên
                }
            }

            var query = session.createQuery(queryStr, RoleEntity.class);

            // Thiết lậptham số cho mỗi tên trong danh sách
            for (int i = 0; i < names.size(); i++) {
                query.setParameter("name" + i, "%" + names.get(i) + "%");
            }

            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
