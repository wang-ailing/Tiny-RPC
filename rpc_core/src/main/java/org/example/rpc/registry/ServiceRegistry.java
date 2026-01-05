package org.example.rpc.registry;

import org.example.rpc.config.ServiceConfig;

/**
 * @Author violinwang
 * @Date 2026/1/5
 * @Description 服务仓库中的服务登记使用Map存储
 * Map中的结构
 * key : 服务名  String    接口名 + 版本号 + 分组
 * value : Service对象
 **/

public interface ServiceRegistry {
//    void register(String serviceName, Object service); // 硬编码服务名登记，并不建议，出bug很难修复
    void register(ServiceConfig serviceConfig);
    Object getService(String serviceName);
}
