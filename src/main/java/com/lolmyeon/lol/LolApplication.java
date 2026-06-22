package com.lolmyeon.lol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LolApplication {

    // EnableScheduling 추가 -- 매일 오전 5시 내용값초기화를 위함

    public static void main(String[] args) {
        SpringApplication.run(LolApplication.class, args);
    }
}