package org.example.client;

import cn.hutool.core.util.IdUtil;
import org.example.api.UserService;
import org.example.rpc.protocol.RpcRequest;
import org.example.rpc.protocol.RpcResponse;
import org.example.rpc.transport.RpcClient;
import org.example.rpc.transport.socket.simple.SimpleSocketRpcClient;

import java.util.concurrent.*;

public class StressTestClientMain {
    public static void main(String[] args) {
        // 1. 模拟并发数
        int concurrentNum = 100;
        // 总请求次数（如果想测试更多请求，可以把这个数字调大，比如 1000）
        int totalRequests = 100;

        // 2. 手动创建 ThreadPoolExecutor
        // 参数说明：
        // corePoolSize: 核心线程数，保持活动状态的最小线程数
        // maximumPoolSize: 最大线程数，当队列满了之后，会创建新线程直到达到这个值
        // keepAliveTime: 非核心线程空闲存活时间
        // unit: 时间单位
        // workQueue: 任务队列，使用有界队列 ArrayBlockingQueue 防止内存溢出
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                concurrentNum,      // 核心线程数
                concurrentNum,      // 最大线程数 (对于计算密集型或IO密集型，这里策略不同，压测场景下通常设为并发数)
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000), // 设置队列容量，防止堆积过多任务导致OOM
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy() // 拒绝策略：队列满了直接抛异常
        );

        System.out.println("开始压测，并发客户端数(核心线程)：" + concurrentNum);
        long start = System.currentTimeMillis();

        for (int i = 0; i < totalRequests; i++) {
            threadPool.execute(() -> {
                try {
                    // 构造请求
                    RpcRequest request = RpcRequest.builder()
                            .requestId(IdUtil.fastSimpleUUID())
                            .interfaceName(UserService.class.getName())
                            .methodName("getUser")
                            .parameterTypes(new Class[]{Long.class})
                            .parameters(new Object[]{1L})
                            .build();

                    // 发送请求
                    RpcClient client = new SimpleSocketRpcClient("localhost", 8888);
                    RpcResponse response = client.sendRequest(request);

                    // System.out.println("响应结果: " + response.getResult());

                } catch (Exception e) {
                    System.err.println("请求失败: " + e.getMessage());
                }
            });
        }

        // 关闭线程池，不再接收新任务
        threadPool.shutdown();
        try {
            // 等待所有任务结束
            if (threadPool.awaitTermination(5, TimeUnit.MINUTES)) {
                long end = System.currentTimeMillis();
                System.out.println("压测结束");
                System.out.println("总耗时: " + (end - start) + "ms");
            } else {
                System.out.println("压测超时，强制关闭");
                threadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            threadPool.shutdownNow();
        }
    }
}