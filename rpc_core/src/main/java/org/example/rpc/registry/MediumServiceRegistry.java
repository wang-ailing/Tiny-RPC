package org.example.rpc.registry;

import lombok.extern.slf4j.Slf4j;
import org.example.rpc.config.ServiceConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author violinwang
 * @Date 2026/1/5
 * @Description 服务端上 通过服务名称找到服务对象
 * 1. 在服务提供方上 添加一个服务
 * 2. 在服务提供方上 查找一个服务
 * 之所以不需要删除一个服务 是因为一般情况下，服务的生命周期和JVM的生命周期一致，随着JVM启动，服务也会启动，随着JVM关闭，服务也会关闭。
 * 因此不需要手动关闭服务。
 **/
@Slf4j
public class MediumServiceRegistry implements ServiceRegistry {
    private static final Map<String, Object> serviceMap = new HashMap<>();

    @Override
    public void register(ServiceConfig serviceConfig) {
        Map<String, Object> currentServiceMap = serviceConfig.getServiceMap();
        if (currentServiceMap == null || currentServiceMap.isEmpty()) {
            log.error("该服务没有实现任何接口，实现类：{}", serviceConfig.getService().getClass().getCanonicalName());
            throw new IllegalArgumentException("该服务没有实现任何接口");
        }
        log.debug("登记服务：{}", currentServiceMap.keySet());
        serviceMap.putAll(currentServiceMap); // 登记服务，这种方式是直接在serviceMap中覆盖添加
    }

    @Override
    public Object getService(String serviceName) {
        Object object = serviceMap.getOrDefault(serviceName, null);
        if (object == null) {
            log.error("没有找到服务：{}", serviceName);
            throw new IllegalArgumentException("没有找到服务：" + serviceName);
        }
        log.debug("找到服务：{}", serviceName);
        return object;
    }
}
