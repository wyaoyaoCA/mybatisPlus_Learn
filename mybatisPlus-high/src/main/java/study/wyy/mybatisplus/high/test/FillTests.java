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

@SpringBootTest()
@RunWith(SpringRunner.class)
@Slf4j
@ActiveProfiles("high")
public class FillTests {

    /**
     * 注意这里导入high包下
     */
    @Autowired
    UserHighMapper userMapper;

    /**
     * 测试删除一个用户
     */
    @Test
    public void insert() {
        User user = new User();
        user.setName("张三");
        user.setAge(17);
        user.setEmail("zhangsan@qq.com");
        user.setManagerId(1088248166370832385L);
        int insert = userMapper.insert(user);
        System.out.println(insert);
        // INSERT INTO user ( id, name, age, email, manager_id, create_time ) VALUES ( ?, ?, ?, ?, ?, ? )
        // 1193902169107656705(Long), 张三(String), 17(Integer), zhangsan@qq.com(String), 1088248166370832385(Long), 2019-11-11T22:43:53.411(LocalDateTime)
    }
}