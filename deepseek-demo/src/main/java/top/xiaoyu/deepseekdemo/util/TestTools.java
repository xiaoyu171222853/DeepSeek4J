package top.xiaoyu.deepseekdemo.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import top.aoyudi.deepseek.annotation.ToolFunction;
import top.aoyudi.deepseek.annotation.ToolParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 供大模型调用的本地工具示例
 */
@Log4j2
@Component
public class TestTools {

    @ToolFunction(description = "获取当前时间")
    private String getCurrentDateTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(dtf);
    }

    @ToolFunction(description = "获取指定城市的天气信息")
    public String getWeather(@ToolParam(name = "location",type = "string",description = "城市名称，例如：北京、上海",required = true) String location,
                             @ToolParam(name = "date",type = "string",description = "日期，例如2025年1月5日，或者1月5日等日期格式") String date) {
        return "以下是获取的"+location+"在"+date+"的天气";
    }
}
