package com.rabbit.ai.controller;

import com.rabbit.ai.service.AiChatService;
import com.rabbit.common.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * AI控制器
 */
@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiChatService aiChatService;

    /**
     * AI对话（同步）
     */
    @PostMapping("/chat")
    public Result<String> chat(@RequestBody String message) {
        String response = aiChatService.chat(0, message);
        return Result.success(response);
    }

    /**
     * AI对话（流式）
     */
    @PostMapping(value = "/chat/stream", produces = "text/event-stream")
    public Flux<String> chatStream(@RequestBody String message) {
        return aiChatService.chatStream(0, message);
    }


}

