package study.wyy.mybatisplus.high.config;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wyaoyao
 * @data 2019-11-11 21:23
 */
@Configuration
public class MybatisPlusConfig {

    //@Bean
    public ISqlInjector sqlInjector(){
        // 已过时，在3.1.1之后不需要配置这个bean，这里就注释掉
        return new LogicSqlInjector();
    }
}
