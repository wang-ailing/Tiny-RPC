package org.example.rpc.exception;

/**
 * @Author violinwang
 * @Date 2026/1/4
 * @Desc 自定义RPC异常
 **/

public class RpcException extends RuntimeException {
    public RpcException() {
        super();
    }

    public RpcException(String message) {
        super(message);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(Throwable cause) {
        super(cause);
    }
}
