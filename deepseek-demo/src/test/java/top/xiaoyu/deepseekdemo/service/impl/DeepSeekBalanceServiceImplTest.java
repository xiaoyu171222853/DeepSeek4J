package top.xiaoyu.deepseekdemo.service.impl;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.aoyudi.deepseek.entity.response.balance.DSBalance;

@Log4j2
@SpringBootTest
class DeepSeekBalanceServiceImplTest {
    @Autowired
    private DeepSeekBalanceServiceImpl deepSeekBalanceServiceImpl;

    @Test
    void getBalance() {
        DSBalance balance = deepSeekBalanceServiceImpl.getBalance();
        log.info(balance);
    }
}