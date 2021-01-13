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

    /**
     * 对象内属性Check
     *
     * @param predicate
     */
    public void isNull(Predicate<T> predicate) {
        Optional.of(value)
                .filter(predicate)
                .orElseThrow(() -> new NullPointerException("传参异常,请检查"));
    }

    /**
     * 参数Check
     *
     * @param clazz
     * @param obj
     */
    public static void isNull(Class clazz, Object... obj) {
        String typeName = clazz.getTypeName();
        String className = typeName.substring(typeName.lastIndexOf(".") + 1);
        if (obj != null && obj.length > 0) {
            for (int i = 0; i < obj.length; i++) {
                if (className.equals("String")) {
                    if (obj[i] == null || "".equals(obj[i].toString())) {
                        throw new NullPointerException("传参异常,请检查");
                    }
                } else {
                    if (obj[i] == null) {
                        throw new NullPointerException("传参异常,请检查");
                    }
                }
            }
        }
    }

}
