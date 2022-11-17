package com.example.vuemanage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController //组合了@ResponseBody + @Controller
@SpringBootApplication
public class VuemanageApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
//        SpringApplication.run(VuemanageApplication.class, args);
//        ConfigurableApplicationContext application = SpringApplication.run(VuemanageApplication.class, args);
//        Environment env = application.getEnvironment();
//        String port = env.getProperty("server.port");
////        String path = env.getProperty("server.servlet.context-path");
//        log.info("\n----------------------------------------------------------\n\t" +
//                "Application Sys_Student is running! Access URLs:\n\t" +
//                "Local: \t\thttp://localhost:" + port  + "/\n\t" +
//                "----------------------------------------------------------");
    }


}
