package com.lambdaschool.usermodel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main class to start the application.
 */
//@EnableJpaAuditing // most of the time in testing this can be turned off (unless you're testing auditing, of course)
@SpringBootApplication
public class UserModelApplication
{

    /**
     * Main method to start the application.
     *
     * @param args Not used in this application.
     */
    public static void main(String[] args)
    {
        SpringApplication.run(UserModelApplication.class,
                              args);
    }
}
