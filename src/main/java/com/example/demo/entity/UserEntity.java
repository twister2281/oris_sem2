package com.example.demo.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserTaskEntity> tasks = new ArrayList<>();

    public UserEntity() {}

    public UserEntity(String name) {
        this.name = name;
        this.status = UserStatus.ACTIVE;
    }

    public UserEntity(String name, UserStatus status) {
        this.name = name;
        this.status = status;
    }

    @PrePersist
    public void applyDefaults() {
        if (status == null) {
            status = UserStatus.ACTIVE;
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public List<UserTaskEntity> getTasks() {
        return tasks;
    }

    public void setTasks(List<UserTaskEntity> tasks) {
        this.tasks = tasks;
    }

    public void addTask(UserTaskEntity task) {
        tasks.add(task);
        task.setUser(this);
    }

    public void removeTask(UserTaskEntity task) {
        tasks.remove(task);
        task.setUser(null);
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "', status=" + status + ", tasks=" + tasks.size() + "}";
    }
}