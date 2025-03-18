package java.com.example.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.example.demo.modules.*.mapper")
public class Demo1Application {
    public static void main(String[] args) {
        //
        SpringApplication.run(Demo1Application.class, args);
    }
}
