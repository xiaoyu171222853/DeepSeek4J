package top.aoyudi.deepseek.entity.enums;

/**
 * DeepSeek 角色枚举类
 * 定义不同的角色类型
 */
public enum DeepSeekRoleEnum {
    // 用户
    USER("user"),
    // 助理
    ASSISTANT("assistant"),
    // 系统提示次
    SYSTEM("system"),
    // 调用工具
    TOOL("tool");
    // 角色
    private final String role;

    /**
     * 内部构造函数
     * @param role 角色
     */
    DeepSeekRoleEnum(String role) {
        this.role = role;
    }

    /**
     * 获取角色
     * @return 角色名
     */
    public String getRole() {
        return role;
    }
}