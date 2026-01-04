package org.example.server;

import cn.hutool.core.util.IdUtil;
import org.example.api.User;
import org.example.api.UserService;

/**
 * @Author violinwang
 * @Date 2026/1/4
 *
 * UserServiceImpl 实现 UserService 接口
 * 这一方是服务提供方，所以会进行接口的实现。
 **/

public class UserServiceImpl implements UserService {

    @Override
    public User getUser(Long id) {
        // 根据id查询用户信息
        // 这里直接构造了一个User对象，实际业务中应该从数据库或者缓存中查询用户信息
        return User.builder()
                .id(id)
                .name(IdUtil.fastSimpleUUID()) // 随机生成用户名
                .build();
    }
}
