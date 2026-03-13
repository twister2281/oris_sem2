package com.example.demo.repository;

import com.example.demo.entity.UserEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class UserRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<UserEntity> mapper = (rs, num) -> {
        UserEntity u = new UserEntity();
        u.setId(rs.getLong("id"));
        u.setName(rs.getString("name"));
        return u;
    };

    public void createTable() {
        jdbcTemplate.getJdbcTemplate().execute(
                "CREATE TABLE IF NOT EXISTS users (id BIGINT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255))"
        );
    }

    public void save(UserEntity user) {
        jdbcTemplate.update(
                "INSERT INTO users (name) VALUES (:name)",
                Map.of("name", user.getName())
        );
    }

    public List<UserEntity> findAll() {
        return jdbcTemplate.query("SELECT * FROM users", mapper);
    }

    public UserEntity findById(Long id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM users WHERE id = :id",
                Map.of("id", id),
                mapper
        );
    }

    public void update(UserEntity user) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", user.getId())
                .addValue("name", user.getName());
        jdbcTemplate.update("UPDATE users SET name = :name WHERE id = :id", params);
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM users WHERE id = :id", Map.of("id", id));
    }
}

