package org.example.server;

import org.example.api.UserService;
import org.example.rpc.config.ServiceConfig;
import org.example.rpc.registry.MediumServiceRegistry;
import org.example.rpc.registry.ServiceRegistry;
import org.example.rpc.transport.socket.medium.MediumSocketRpcServer;

/**
 * @Author violinwang
 * @Date 2026/1/5
 **/

public class MediumSocketServerMain {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        ServiceConfig serviceConfig = new ServiceConfig(userService);

//        System.out.println(serviceConfig.getServiceKey());

//        ServiceRegistry serviceRegistry = new MediumServiceRegistry();
//        serviceRegistry.register(serviceConfig);
//        System.out.println(UserService.class.getName());接口的 getName 和 getCanonicalName 获取结果是一样的欸
//        Object service = serviceRegistry.getService(UserService.class.getCanonicalName());
//        System.out.println(service);
        MediumSocketRpcServer mediumSocketRpcServer = new MediumSocketRpcServer(8888);
        mediumSocketRpcServer.publish(serviceConfig);
        mediumSocketRpcServer.start();

    }
}
