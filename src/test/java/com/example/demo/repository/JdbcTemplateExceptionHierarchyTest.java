package com.example.demo.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class JdbcTemplateExceptionHierarchyTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @BeforeEach
    void setUp() {
        userRepository.createTable();
        namedParameterJdbcTemplate.getJdbcTemplate().execute("DELETE FROM users");
    }

    @Test
    void findByIdThrowsEmptyResultExceptionForMissingUser() {
        EmptyResultDataAccessException exception = assertThrows(
                EmptyResultDataAccessException.class,
                () -> userRepository.findById(9999L)
        );

        assertInstanceOf(DataAccessException.class, exception);
    }

    @Test
    void invalidSqlThrowsBadSqlGrammarException() {
        BadSqlGrammarException exception = assertThrows(
                BadSqlGrammarException.class,
                () -> namedParameterJdbcTemplate.queryForObject(
                        "SELECT missing_column FROM users WHERE id = :id",
                        Map.of("id", 1L),
                        Long.class
                )
        );

        assertInstanceOf(DataAccessException.class, exception);
    }

    @Test
    void duplicatePrimaryKeyThrowsDuplicateKeyException() {
        namedParameterJdbcTemplate.update(
                "INSERT INTO users (id, name, status) VALUES (:id, :name, :status)",
                Map.of("id", 1L, "name", "Alice", "status", "ACTIVE")
        );

        DuplicateKeyException exception = assertThrows(
                DuplicateKeyException.class,
                () -> namedParameterJdbcTemplate.update(
                        "INSERT INTO users (id, name, status) VALUES (:id, :name, :status)",
                        Map.of("id", 1L, "name", "Bob", "status", "ACTIVE")
                )
        );

        assertInstanceOf(DataAccessException.class, exception);
    }
}

