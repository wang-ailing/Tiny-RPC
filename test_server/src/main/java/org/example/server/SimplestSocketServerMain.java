package org.example.server;

import lombok.SneakyThrows;
import org.example.rpc.transport.RpcServer;
import org.example.rpc.transport.socket.simple.SocketRpcServer;

/**
 * @Author violinwang
 * @Date 2026/1/4
 **/

public class SimplestSocketServerMain {
    @SneakyThrows
    public static void main(String[] args){
        RpcServer rpcServer = new SocketRpcServer("localhost", 8888);
        rpcServer.start();
    }
}
