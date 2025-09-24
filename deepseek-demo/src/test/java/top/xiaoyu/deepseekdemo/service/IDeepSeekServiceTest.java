package top.xiaoyu.deepseekdemo.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import top.aoyudi.deepseek.entity.model.DeepSeekChatModel;
import top.aoyudi.deepseek.entity.model.DeepSeekModel;
import top.aoyudi.deepseek.entity.model.DeepSeekReasonerModel;
import top.aoyudi.deepseek.entity.response.DSResponse;
import top.aoyudi.deepseek.entity.response.message.DSRespMessage;

import javax.annotation.Resource;

import java.util.concurrent.CompletableFuture;


@SpringBootTest
class IDeepSeekServiceTest {

    @Resource(name = "deepSeekServiceImpl")
    private IDeepSeekService deepSeekService;

    @Resource
    private DeepSeekChatModel deepSeekChatModel;

    @Resource
    private DeepSeekReasonerModel deepSeekReasonerModel;

    @Resource
    private DeepSeekChatModel deepSeekChatByStreamModel;

    @Resource
    private DeepSeekReasonerModel deepSeekReasonerByStreamModel;

    @Test
    void chat() {
        CompletableFuture<DSRespMessage> messageCompletableFuture = deepSeekService.chat("你好", deepSeekChatModel);
        DSRespMessage join = messageCompletableFuture.join();
        System.out.println(join.getContent());
    }

    @Test
    void chatWithReasoner() {
        CompletableFuture<DSRespMessage> messageCompletableFuture = deepSeekService.chat("你好", deepSeekReasonerModel);
        DSRespMessage join = messageCompletableFuture.join();
        System.out.println(join.getReasoning_content());
        System.out.println(join.getContent());
    }

    @Test
    void chatByStream() {
        Flux<DSResponse> stringFlux = deepSeekService.chatByStream("你好", deepSeekChatByStreamModel);
        stringFlux.subscribe(line->{
            if (line.getChoices().get(0).getDelta().getReasoning_content()!=null){
                System.out.println(line.getChoices().get(0).getDelta().getReasoning_content());
            }
            if (line.getChoices().get(0).getDelta().getContent()!=null){
                System.out.println(line.getChoices().get(0).getDelta().getContent());
            }
        });
    }

    @Test
    void chatByStreamWithReasoner() {
        Flux<DSResponse> stringFlux = deepSeekService.chatByStream("你好", deepSeekReasonerByStreamModel);
        stringFlux.subscribe(line->{
            if (line.getChoices().get(0).getDelta().getReasoning_content()!=null){
                System.out.println(line.getChoices().get(0).getDelta().getReasoning_content());
            }
            if (line.getChoices().get(0).getDelta().getContent()!=null){
                System.out.println(line.getChoices().get(0).getDelta().getContent());
            }
        });
    }
}