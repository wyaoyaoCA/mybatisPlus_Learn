package study.wyy.mybatisplus.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import study.wyy.mybatisplus.pojo.User;

/**
 * @author wyaoyao
 * @data 2019-11-13 16:03
 */
public interface UserService extends IService<User> {

    public IPage<User> pageUser(String name);
}
