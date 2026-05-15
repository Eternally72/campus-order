package com.rabbit.ai.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

/**
 * 数据库查询工具
 */
public class DatabaseTools {

    @Tool(name = "查询订单",value = "查询用户商品订单")
    public void getOrderTool(@P("用户数据") int id){

    }
}
