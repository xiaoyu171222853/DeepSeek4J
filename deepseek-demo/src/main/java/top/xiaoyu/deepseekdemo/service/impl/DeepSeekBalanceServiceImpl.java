package top.xiaoyu.deepseekdemo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.aoyudi.deepseek.entity.response.balance.DSBalance;
import top.aoyudi.deepseek.service.DeepSeekBalanceService;

@Service
public class DeepSeekBalanceServiceImpl {
    @Autowired
    private DeepSeekBalanceService deepSeekBalanceService;

    public DSBalance getBalance() {
        return deepSeekBalanceService.getBalance();
    }

}
