package com.smart.heart.es6.support.template;

import java.io.IOException;
import java.util.Map;

/**
 * @Description: ES通用模板接口
 * @Author: Arnold.zhao
 * @Date: 2020/9/27
 */
public interface ESTemplate {
    /**
     * 获取指定索引名的 mapping 字段信息
     *
     * @param indexName 索引名
     * @param indexType 索引类型(_doc)
     * @return Map (字段名,字段类型)
     * @throws IOException
     */
    Map<String, String> getIndexFieldTypeMap(String indexName, String indexType) throws IOException;
}
