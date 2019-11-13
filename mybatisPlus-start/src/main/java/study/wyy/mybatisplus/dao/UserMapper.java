package study.wyy.mybatisplus.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import study.wyy.mybatisplus.pojo.User;

import java.util.List;

/**
 * @author wyaoyao
 * @data 2019-11-09 14:11
 */
public interface UserMapper extends BaseMapper<User> {

    List<User> selectMe();
}
