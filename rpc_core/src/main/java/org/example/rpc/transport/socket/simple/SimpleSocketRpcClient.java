package org.example.rpc.transport.socket.simple;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.example.rpc.exception.RpcException;
import org.example.rpc.protocol.RpcRequest;
import org.example.rpc.protocol.RpcResponse;
import org.example.rpc.transport.RpcClient;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @Author violinwang
 * @Date 2026/1/4
 **/
@Slf4j
@Data
@AllArgsConstructor
public class SimpleSocketRpcClient implements RpcClient {
    private String host;
    private int port;

    @Override
    public <T> RpcResponse<T> sendRequest(RpcRequest request) {
        try (Socket socket = new Socket(getHost(), getPort())){
            // 如果不放入try括号中，则需要手动关闭socket  socket.close();

            // 发送请求
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(request);
            outputStream.flush();

            // 接收响应
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            RpcResponse<T> response = (RpcResponse<T>) inputStream.readObject();

            return response;
        } catch (Exception e) {
            log.error("客户端请求失败", e); // 必须添加logback日志，否则无法找到error方法
            //抛出RPC异常
            throw new RpcException("SocketRpcClient sendRequest error", e);
        }
//        return null;
    }
}
