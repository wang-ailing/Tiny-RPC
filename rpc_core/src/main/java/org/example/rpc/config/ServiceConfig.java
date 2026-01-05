package org.example.rpc.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author violinwang
 * @Date 2026/1/5
 * @Description ServiceConfig 是 配置类
 * 这是一种参数对象模式
 * 其实就是传入version、group、service对象，然后组装成Map<String, Object>
 * 配置类
 *
 * 用在服务登记的时候，把服务对象和版本、组装成一个配置对象，然后注册到注册中心
 * 你要告诉RPC框架，我要登记一个服务，这个服务的版本是什么，这个服务的组是什么，这个服务的实现类是什么
 * MediumSocketServerMain   使用的地方是：
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceConfig {
    private String version;
    private String group;
    private Object service; // service object 服务对象
    // 实现类 例如 AdminUserServiceImpl1  或者   CommonServiceImpl2
    // 之所以用 Object，是因为最终这个service需要存在Map里面，所以不能确定具体类型，只能用Object
    // 并且没有单一类型，使用泛型也不合适

    public ServiceConfig(Object service) { // 构造方法不写public的话，就只能当前包下的类才能使用
        this.version = "";
        this.group = "";
        this.service = service;
    }

    public Map<String, Object> getServiceMap() {
        return Arrays.stream(getService().getClass().getInterfaces())
                .map(Class::getCanonicalName)
                .map(name -> name + getVersion() + getGroup())
                .collect(Collectors.toMap(name -> name, name -> getService()));
        // Map的这个流转，就是当前都是String，然后假装给它去一个名字叫name
        // 然后你就说怎么处理这个name就行了
        // 真的很妙，这个collect的用法
    }

    // 由于一个实现类 可能实现了 多个接口，因此从任意一个接口+version+group 都应该能够到对应的实现类
    // 也就是说： 这一个实现类，在Map中会产生多个key对应它
    // interface1.version.group -> implementation1
    // interface2.version.group -> implementation1
    // interface3.version.group -> implementation1
    // 因此需要获得 当前这个配置下，所有的key String
    // 当然，可以不获取 key 然后直接组成Map
    // getName 是给JVM看的，实际上是获取全类名还有一些'$'符号，不利于使用
    // getCanonicalName 是给人看的，实际上是获取包名+类名，利于使用
    public List<String> getServiceKey() {
//        service.getClass().getInterfaces()  返回值是一个数组，数组就要用Arrays.stream借助一下转成Stream流
        return Arrays.stream(getService().getClass().getInterfaces())
                .map(Class::getCanonicalName) // 获得接口的全类名
                .map(name -> name + getVersion() + getGroup()) // 组装key
                .collect(Collectors.toList());
    }
    // getVersion 和 version 两者  更倾向于getVersion，因为getVesion能够在继承时被重写，此时能够正确获取到子类的版本号

}
