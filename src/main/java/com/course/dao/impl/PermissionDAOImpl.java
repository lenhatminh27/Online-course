package com.course.dao.impl;

import com.course.common.utils.HibernateUtils;
import com.course.dao.PermissionDAO;
import com.course.entity.PermissionEntity;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class PermissionDAOImpl implements PermissionDAO {

    @Override
    public List<PermissionEntity> getAllPermission() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM PermissionEntity";
            return session.createQuery(hql, PermissionEntity.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Long> getPermissionByRoleID(Long roleID) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = """
                SELECT p.id 
                FROM RoleEntity r 
                JOIN r.permissions p 
                WHERE r.id = :roleID
            """;
            return session.createQuery(hql, Long.class)
                    .setParameter("roleID", roleID)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<PermissionEntity> findAllByIds(List<Long> permissionIds) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM PermissionEntity p WHERE p.id IN :permissionIds";
            return session.createQuery(hql, PermissionEntity.class)
                    .setParameter("permissionIds", permissionIds)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public PermissionEntity findById(Long id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.get(PermissionEntity.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

