package top.aoyudi.deepseek.entity.request;

import lombok.Data;
import org.springframework.stereotype.Component;
import top.aoyudi.chat.entity.ChatMessage;
import top.aoyudi.deepseek.entity.model.DeepSeekChatModel;
import top.aoyudi.deepseek.entity.model.DeepSeekModel;
import top.aoyudi.deepseek.entity.model.DeepSeekReasonerModel;
import top.aoyudi.deepseek.entity.request.tool.DSReqFunction;
import top.aoyudi.deepseek.entity.request.tool.DSReqTool;
import top.aoyudi.deepseek.handler.ToolRegister;

import java.util.ArrayList;
import java.util.List;

/**
 * DeepSeek最终请求体——不直接使用，请创建DeepSeekRequest
 */
@Data
public class FinallyRequestBody {
    /**
     * 消息列表
     */
    private List<ChatMessage> messages;

    /**
     * 模型id
     */
    private String model;

    /**
     * 是否流式输出
     */
    private boolean stream;

    /**
     * 降低模型重复相同内容的可能性
     */
    private double frequency_penalty;

    /**
     * 限制一次请求中模型生成 completion 的最大 token 数
     */
    private int max_tokens;

    /**
     * 从而增加模型谈论新主题的可能性。
      */
    private double presence_penalty;

    /**
     * 采样温度，介于 0 和 2 之间。更高的值，如 0.8，会使输出更随机，而更低的值，如 0.2，会使其更加集中和确定。
     */
    private double temperature;
    /**
     * 作为调节采样温度的替代方案，模型会考虑前 top_p 概率的 token 的结果。所以 0.1 就意味着只有包括在最高 10% 概率中的 token 会被考虑。
     */
    private double top_p;

    /**
     * 工具
     */
    private List<DSReqTool> tools;
    /**
     * 可以是 String 或 ToolChoice 对象
     */
    private Object tool_choice;

    /**
     * 是否返回所输出 token 的对数概率。如果为 true，则在 message 的 content 中返回每个输出 token 的对数概率。
     * @param deepSeekRequest
     */
    // private boolean logprobs;

    /**
     * 一个介于 0 到 20 之间的整数 N，指定每个输出位置返回输出概率 top N 的 token，且返回这些 token 的对数概率。指定此参数时，logprobs 必须为 true。
     * @param deepSeekRequest
     */
    // private int top_logprobs;

    /**
     * 构造
     * @param deepSeekRequest deepSeekRequest
     */
    public FinallyRequestBody(DeepSeekRequest deepSeekRequest){
        // 挨个赋值
        this.messages = deepSeekRequest.getMessages();
        // 调用工具列表
        List<String> functionNameList = deepSeekRequest.getDeepSeekModel().getFunction_name_list();
        // 如果工具不为空
        if(!functionNameList.isEmpty()){
            tools = new ArrayList<>();
            functionNameList.forEach(name->{
                DSReqTool dsReqTool = new DSReqTool();
                // 获取方法
                DSReqFunction dsReqFunction = ToolRegister.getDsReqFunction(name);
                if(dsReqFunction == null){
                    throw new IllegalStateException("调用不存在方法:"+name);
                }
                dsReqTool.setFunction(dsReqFunction);
                tools.add(dsReqTool);
            });
            tool_choice = deepSeekRequest.getDeepSeekModel().getTool_choice();
        }
        // 使用模型
        DeepSeekModel theDeepSeekModel = deepSeekRequest.getDeepSeekModel();
        if(theDeepSeekModel instanceof DeepSeekChatModel deepSeekModel){
            this.model=deepSeekModel.getModel();
            this.stream=deepSeekModel.isStream();
        }
        if (theDeepSeekModel instanceof DeepSeekReasonerModel deepSeekModel){
            this.model=deepSeekModel.getModel();
            this.stream=deepSeekModel.isStream();
        }
        this.frequency_penalty= theDeepSeekModel.getFrequency_penalty();
        this.max_tokens= theDeepSeekModel.getMax_tokens();
        this.presence_penalty= theDeepSeekModel.getPresence_penalty();
        this.temperature= theDeepSeekModel.getTemperature();
        this.top_p= theDeepSeekModel.getTop_p();
    }
}
