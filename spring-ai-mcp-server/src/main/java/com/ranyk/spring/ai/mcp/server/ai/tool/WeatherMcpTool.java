package com.ranyk.spring.ai.mcp.server.ai.tool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

/**
 * CLASS_NAME: WeatherMcpTool.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 关于天气的 MCP 服务工具
 * @date: 2026-06-26
 */
@Slf4j
@Component
@SuppressWarnings("unused")
public class WeatherMcpTool {

    /**
     * 根据传入的城市名称查询模拟天气预报（演示数据，非真实气象来源）
     *
     * @param city 城市名称
     * @return 天气预报信息字符串
     */
    @McpTool(
            name = "getWeatherForecast",
            description = "根据传入的城市名称查询模拟天气预报（演示数据，非真实气象来源）",
            generateOutputSchema = true
    )
    public String getWeatherForecast(@McpToolParam(description = "城市名称, 例如: 北京、 上海、 成都等") String city) {
        log.info("执行获取天气预报的 MCP-Server , 当前的入参 city => {}", city);
        // TODO 真实场景下因该是在此处去调用天气预报的接口获取指定的城市或区域的天气预报信息, 现在作为模拟, 直接返回固定的天气信息
        return "当前城市: " + city + " 的天气预报信息为: 天气很好, 温度适宜, 湿度适中, 风向: 东风, 风级: 3级, 最低温度: 22℃, 最高温度: 30℃";
    }
}
