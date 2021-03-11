package com.smart.heart.es6.example.json.constant;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/3/11
 */
public class Constant {
    /**
     * ES的类型，为了兼容7.X，固定设置为_doc
     */
    public static final String TYPE = "_doc";
    /**
     * ES的属性字段统一
     */
    public static final String PROPERTIES = "properties";

    //关系标识符
    public static final String MUST = "must";
    public static final String SHOULD = "should";
    public static final String NOT_MUST = "not_must";
    public static final String FILTER = "filter";


    //关系内的字段含义标识符
    public static final String U_TERM = "TERM";
    public static final String U_MATCH = "MATCH";
    public static final String U_RANGE = "RANGE";

    //es 字符匹配条件类型
    public enum ConditionType {
        MATCH, RANGE, TERM
    }

}
