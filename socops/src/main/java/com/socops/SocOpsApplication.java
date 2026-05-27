package com.socops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.socops", "ec.com.hvq.paneles"})
public class SocOpsApplication {

    public static void main(String[] commandLineArgs) {
        SpringApplication.run(SocOpsApplication.class, commandLineArgs);
    }
}
