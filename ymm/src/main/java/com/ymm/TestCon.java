package com.ymm;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Arnold.zhao
 * @version TestCon.java, v 0.1 2021-10-18 17:53 Arnold.zhao Exp $$
 */
@RestController
public class TestCon {

    @GetMapping("/test")
    public String test(String param) {
        try {
            int a = 0 / 0;
        } catch (Exception e) {
            throw e;
        }
        return param + "-End";
    }

    @RequestMapping(value = "/offlineText1",
            method = {RequestMethod.GET, RequestMethod.POST}, produces = {MediaType.TEXT_PLAIN_VALUE})
    public R offlineText1(String status) {
        return new R("abc", "异常了1");
    }

    @RequestMapping(value = "/offlineText",
            method = {RequestMethod.GET, RequestMethod.POST}, produces = {MediaType.TEXT_PLAIN_VALUE})
    public String offlineText(String status) {
        if (status.equals("a")) {
            return "OK";
        } else {
            try {
                int a = 0 / 0;
            } catch (Exception e) {
                throw e;
            }
        }
        return "no ok";
    }

    @RequestMapping(value = "/offlineJson",
            method = {RequestMethod.GET, RequestMethod.POST}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String offlineJson(String status) {
        if (status.equals("a")) {
            return "OK";
        } else {
            try {
                int a = 0 / 0;
            } catch (Exception e) {
                throw e;
            }
        }
        return "no ok";
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
