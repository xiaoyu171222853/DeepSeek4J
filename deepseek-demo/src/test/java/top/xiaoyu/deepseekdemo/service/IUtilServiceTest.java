package top.xiaoyu.deepseekdemo.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@Log4j2
@SpringBootTest
class IUtilServiceTest {

    @Autowired
    private IUtilService utilService;

    @Test
    void testTool() throws Exception {
        String time = utilService.testTool("现在几点了");
        log.info(time);
    }
}