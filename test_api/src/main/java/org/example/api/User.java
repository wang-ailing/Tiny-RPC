package org.example.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author violinwang
 * @Date 2026/1/3
 **/
@Data //包含Getter和Setter方法  @Getter + @Setter + @ToString
@NoArgsConstructor // 无参构造
@AllArgsConstructor // 全参构造
@Builder // 链式调用    链式调用可以使代码更简洁，更易读
// User类
// 包含name和age两个属性
public class User implements Serializable {
    // serialVersionUID 一般来说需要指定一下
    // 对于实现Serializable接口的类，如果没有显式指定 serialVersionUID
    // JVM会自动生成一个 serialVersionUID，但是这个值是根据类的内部细节决定的
    // 如果类的实现发生变化，这个值也会发生变化，这就可能导致反序列化失败
    private static final long serialVersionUID = 1L; //序列化id

    // 包含id, name, age三个属性
    // 使用包装类而不是基本数据类型
    // 包装类可以区分 未设置值的null 和 设置了值的0  也就是空值 & 零值
    // 包装类才能支持泛型  而基本数据类型不支持泛型
    private Long id;
    private String name;
    private Integer age;
}
