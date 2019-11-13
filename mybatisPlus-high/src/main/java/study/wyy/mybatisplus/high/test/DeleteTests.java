package study.wyy.mybatisplus.high.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import study.wyy.mybatisplus.high.dao.UserHighMapper;
import study.wyy.mybatisplus.high.pojo.User;

import java.util.List;

@SpringBootTest()
@RunWith(SpringRunner.class)
@Slf4j
@ActiveProfiles("high")
public class DeleteTests {

    /**
     * 注意这里导入high包下
     */
    @Autowired
    UserHighMapper userMapper;

    /**
     * 测试删除一个用户
     */
    @Test
    public void deleteById(){
        //  UPDATE user SET deleted=1 WHERE id=? AND deleted=0
        // 删除语句变成了update语句
        int rows = userMapper.deleteById(1094592041087729666L);
        log.info("影响函数：{}",rows);
    }

    /**
     * select update语句都会给加上WHERE deleted=0这个限制条件，
     * 但是如果自定义的sql则需要自己在sql加上，mybatisplus不会自动帮我们加上
     */
    @Test
    public void select(){
        // SELECT id,name,age,email,manager_id,create_time,update_time,version,deleted FROM user WHERE deleted=0
        List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);
    }
}
