package org.example.rpc.transport;

import org.example.rpc.protocol.RpcRequest;
import org.example.rpc.protocol.RpcResponse;

/**
 * @Author violinwang
 * @Date 2026/1/4
 **/

public interface RpcClient {

    // 注意这里由于返回值的类中需要泛型，所以在返回值前面需要给定泛型，同时需要尖括号包裹，<T>
    <T> RpcResponse<T> sendRequest(RpcRequest request);
}
