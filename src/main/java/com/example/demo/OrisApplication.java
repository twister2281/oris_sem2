package com.example.demo;

import com.example.demo.config.AppConfig;
import com.example.demo.entity.UserEntity;
import com.example.demo.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class OrisApplication {
    public static void main(String[] args) {
        // Запуск
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        UserService service = context.getBean(UserService.class);

        // Инициализация
        service.init();
        System.out.println("=== CRUD ===\n");

        // CREATE
        System.out.println("Добавляем:");
        service.create("Иван");
        service.create("Мария");
        service.create("Петр");
        service.getAll().forEach(System.out::println);

        // READ
        System.out.println("\nИщем id=2:");
        System.out.println(service.getOne(2L));

        // UPDATE
        System.out.println("\nОбновляем id=2:");
        service.update(2L, "Мария Ивановна");
        service.getAll().forEach(System.out::println);

        // DELETE
        System.out.println("\nУдаляем id=3:");
        service.delete(3L);
        service.getAll().forEach(System.out::println);

        // Закрываем
        ((AnnotationConfigApplicationContext) context).close();
    }
}