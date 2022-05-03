package com.smart.heart.agent;

import com.smart.heart.util.Log;

/**
 * @author Arnold.zhao
 * @version AgentTest.java, v 0.1 2022-05-01 18:21 Arnold.zhao Exp $$
 */
public class AgentTest {
    public static void main(String[] args) {
        AgentTest agentTest = new AgentTest();
        agentTest.foo();
    }

    public void foo() {
        Log.out("AgentTest origin code test");
    }

}
