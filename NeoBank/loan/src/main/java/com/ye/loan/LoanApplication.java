package com.ye.loan;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@EnableConfigurationProperties( value = {LoanApplication.class})
@OpenAPIDefinition(
        info = @Info(
                title = "Loan microservice REST API Documentation",
                description = "NeoBank Accounts microservice REST API Documentation",
                version = "v1",
                contact = @Contact(
                        name = "Ye Wint Hlaing",
                        email = "yewinthlaing23@gmail.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.neobank.com"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description =  "NeoBank Loans microservice REST API Documentation",
                url = "https://www.neobank.com/swagger-ui.html"
        )
)
public class LoanApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoanApplication.class, args);
    }

}
