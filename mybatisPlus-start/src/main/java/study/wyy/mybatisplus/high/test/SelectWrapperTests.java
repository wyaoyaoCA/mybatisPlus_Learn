package study.wyy.mybatisplus.high.test;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import study.wyy.mybatisplus.dao.UserMapper;
import study.wyy.mybatisplus.pojo.User;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
/**
 * 条件构造器
 * 默认and连接
 */
public class SelectWrapperTests {

    @Autowired
    private UserMapper userMapper;

    /**
     * 1、名字中包含雨并且年龄小于40
     * 	name like '%雨%' and age<40
     */
    @Test
    public void test1(){
        QueryWrapper queryWrapper = new QueryWrapper<User>();
        //QueryWrapper<User> query = Wrappers.query();
        queryWrapper.like("name","雨");
        queryWrapper.lt("age",40);
        List list = userMapper.selectList(queryWrapper);

        list.stream().forEach(
                user-> System.out.println(user)
        );


    }

    /**
     *
     *      实体作为条件构造器的方法参数
     *      默认是等值处理：name="王天风"
     *      也可自己定义：在实体类中的属性使用这个下面的数据 SqlCondition定义了许多字符串常量
     *
     *          @TableField(condition= SqlCondition.LIKE)
     *      SqlCondition定义了的字符串常量不满足我们需求，也可以自己直接写
     *          @TableField(condition= "%s&lt;#{%s}")
     */
    @Test
    public void test2(){

        User user1 = new User();
        user1.setName("王天风");
        user1.setAge(25);
        QueryWrapper queryWrapper = new QueryWrapper<User>(user1);
        List list = userMapper.selectList(queryWrapper);

        list.stream().forEach(
                user-> System.out.println(user)
        );
    }

    /**
     * 名字中包含雨年并且龄大于等于20且小于等于40并且email不为空
     * name like '%雨%' and age between 20 and 40 and email is not null
     */

    @Test
    public void test3(){
        QueryWrapper queryWrapper = new QueryWrapper<User>();
        queryWrapper.like("name","雨");
        queryWrapper.between("age",20,40);
        queryWrapper.isNotNull("email");
        List list = userMapper.selectList(queryWrapper);
        list.stream().forEach(
                user-> System.out.println(user)
        );
    }


    /**
     * 名字为王姓或者年龄大于等于25，按照年龄降序排列，年龄相同按照id升序排列
     *    name like '王%' or age>=25 order by age desc,id asc
     */
    @Test
    public void test4(){
        QueryWrapper queryWrapper = new QueryWrapper<User>();
        queryWrapper.likeRight("name", "王");
        queryWrapper.or();
        queryWrapper.ge("age",25);
        queryWrapper.orderByDesc("age");
        queryWrapper.orderByAsc("id");
        List list = userMapper.selectList(queryWrapper);
        list.stream().forEach(
                user-> System.out.println(user)
        );
    }

    /**
     * 创建日期为2019年2月14日并且直属上级为名字为王姓
     * date_format(create_time,'%Y-%m-%d')='2019-02-14' and manager_id in (select id from user where name like '王%')
     */
    @Test
    public void test5(){
        QueryWrapper queryWrapper = new QueryWrapper<User>();
        // 没有sql注入
        queryWrapper.apply("date_format(create_time,'%Y-%m-%d')={0}","2019-02-14");
        // 这个有sql注入的风险
        queryWrapper.apply("date_format(create_time,'%Y-%m-%d')='2019-02-14'");
        queryWrapper.inSql("manager_id","select id from user where name like '王%'");
        List list = userMapper.selectList(queryWrapper);
        list.stream().forEach(
                user-> System.out.println(user)
        );
    }


    /**
     * 名字为王姓并且（年龄小于40或邮箱不为空）
     *     name like '王%' and (age<40 or email is not null)
     */
    @Test
    public void test6(){

        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.likeRight("name","王");
        /**
         * 这里接受的是一个Function<QueryWrapper<User>,QueryWrapper<User>>函数式接口，
         * demo案例
         * @see study.wyy.mybatisplus.java8.function.Demo01Function
          */
        queryWrapper.and(wq->wq.lt("age",40).or().isNotNull("email"));

        List list = userMapper.selectList(queryWrapper);
        list.stream().forEach(
                user-> System.out.println(user)
        );
    }


    /**
     * 名字为王姓或者（年龄小于40并且年龄大于20并且邮箱不为空）
     *     name like '王%' or (age<40 and age>20 and email is not null)
     */
    @Test
    public void test7 (){

        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.likeRight("name","王");
        queryWrapper.or(wq->wq.lt("age",40).gt("age",20).isNotNull("email"));
        List list = userMapper.selectList(queryWrapper);
        list.stream().forEach(
                user-> System.out.println(user)
        );
    }

    /**
     * 考察的是条件不是以and开头的
     * 年龄小于40或邮箱不为空）并且名字为王姓
     *     (age<40 or email is not null) and name like '王%'
     */

    @Test
    public void test8 (){

        QueryWrapper<User> queryWrapper = new QueryWrapper();
        /**
         * 这里接受的是一个Function<QueryWrapper<User>,QueryWrapper<User>>函数式接口，
         */
        queryWrapper.nested(wq->wq.lt("age",40).or().isNotNull("email"))
                .likeRight("name","王");
        List list = userMapper.selectList(queryWrapper);
        list.stream().forEach(
                user-> System.out.println(user)
        );
    }

    /**
     * 年龄为30、31、34、35
     *     age in (30、31、34、35)
     */

    @Test
    public void test9 (){

        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.in("age", Arrays.asList(30,31,34,35));
        List list = userMapper.selectList(queryWrapper);
        list.stream().forEach(
                user-> System.out.println(user)
        );
    }

    /**
     * 9、只返回满足条件的其中一条语句即可
     * limit 1
     */

    @Test
    public void test10 (){

        QueryWrapper<User> queryWrapper = new QueryWrapper();
        // last: 只能调用一次,多次调用以最后一次为准 有sql注入的风险,请谨慎使用
        // SELECT id,name,age,email,manager_id,create_time FROM user WHERE age IN (?,?,?,?) limit 1
        queryWrapper.in("age", Arrays.asList(30,31,34,35)).last("limit 1");
        List list = userMapper.selectList(queryWrapper);
        list.stream().forEach(
                user-> System.out.println(user)
        );
    }

    /**
     * select不列出全部字段
     * select id,name
     * 	           from user
     * 	           where name like '%雨%' and age<40
     */

    @Test
    public void test11 (){

        QueryWrapper<User> queryWrapper = new QueryWrapper();
        // SELECT id,name FROM user WHERE name LIKE ? AND age < ?
        queryWrapper.select("id","name").like("name","雨").lt("age",40);
        List list = userMapper.selectList(queryWrapper);
        list.stream().forEach(
                user-> System.out.println(user)
        );
    }

    /**
     * 反向排除，排除create_time和manager_id
     */
    @Test
    public void test12 (){

        QueryWrapper<User> queryWrapper = new QueryWrapper();
        // SELECT id,name,age,email FROM user WHERE name LIKE ? AND age < ?
        queryWrapper.like("name","雨").lt("age",40).
                // 参数一：Class<T> entityClass  实体类的Class
                // 参数二：函数式接口Predicate<TableFieldInfo> predicate
                // TableFieldInfo 表的基本信息
                select(User.class,
                        tbInfo->!tbInfo.getColumn().equals("create_time")&&
                        !tbInfo.getColumn().equals("manager_id"));
        List list = userMapper.selectList(queryWrapper);
        list.stream().forEach(
                user-> System.out.println(user)
        );
    }

    /**
     * 反向排除，排除create_time和manager_id
     */
    @Test
    public void test13 (){

        QueryWrapper<User> queryWrapper = new QueryWrapper();
        String name = "王";
        /**
         * 参数一：boolean：为true的时候，才会将参数二参数三构成的查询条件拼接到查询语句中
         *
         * 之前的方法都是调的这个方法，默认是true
         */
        queryWrapper.like(StringUtils.isNotEmpty(name),"name",name);

        List list = userMapper.selectList(queryWrapper);
        list.stream().forEach(
                user-> System.out.println(user)
        );
    }

    /**
     * allEq
     */
    @Test
    public void selectList_allEq() {
        QueryWrapper<User> query = new QueryWrapper<>();
        Map<String, Object> params = new HashMap<>();
        params.put("name", "刘明强");
        params.put("age", 31);
        params.put("email", null);
//        query.allEq(params,false);//第二个参数表示如果列值为null是否按IS NULL查询，false则忽略null列的查询
        query.allEq((k, v) -> !k.equals("name"), params, false);//第一个参数是过滤器
        List<User> list = userMapper.selectList(query);
        list.forEach(System.out::println);
    }

    /**
     * selectMaps的应用场景1：当表中的列特别多，但实际只需要几个列时，这时返回一个实体类有些不必要
     */
    @Test
    public void selectMaps() {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.like("name", "雨").lt("age", 40).select("name", "age");
        List<Map<String, Object>> maps = userMapper.selectMaps(query);
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
    public void selectMaps2() {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.select("AVG(age) avg_age", "MIN(age) min_age", "MAX(age) max_age")
                .groupBy("manager_id")
                .having("SUM(age)<{0}", 100);
        List<Map<String, Object>> maps = userMapper.selectMaps(query);
        maps.forEach(System.out::println);
    }


    /**
     * selectObjs只返回第一列，其它列被遗弃
     * 应用场景：只需返回一列的时候
     */
    @Test
    public void selectObjs() {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.like("name", "雨").lt("age", 40).select("name", "age");
        List<Object> list = userMapper.selectObjs(query);
        list.forEach(System.out::println);
    }

    /**
     * 返回总记录数
     */
    @Test
    public void selectCount() {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.like("name", "雨").lt("age", 40);
        Integer count = userMapper.selectCount(query);
        System.out.println("总记录数：" + count);
    }

    /**
     * selectOne：只能查询一条记录，查询到多条会报错
     */
    @Test
    public void selectOne() {
        QueryWrapper<User> query = new QueryWrapper<>();
        query.like("name", "刘红雨").lt("age", 40);
        User user = userMapper.selectOne(query);
        System.out.println(user);
    }

    /*********************************
     *
     *
     *
     *                  Lambda条件构造器：可防止误写
     *
     *
     *
     * *********************************************8******/

    @Test
    public void test14(){
        //LambdaQueryWrapper<User> lambdaQ = new QueryWrapper<User>().lambda();
        //LambdaQueryWrapper<User> lambdaQ = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<User> lambdaQ = Wrappers.<User>lambdaQuery();
        lambdaQ.likeLeft(User::getName,"雨");
        // 等效下面，这样就避免自己写列名
        //QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //queryWrapper.like("name","雨");
    }

    /**
     * 链式lambda条件构造器：更优雅的书写方式
     */
    @Test
    public void lambdaQueryChainWrapper() {
        List<User> list = new LambdaQueryChainWrapper<User>(userMapper)
                .likeRight(User::getName, "王")
                .and(
                        q -> q
                                .lt(User::getAge, 40)
                                .or()
                                .isNotNull(User::getEmail)
                )
                .list();
        list.forEach(System.out::println);
    }

    /****
     *
     * 自定义sql
     * 准备工作：
     *      1 application.yml中配置mapper映射文件的位置
     *       mybatis-plus:
     *          mapper-locations: mapper/*Mapper.xml
     *      2 在UserMapper声明对应的方法
     *
     */

    @Test
    public void test16(){
        List<User> users = userMapper.selectMe();
        users.stream().forEach(user-> System.out.println(user));

    }

}
