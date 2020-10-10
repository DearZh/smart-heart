package com.smart.heart.es6.support.template;

import com.smart.heart.es6.support.ES6Connection;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description: 针对ES6的模板方法实现
 * @Author: Arnold.zhao
 * @Date: 2020/9/27
 */
public class ES6Template extends ES6Connection implements ESTemplate {

    public ES6Template(ESClientMode mode) throws UnknownHostException {
        super(mode);
    }


    @Override
    public Map<String, String> getIndexFieldTypeMap(String indexName, String indexType) throws IOException {
        /**
         * https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.8/java-rest-high-get-mappings.html#_optional_arguments_29
         * Arnold.zhao 2020/9/27
         */

        Map<String, String> fieldType = new LinkedHashMap();
        MappingMetaData mappingMetaData = super.getMapping(indexName, indexType);
        Map<String, Object> sourceMap = mappingMetaData.getSourceAsMap();
        //获取所有properties下的字段Map数据
        Map<String, Object> esMapping = (Map<String, Object>) sourceMap.get("properties");
        /**
         * 解析对应的字段类型
         */
        for (Map.Entry<String, Object> entry : esMapping.entrySet()) {
            Map<String, Object> value = (Map<String, Object>) entry.getValue();
            if (value.containsKey("properties")) {
                fieldType.put(entry.getKey(), "object");
            } else {
                fieldType.put(entry.getKey(), (String) value.get("type"));
            }
        }
        return fieldType;
    }


}
