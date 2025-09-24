package top.aoyudi.deepseek.entity.enums;

/**
 * DeepSeek 模型枚举
 * 用于表示不同的 DeepSeek 模型
 */
public enum DeepSeekModelEnum {
    // 普通对话模型
    CHAT("deepseek-chat"),
    // 带有思考的对话模型
    REASONER("deepseek-reasoner");
    // 模型名
    private final String modelName;

    /**
     * 内部构造函数
     * @param modelName 模型名
     */
    DeepSeekModelEnum(String modelName) {
        this.modelName = modelName;
    }

    /**
     * 获取模型名称
     *
     * @return 模型名称
     */
    public String getModelName() {
        return modelName;
    }
}