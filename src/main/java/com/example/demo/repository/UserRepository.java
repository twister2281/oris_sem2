package com.example.demo.repository;

import com.example.demo.entity.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void createTable() {
        // Схема создается Hibernate автоматически через ddl-auto.
    }

    @Transactional
    public void save(UserEntity user) {
        entityManager.persist(user);
    }

    public List<UserEntity> findAll() {
        return entityManager
                .createQuery("select u from UserEntity u", UserEntity.class)
                .getResultList();
    }

    public UserEntity findById(Long id) {
        UserEntity user = entityManager.find(UserEntity.class, id);
        if (user == null) {
            throw new EmptyResultDataAccessException(1);
        }
        return user;
    }

    public Optional<UserEntity> findByUsername(String username) {
        List<UserEntity> users = entityManager
                .createQuery("select u from UserEntity u where u.username = :username", UserEntity.class)
                .setParameter("username", username)
                .setMaxResults(1)
                .getResultList();
        return users.stream().findFirst();
    }

    public boolean existsByUsername(String username) {
        Long count = entityManager
                .createQuery("select count(u) from UserEntity u where u.username = :username", Long.class)
                .setParameter("username", username)
                .getSingleResult();
        return count != null && count > 0;
    }

    @Transactional
    public void update(UserEntity user) {
        UserEntity existing = entityManager.find(UserEntity.class, user.getId());
        if (existing == null) {
            throw new EmptyResultDataAccessException(1);
        }
        existing.setName(user.getName());
        if (user.getUsername() != null) {
            existing.setUsername(user.getUsername());
        }
        if (user.getPasswordHash() != null) {
            existing.setPasswordHash(user.getPasswordHash());
        }
        if (user.getStatus() != null) {
            existing.setStatus(user.getStatus());
        }
    }

    @Transactional
    public void deleteById(Long id) {
        UserEntity existing = entityManager.find(UserEntity.class, id);
        if (existing != null) {
            entityManager.remove(existing);
        }
    }
}

