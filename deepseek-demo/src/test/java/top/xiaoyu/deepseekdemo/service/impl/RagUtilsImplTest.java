package top.xiaoyu.deepseekdemo.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RagUtilsImplTest {

    @Autowired
    private RagServiceImpl ragServiceImpl;

    @Test
    void ragTest() {
        ragServiceImpl.ragTest("15岁可以当兵吗");
    }
}