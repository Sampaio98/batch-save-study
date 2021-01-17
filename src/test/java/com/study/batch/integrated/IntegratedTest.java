package com.study.batch.integrated;

import com.github.javafaker.Faker;
import com.github.javafaker.service.RandomService;
import com.study.batch.model.Employee;
import com.study.batch.model.User;
import com.study.batch.repository.EmployeeRepository;
import com.study.batch.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SpringBootTest
public class IntegratedTest {
    private static final int CHUNK_SIZE = 10000;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    private final Faker faker = new Faker(Locale.getDefault(), new RandomService());

    @Test
    public void employee_save_batch_by_chunk_size() {
        List<Employee> employees = new ArrayList<>();

        for (int i = 0; i < CHUNK_SIZE; i++) {
            String fullName = faker.name().fullName();
            String email = fullName
                    .replaceAll("[.,' ']", "")
                    .concat("@teste.com");

            Employee employee = Employee.builder()
                    .name(fullName)
                    .email(StringUtils.uncapitalize(email))
                    .isNew(true)
                    .build();

            employees.add(employee);
        }

        long start = System.currentTimeMillis();
        employeeRepository.saveAll(employees);
        long result = System.currentTimeMillis() - start;
        System.out.println("============> O batch save foi feito em: " + result + "ms");
    }

    @Test
    public void user_save_batch_by_chunk_size() {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < CHUNK_SIZE; i++) {
            String username = faker.superhero().prefix() + faker.animal().name() + faker.number().digits(4);

            User user = User.builder()
                    .username(StringUtils.uncapitalize(username))
                    .password(faker.lorem().characters(8, 16))
                    .build();

            users.add(user);
        }

        long start = System.currentTimeMillis();
        userRepository.saveAll(users);
        long result = System.currentTimeMillis() - start;
        System.out.println("============> O batch save foi feito em: " + result + "ms");
    }

}
