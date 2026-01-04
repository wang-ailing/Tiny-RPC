package org.example.rpc.transport.socket.simple;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.example.rpc.exception.RpcException;
import org.example.rpc.protocol.RpcRequest;
import org.example.rpc.protocol.RpcResponse;
import org.example.rpc.transport.RpcServer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author violinwang
 * @Date 2026/1/4
 **/
@Data
@AllArgsConstructor
@Slf4j
public class SocketRpcServer implements RpcServer {
    private String host;
    private int port;

    @Override
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket()){
            serverSocket.bind(new InetSocketAddress(host, port));
            log.info("Server started on {}:{}", host, port);
            // 这样就不用写finally了，try-with-resources自动关闭资源
            Socket socket;
            while ((socket = serverSocket.accept()) != null){
                log.info("client connected: {}", socket.getInetAddress());
                try (Socket currentSocket = socket;
                     ObjectOutputStream objectOutputStream = new ObjectOutputStream(currentSocket.getOutputStream());
                     ObjectInputStream objectInputStream = new ObjectInputStream(currentSocket.getInputStream())) {

                    // A. 读取客户端请求
                    // 对应 Client 端的 writeObject
                    RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
                    log.info("Received request: {}", rpcRequest);

                    // TODO: 假装调用了服务端的业务逻辑，这里省略了
                    String result = "Hello, " + rpcRequest.getRpcServiceName();

                    // C. 发送响应给客户端
                    // 这里需要注意的是返回的ID是请求端相同的ID，因为请求端和响应端是一对一的关系
                    objectOutputStream.writeObject(RpcResponse.success(rpcRequest.getRequestId(), result));

                    objectOutputStream.flush();

                } catch (Exception e) {
                    log.error("处理请求失败", e);
                }
            }

        } catch (Exception e) {
            log.error("服务端启动失败", e);
            throw new RpcException("Failed to start SocketRpcServer", e);
        }
    }
}
