package org.example.client;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.api.UserService;
import org.example.rpc.protocol.RpcRequest;
import org.example.rpc.protocol.RpcResponse;
import org.example.rpc.transport.RpcClient;
import org.example.rpc.transport.socket.simple.SimpleSocketRpcClient;

/**
 * @Author violinwang
 * @Date 2026/1/5
 **/
@Slf4j
public class MediumSocketClientMain {
    public static void main(String[] args) {
        // 告诉RPC需要调用哪一个接口下的哪一个方法
        RpcRequest request = RpcRequest.builder()
                .requestId(IdUtil.fastSimpleUUID())
                .interfaceName(UserService.class.getCanonicalName())
                .methodName("getUser")
                .parameterTypes(new Class[]{Long.class})
                .parameters(new Object[]{1L})
                .build();

        log.debug(request.getInterfaceName());

        RpcClient client = new SimpleSocketRpcClient("localhost", 8888);
        RpcResponse response = client.sendRequest(request);

        log.debug(response.getResult().toString());
        log.debug(response.getStatusCode().toString());
        log.debug(response.toString());
    }
}
