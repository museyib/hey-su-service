package az.inci.heysu;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
public class HeySuApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(HeySuApplication.class, args);
    }

}
