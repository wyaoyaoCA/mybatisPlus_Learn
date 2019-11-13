package study.wyy.mybatisplus.test;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import study.wyy.mybatisplus.dao.UserMapper;
import study.wyy.mybatisplus.pojo.User;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wyaoyao
 * @data 2019-11-13 10:05
 * 入门案例测试
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class BaseMapperTest {

    @Autowired
    UserMapper userMapper;

    @Test
    public void test(){

        User user = userMapper.selectById(1088248166370832385L);
        System.out.println("执行结果："+user);

    }


    /**
     * 测试insert
     */
    @Test
    public void insert(){
        User user = new User();
        user.setName("wyaoyao");
        user.setAge(26);
        user.setManagerId(1088248166370832385L);
        user.setCreateTime(LocalDateTime.now());
        user.setEmail("wyaoyao@mybatis.com");
        int insert = userMapper.insert(user);
        System.out.println("影响行数："+insert);
        // 获取新增的id
        System.out.println("新增的id：" + user.getId());
    }

    /**************************************************************************
     *                  查询相关的测试
     * ************************************************************************/


    /**
     * 根据id批量查询
     */
    @Test
    public void selectBatchIds(){
        // SELECT id,name,age,email,manager_id,create_time FROM user WHERE id IN ( ? , ? )
        List<User> users = userMapper.selectBatchIds(Arrays.asList(1088250446457389058L, 1094590409767661570L));
        users.stream().forEach(
                user-> System.out.println(user)
        );
    }

    /**
     * 条件查询，map封装的就是查询条件，且是等值查询
     */
    @Test
    public void selectByMap(){

        Map<String, Object> columnMap = new HashMap<>();

        // 注意的是 这里map的key是数据库里的列名而不是实体类的属性名字
        columnMap.put("name","王天风");
        columnMap.put("age",25);
        /**
         * 查询（根据 columnMap 条件）
         *
         * @param columnMap 表字段 map 对象
         */
        // SELECT id,name,age,email,manager_id,create_time FROM user WHERE name = ? AND age = ?

        List<User> users = userMapper.selectByMap(columnMap);

        users.stream().forEach(
                user-> System.out.println(user)
        );
    }

    @Test
    public void selectOne(){
        // querryWrapper是mybatisPlus提供的条件构造器
        QueryWrapper<User> querryWrapper = new QueryWrapper<>();
        // 设置id=1088250446457389058L的条件
        querryWrapper.eq("id",1088250446457389058L);
        // 这里需要的是一个QuerryWrapper
        // SELECT id,name,age,email,manager_id,create_time FROM user WHERE id = ?
        User user = userMapper.selectOne(querryWrapper);
        System.out.println("执行结果：" + user);

        // 如果selectOne查出的不是一条结果呢

    }

    /**
     *   如果selectOne查出的不是一条结果呢
     */
    @Test
    public void selectOne2(){
        // 这里直接传null，where后面没有条件
        // SELECT id,name,age,email,manager_id,create_time FROM user
        User user = userMapper.selectOne(null);
        System.out.println("执行结果：" + user);

        /**
         * 这里抛出异常TooManyResultsException
         *  Expected one result (or null) to be returned by selectOne(), but found: 5
         *  期望为一个结果，但是查出5个
         */
    }

    /**
     * 根据 Wrapper 条件，查询总记录数
     */
    @Test
    public void selectCount(){
        QueryWrapper<User> queryWrapper =new QueryWrapper<>();
        // 设置条件：年龄小于30的
        queryWrapper.lt("age",30);
        // SELECT COUNT( 1 ) FROM user WHERE age < ?
        // 统计年龄小于30的数据量
        Integer count = userMapper.selectCount(queryWrapper);
        System.out.println("执行结果：" + count);
    }

    /**
     * 根据QueryWrapper构造的条件,查询全部记录
     */
    @Test
    public void selectList(){
        QueryWrapper<User> queryWrapper =new QueryWrapper<>();

        // 设置条件：年龄小于30的
        // SELECT id,name,age,email,manager_id,create_time FROM user WHERE age < ?
        queryWrapper.lt("age",30);
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    /**
     * selectMaps的应用场景1：当表中的列特别多，但实际只需要几个列时，这时返回一个实体类有些不必要
     * 字段，这个时候就可以使用selectMaps方法 ,
     */
    @Test
    public void selectMaps(){
        QueryWrapper<User> queryWrapper =new QueryWrapper<>();
        // 通过select方法筛选出你要查询的字段，类似与select name,age from user
        queryWrapper.likeRight("name","李").select("name","age");
        // key:就是数据库字段名
        // value：就是对应字段的值
        List<Map<String, Object>> maps = userMapper.selectMaps(queryWrapper);
        maps.forEach(System.out::println);

    }
    /**
     * selectMaps的应用场景2：查询统计结果
     * 按照直属上级分组，查询每组的平均年龄、最大年龄、最小年龄，并且只取年龄总和小于100的组
     * SELECT AVG(age) avg_age,MIN(age) min_age,MAX(age) max_age
     * FROM `user`
     * GROUP BY `manager_id`
     * HAVING SUM(age)<100;
     */
    @Test
    public void selectMaps2(){
        QueryWrapper<User> queryWrapper =new QueryWrapper<>();
        queryWrapper.select("AVG(age) avg_age","MIN(age) min_age","MAX(age) max_age")
                .groupBy("manager_id")
                .having("SUM(age)<{0}", 100);

        // SELECT AVG(age) avg_age,MIN(age) min_age,MAX(age) max_age
        // FROM user
        // GROUP BY manager_id
        // HAVING SUM(age)<?
        List<Map<String, Object>> maps = userMapper.selectMaps(queryWrapper);
        maps.forEach(System.out::println);
        /**
         * {max_age=40, avg_age=40.0000, min_age=40}
         * {max_age=25, avg_age=25.0000, min_age=25}
         * {max_age=31, avg_age=28.3333, min_age=26}
         */
    }

    /**
     * selectObjs只返回第一列，其它列被遗弃
     * 应用场景：只需返回一列的时候
     */
    @Test
    public void selectObjs(){
        QueryWrapper<User> queryWrapper =new QueryWrapper<>();
        // 通过select方法筛选出你要查询的字段，类似与select name,age from user
        queryWrapper.likeRight("name","李").select("name","age");
        // key:就是数据库字段名
        // value：就是对应字段的值
        List<Object> maps = userMapper.selectObjs(queryWrapper);
        maps.forEach(System.out::println);
    }
    /**************************************************************************
     *                  删除相关的测试
     *                  和查询的方法是能够对应起来，这里就不做太多测试
     *                  deleteById 根据 ID 删除
     *                  deleteByMap
     *                  delete
     *                  deleteBatchIds： 删除（根据ID 批量删除）
     * ************************************************************************/
    /**
     * 测试根据id删除
     */
    @Test
    public void deleteById(){
        // 删除上一个测试插入的数据
        int row = userMapper.deleteById(1194442680788721665L);
        System.out.println("影响行数："+row);
    }

    /**
     * 根据条件，删除记录
     */
    @Test
    public void deleteByMap(){

        Map<String, Object> map = new HashMap<>();
        map.put("name","wwyaoyao");
        map.put("age",26);
        // DELETE FROM user WHERE name = ? AND age = ?
        int i = userMapper.deleteByMap(map);
        System.out.println("影响函数："+ i);
    }
    /**
     * 根据条件，删除记录
     *
     * 和deleteByMap的区别就是，deleteByMap使用map封装，这种查询只能是等值（=）
     * delete是根据QueryWrapper这个条件构造器，就可以构造更为复杂的条件
     *
     */
    @Test
    public void delete(){
        QueryWrapper<User> queryWrapper =new QueryWrapper<>();
        //DELETE FROM user WHERE name = ? AND age = ?
        queryWrapper.eq("name","wwyaoyao").eq("age",26);
        userMapper.delete(queryWrapper);
    }


    /**************************************************************************
     *                  更新相关的测试
     *                  updateById
     *                  update
     * ************************************************************************/

    /**
     * 根据ID修改
     * 将ID为11094590409767661570L的年龄改为19
     */
    @Test
    public void updateById(){

        User user = new User();
        user.setId(1094590409767661570L);
        user.setAge(19);
        // UPDATE user SET age=? WHERE id=?
        int row = userMapper.updateById(user);
        System.out.println("影响行数："+row);
    }

    /**
     * 根据条件更新，条件是通过条件构造器设置
     * 将年龄小于30的年龄设置为32
     */
    @Test
    public void update(){
        User user = new User();
        user.setAge(32);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lt("age",30);
        int update = userMapper.update(user, queryWrapper);
        System.out.println("影响行数："+update);
    }

}
