package study.wyy.mybatisplus.high.test;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import study.wyy.mybatisplus.dao.UserMapper;
import study.wyy.mybatisplus.pojo.User;

import java.util.Map;

/**
 * @author wyaoyao
 * @data 2019-11-13 15:03
 * 分页查询测试
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class PageTest {


    @Autowired
    UserMapper userMapper;

    @Test
    public void selectPage(){
        // 设置分页信息,当前页为第一页，每页显示两条
        Page<User> page = new Page<User>(1,2);
        // 设置查询条件，查询年纪大于10的
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("age",10);
        IPage<User> userIPage = userMapper.selectPage(page, queryWrapper);
        // 获取分页信息
        System.out.println("当前页："+userIPage.getCurrent());
        System.out.println("总页数："+userIPage.getPages());
        System.out.println("总条数："+userIPage.getTotal());
        System.out.println("记录数："+userIPage.getRecords());
    }

    /**
     * 和上一个分页的区别就是数据库信息封装成了一个map，这个区别
     * 和在seletc查询的时候已经说过，返回的泛型是实体和map的区别了
     */
    @Test
    public void selectMapsPage(){
        // 设置分页信息,当前页为第一页，每页显示两条
        Page<User> page = new Page<User>(1,2);
        // 设置查询条件，查询年纪大于10的
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("age",10);
        IPage<Map<String, Object>> userIPage = userMapper.selectMapsPage(page, queryWrapper);
        // 获取分页信息
        System.out.println("当前页："+userIPage.getCurrent());
        System.out.println("总页数："+userIPage.getPages());
        System.out.println("总条数："+userIPage.getTotal());
        System.out.println("记录数："+userIPage.getRecords());
    }

    /***************************************************************************8
     *      刚刚的查询是进行了两次查询，先查询总记录数，在进行分页查询
     *      如果不想查询总记录数，也是可以的，
     *      使用Page类的三个参数的构造器，构造分页信息，第三个参数就是是否查询总记录数，
     *      true：查询
     *      false：不查询
     */

    @Test
    public void selectMapsPage2(){
        // 设置分页信息,当前页为第一页，每页显示两条
        Page<User> page = new Page<User>(1,2,false);
        // 设置查询条件，查询年纪大于10的
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("age",10);
        IPage<Map<String, Object>> userIPage = userMapper.selectMapsPage(page, queryWrapper);
        // 获取分页信息
        System.out.println("当前页："+userIPage.getCurrent());
        System.out.println("总页数："+userIPage.getPages());
        // 这里返回的总条数就是0了
        System.out.println("总条数："+userIPage.getTotal());
        System.out.println("记录数："+userIPage.getRecords());
    }

}
