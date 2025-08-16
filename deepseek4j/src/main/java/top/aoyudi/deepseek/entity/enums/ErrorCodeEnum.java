package top.aoyudi.deepseek.entity.enums;

import lombok.Getter;

/**
 * 错误码枚举
 */
@Getter
public enum ErrorCodeEnum {
    // 格式错误
    BAD_REQUEST(400, "格式错误", "请求体格式错误"),
    // 认证失败
    UNAUTHORIZED(401, "认证失败", "API key 错误，认证失败"),
    // 余额不足
    PAYMENT_REQUIRED(402, "余额不足", "账号余额不足"),
    // 参数错误
    UNPROCESSABLE_ENTITY(422, "参数错误", "请求体参数错误"),
    // 请求速率达到上限
    TOO_MANY_REQUESTS(429, "请求速率达到上限", "请求速率（TPM 或 RPM）达到上限"),
    // 服务器故障
    INTERNAL_SERVER_ERROR(500, "服务器故障", "服务器内部故障"),
    // 服务器繁忙
    SERVICE_UNAVAILABLE(503, "服务器繁忙", "服务器负载过高");

    /**
     * 状态码
     */
    private final int code;
    /**
     * 描述
     */
    private final String description;
    /**
     * 原因
     */
    private final String reason;

    /**
     * 内部构造函数
     * @param code 错误码
     * @param description 描述
     * @param reason 错误原因
     */
    ErrorCodeEnum(int code, String description, String reason) {
        this.code = code;
        this.description = description;
        this.reason = reason;
    }


    /**
     * 根据状态码获取枚举实例的方法
     * @param code 错误码
     * @return 枚举类对象
     */
    public static ErrorCodeEnum fromCode(int code) {
        for (ErrorCodeEnum errorCode : values()) {
            if (errorCode.getCode() == code) {
                return errorCode;
            }
        }
        return null; // 或者抛出异常
    }
}