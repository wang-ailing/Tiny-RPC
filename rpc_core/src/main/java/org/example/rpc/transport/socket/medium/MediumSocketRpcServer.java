package org.example.rpc.transport.socket.medium;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.rpc.config.ServiceConfig;
import org.example.rpc.exception.RpcException;
import org.example.rpc.protocol.RpcRequest;
import org.example.rpc.protocol.RpcResponse;
import org.example.rpc.registry.MediumServiceRegistry;
import org.example.rpc.registry.ServiceRegistry;
import org.example.rpc.transport.RpcServer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author violinwang
 * @Date 2026/1/5
 * @Description MediumSocketRpcServer
 * 其实这里更倾向于：当前类只负责网络通信的部分
 * 除了网络通信之外的工作交给
 **/
@Data
//@AllArgsConstructor
@Slf4j
public class MediumSocketRpcServer implements MediumRpcServer {
    private String host;   // 为什么server不需要host？ 因为server都是在本地启动服务，只需要指定端口就可以了
    private int port;
    private ServiceRegistry serviceRegistry;

    public MediumSocketRpcServer(int port){
        this.host = "localhost";
        this.port = port;
        this.serviceRegistry = new MediumServiceRegistry();
    }

    public MediumSocketRpcServer(String host, int port){
        this.host = host;
        this.port = port;
        this.serviceRegistry = new MediumServiceRegistry();
    }

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

                    // B. 处理请求
                    // TODO: 假装调用了服务端的业务逻辑，这里省略了
                    rpcRequest.getInterfaceName();
                    rpcRequest.getMethodName();
                    rpcRequest.getParameterTypes();
                    rpcRequest.getParameters();
                    Object object = serviceRegistry.getService(rpcRequest.getInterfaceName());
                    // Method method = service.getClass().getMethod(
                    //    rpcRequest.getMethodName(),
                    //    rpcRequest.getParameterTypes()
                    //);
                    Object result = object.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes()) //拿到方法
                            .invoke(object, rpcRequest.getParameters());

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

    @Override
    public void publish(ServiceConfig serviceConfig) {
        serviceRegistry.register(serviceConfig);
    }

}
