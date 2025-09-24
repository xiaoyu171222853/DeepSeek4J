package top.xiaoyu.deepseekdemo.service;

import reactor.core.publisher.Flux;
import top.aoyudi.deepseek.entity.model.DeepSeekModel;
import top.aoyudi.deepseek.entity.response.DSResponse;
import top.aoyudi.deepseek.entity.response.message.DSRespMessage;

import java.util.concurrent.CompletableFuture;

/**
 * DeepSeek服务调用示例
 */
public interface IDeepSeekService {
    /**
     * chat
     * @param content 聊天信息
     * @param deepSeekModel 聊天模型 DeepSeekModelEnum
     */
    default CompletableFuture<DSRespMessage> chat(String content, DeepSeekModel deepSeekModel){
        return null;
    };

    /**
     * chatByStream
     * @param content     聊天信息
     * @param deepSeekModel 聊天模型 DeepSeekModelEnum
     */
    default Flux<DSResponse> chatByStream(String content, DeepSeekModel deepSeekModel) {
        return null;
    }
}
