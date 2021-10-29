package com.ymm;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Arnold.zhao
 * @version TestCon.java, v 0.1 2021-10-18 17:53 Arnold.zhao Exp $$
 */
@RestController
public class TestCon {

    @GetMapping("/test")
    public String test(String param) {

        return param + "-End";
    }

    @GetMapping("/test1")
    public String test1(@RequestBody String param) {

        return param + "-End";
    }

    @GetMapping("/log/logsearch/gatewaylogs/mobile/search/{mobile}")
    public String mobileSearch(@PathVariable(value = "mobile") String mobile) {

        return mobile + "DD";
    }

}
