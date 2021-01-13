package com.gangtise.cloud.launcher.config;

import com.gangtise.cloud.launcher.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/1/12
 */
@Slf4j
@RestControllerAdvice
public class RestControllerAdvices {

    /**
     * 全局异常.
     *
     * @param e the e
     * @return R
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R handleGlobalException(Exception e) {
        log.error("全局异常信息 ex={}", e.getMessage(), e);
        return R.failed(e.getLocalizedMessage());
    }
}
