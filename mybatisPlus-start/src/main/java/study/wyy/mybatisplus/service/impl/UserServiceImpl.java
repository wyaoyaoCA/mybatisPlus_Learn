package study.wyy.mybatisplus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import study.wyy.mybatisplus.dao.UserMapper;
import study.wyy.mybatisplus.pojo.User;
import study.wyy.mybatisplus.service.UserService;


/**
 * @author wyaoyao
 * @data 2019-11-13 16:05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {



    /**
     * 分页查询用户
     * @return
     */
    @Override
    public IPage<User> pageUser(String name){
        // 这里书写我们的业务逻辑
        Page page = new Page(1, 2);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper();
        wrapper.like(User::getName,"雨");
        IPage result = super.page(page, wrapper);
        return result;
    }
}
