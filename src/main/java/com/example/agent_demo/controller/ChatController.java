package com.example.agent_demo.controller;

import com.example.agent_demo.SREAgent;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final SREAgent sreAgent;

    public ChatController(SREAgent sreAgent) {
        this.sreAgent = sreAgent;
    }

    @PostMapping("/chat")
    public String chat(@RequestBody String message) {
        return sreAgent.chat(message);
    }
}
