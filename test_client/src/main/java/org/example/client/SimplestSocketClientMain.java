package org.example.client;

import cn.hutool.core.util.IdUtil;
import org.example.api.UserService;
import org.example.rpc.protocol.RpcRequest;
import org.example.rpc.protocol.RpcResponse;
import org.example.rpc.transport.RpcClient;
import org.example.rpc.transport.socket.simple.SocketRpcClient;

/**
 * @Author violinwang
 * @Date 2026/1/4
 * @Desc 最简单的使用RPC框架的例子，同时RPC框架是最简单的那种框架，没有动态代理的那种
 * Client和RPC之间的交互：
 * Client告诉RPC需要调用哪一个接口下的哪一个方法；
 * RPC根据Client的请求，找到对应的实现类，并执行方法。并将方法调用的返回值返回给Client。
 *
 * RPC需要能够做到：
 * 1. 处理Client的请求；
 * 2. 找到对应的实现类；
 * 3. 让服务端执行方法；
 * 4. 返回服务端方法执行的结果。
 *
 * Client能够接收到RPC的返回值。
 **/

public class SimplestSocketClientMain {
    public static void main(String[] args) {
        // 告诉RPC需要调用哪一个接口下的哪一个方法
        RpcRequest request = RpcRequest.builder()
                .requestId(IdUtil.fastSimpleUUID())
                .interfaceName(UserService.class.getName())
                .methodName("getUser")
                .parameterTypes(new Class[]{Long.class})
                .parameters(new Object[]{1L})
                .build();

        RpcClient client = new SocketRpcClient("localhost", 8888);
        RpcResponse response = client.sendRequest(request);
        System.out.println(response.getResult());
        System.out.println(response);
    }
}
