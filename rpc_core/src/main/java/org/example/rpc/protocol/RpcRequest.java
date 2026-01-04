package org.example.rpc.protocol;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author violinwang
 * @Date 2026/1/4
 * @Desc 需要在网络中传输的请求对象
 * 什么样的对象需要序列化呢？只要一个对象想要“活着”离开JVM，就需要序列化。
 * 二进制流，二进制数据，网络传输
 *
 * 定义了服务端和客户端之间如何通信
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private String requestId;
    private String interfaceName;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;
//    private Class<?> returnType; 其实不需要返回类型，对于服务请求方来说，方面名和参数列表已经可以确定方法的调用，不需要返回类型
    private String version; // 版本号，用于兼容性，用于后缀
    private String group;   // 组别，用于同一个接口的不同实现类的区分，用于前缀

    // UserService -> CommonUserServiceImpl1.getUser()
    //             -> CommonUserServiceImpl2.getUser()
    //             -> AdminUserServiceImpl1.getUser()
    //             -> AdminUserServiceImpl2.getUser()

    public String getRpcServiceName() {
        return StrUtil.blankToDefault(getGroup(), StrUtil.EMPTY)
                + getInterfaceName()
                + StrUtil.blankToDefault(getVersion(), StrUtil.EMPTY);
    }
}