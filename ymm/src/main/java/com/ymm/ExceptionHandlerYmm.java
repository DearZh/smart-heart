package com.ymm;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Arnold.zhao
 * @version ExceptionHandler.java, v 0.1 2022-08-23 20:32 Arnold.zhao Exp $$
 */
@RestControllerAdvice
public class ExceptionHandlerYmm {

//    @ExceptionHandler(Exception.class)
////    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public String handleGlobalException(Exception e) {
//        return JSON.toJSONString(new R(e.getMessage(), "异常了"));
//    }

    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public C handleGlobalException1(Exception e) {
        return new C(e.getMessage(), "异常了1");
    }

    class C {
        String exceptionMsg;
        String msg;

        public C(String exceptionMsg, String msg) {
            this.exceptionMsg = exceptionMsg;
            this.msg = msg;
        }

    }
}
