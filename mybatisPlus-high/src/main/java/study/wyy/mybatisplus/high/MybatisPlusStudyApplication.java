package study.wyy.mybatisplus.high;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author wyaoyao
 */
@MapperScan("study.wyy.mybatisplus.high.dao")
@SpringBootApplication
public class MybatisPlusStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(study.wyy.mybatisplus.MybatisPlusStudyApplication.class, args);
    }

}
