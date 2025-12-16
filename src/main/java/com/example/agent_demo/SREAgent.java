package com.example.agent_demo;

import dev.langchain4j.service.spring.AiService;

import com.example.agent_demo.tools.OpsTools;

import dev.langchain4j.service.SystemMessage;

@AiService(tools = { "OpsTools" })
public interface SREAgent {

    @SystemMessage("You are an expert SRE Incident Commander. " +
            "Your goal is to root-cause production issues by coordinating with teams and checking documentation. " +
            "Always start by gathering context from the relevant teams (Backend, DB, Infra). " +
            "Then check Wiki/Runbooks for known solutions. " +
            "Synthesize all information to provide a root cause and a recommended action.")
    String chat(String userMessage);
}
