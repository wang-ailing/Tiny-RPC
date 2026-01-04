package org.example.rpc.protocol;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author violinwang
 * @Date 2026/1/4
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RpcResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    // 请求ID + 响应状态码 + 响应消息 + 响应结果数据
    private String requestId;
    private Integer statusCode;
    private String statusMessage;
    private T result;

    public static <T> RpcResponse<T> success(String requestId) {
        return RpcResponse.<T>builder()
                .requestId(requestId)
                .statusCode(200)
                .statusMessage("success")
                .build();
    }

    public static <T> RpcResponse<T> success(String requestId, T result) {
        return RpcResponse.<T>builder()
                .requestId(requestId)
                .statusCode(200)
                .statusMessage("success")
                .result(result)
                .build();
    }

    public static <T> RpcResponse<T> fail(String requestId, Integer statusCode, String statusMessage) {
        return RpcResponse.<T>builder()
                .requestId(requestId)
                .statusCode(statusCode)
                .statusMessage(statusMessage)
                .build();
    }

    public static <T> RpcResponse<T> fail(String requestId, String statusMessage) {
        return RpcResponse.<T>builder()
                .requestId(requestId)
                .statusCode(500)
                .statusMessage(statusMessage)
                .build();
    }
}
