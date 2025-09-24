package top.aoyudi.deepseek.entity.response.balance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DSBalance {
    private Boolean is_available;
    private BalanceInfo[] balance_infos;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class BalanceInfo{
        // 货币，人民币或美元
        private String currency;
        // 总的可用余额，包括赠金和充值余额
        private String total_balance;
        // 未过期的赠金余额
        private String granted_balance;
        // 充值余额
        private String topped_up_balance;
    }
}
