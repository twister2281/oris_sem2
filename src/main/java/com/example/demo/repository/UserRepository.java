package com.example.demo.repository;

import com.example.demo.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper<UserEntity> mapper = (rs, num) -> {
        UserEntity u = new UserEntity();
        u.setId(rs.getLong("id"));
        u.setName(rs.getString("name"));
        return u;
    };

    public void createTable() {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS users (id BIGINT AUTO_INCREMENT, name VARCHAR(255))");
    }

    public void save(UserEntity user) {
        jdbcTemplate.update("INSERT INTO users (name) VALUES (?)", user.getName());
    }

    public List<UserEntity> findAll() {
        return jdbcTemplate.query("SELECT * FROM users", mapper);
    }

    public UserEntity findById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE id = ?", mapper, id);
    }

    public void update(UserEntity user) {
        jdbcTemplate.update("UPDATE users SET name = ? WHERE id = ?", user.getName(), user.getId());
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM users WHERE id = ?", id);
    }
}