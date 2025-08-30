package top.xiaoyu.deepseekdemo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.aoyudi.deepseek.entity.response.balance.DSBalance;
import top.aoyudi.deepseek.service.DeepSeekBalanceService;

/**
 * 获取余额服务
 */
@Service
public class DeepSeekBalanceServiceImpl {
    // 直接注入
    @Autowired
    private DeepSeekBalanceService deepSeekBalanceService;

    /**
     * 获取余额
     * @return 余额
     */
    public DSBalance getBalance() {
        return deepSeekBalanceService.getBalance();
    }

}
