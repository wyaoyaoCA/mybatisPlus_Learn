package study.wyy.mybatisplus.high.test;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import study.wyy.mybatisplus.pojo.User;
import study.wyy.mybatisplus.service.UserService;

/**
 * @author wyaoyao
 * @data 2019-11-13 17:09
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class GetOneTest {

    @Autowired
    UserService userService;


    @Test
    public void getOne(){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lt("age",40);
        User user = userService.getOne(wrapper);
        // 如果查出不是一条记录，是会抛出TooManyResultsException这个异常
        // 到这里和selectOne是一样的，但是getOne还有两个参数的重载方法
        System.out.println("查询结果："+ user);

    }


    @Test
    public void getOne2(){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lt("age",40);
        /**
         * 第二个参数，表示如果查出多条，是否抛出异常，默认为true抛出异常
         */
        User user = userService.getOne(wrapper,false);
        // 如果查出不是一条记录，就不会抛出异常，会返回查出的结果里的第一条
        System.out.println("查询结果："+ user);
    }
}
