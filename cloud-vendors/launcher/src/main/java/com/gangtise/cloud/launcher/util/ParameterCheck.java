package com.gangtise.cloud.launcher.util;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * @Description: 参数状态检测
 * @Author: Arnold.zhao
 * @Date: 2021/1/12
 */
public class ParameterCheck<T> {

    T value;

    public ParameterCheck(T value) {
        this.value = value;
    }

    public static <T> ParameterCheck<T> of(T value) {
        return new ParameterCheck<T>(value);
    }

    public void isNull(Predicate<T> predicate) {
        Optional.of(value)
                .filter(predicate)
                .orElseThrow(() -> new NullPointerException("传参异常,请检查"));
    }
}
