package org.example.rpc.transport.socket.medium;

import org.example.rpc.config.ServiceConfig;

/**
 * @Author violinwang
 * @Date 2026/1/5
 **/

public interface MediumRpcServer {
    void start();
    void publish(ServiceConfig serviceConfig);
}
