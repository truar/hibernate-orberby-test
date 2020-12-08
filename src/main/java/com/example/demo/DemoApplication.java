package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.support.TransactionTemplate;

import static java.util.Arrays.asList;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    /**
     * With a single transaction, Hibernate uses the cache, and does not reorder the task.
     */
    @Bean
    public CommandLineRunner orderByNotWorking(EventRepository repository, TransactionTemplate template) {
        return args -> {
            template.executeWithoutResult(transactionStatus -> {
                Event e1 = new Event("e1", asList(
                        new Stage("s1", 2, asList(new Task("t11", 2), new Task("t12", 1))),
                        new Stage("s2", 1, asList(new Task("t21", 2), new Task("t22", 1), new Task("t23", 5))),
                        new Stage("s3", 3, asList(new Task("t31", 1), new Task("t32", 5)))
                ));
                repository.save(e1);
                Event e11 = repository.findByName("e1");

                e11.getStages()
                        .forEach(System.out::println);
            });
        };
    }

    /**
     * With separates transaction, Hibernate fetches stages and tasks in the proper order
     */
    @Bean
    public CommandLineRunner orderByWorking(EventRepository repository, TransactionTemplate template) {
        return args -> {
            template.executeWithoutResult(transactionStatus -> {
                Event e1 = new Event("e1", asList(
                        new Stage("s1", 2, asList(new Task("t11", 2), new Task("t12", 1))),
                        new Stage("s2", 1, asList(new Task("t21", 2), new Task("t22", 1), new Task("t23", 5))),
                        new Stage("s3", 3, asList(new Task("t31", 1), new Task("t32", 5)))
                ));
                repository.save(e1);
            });
            template.executeWithoutResult(transactionStatus -> {
                Event e11 = repository.findByName("e1");

                e11.getStages()
                        .forEach(System.out::println);
            });
        };
    }

}
