package com.gangtise.test.es.controller;

import com.alibaba.fastjson.JSONObject;
import com.gangtise.test.es.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/3/11
 */
@Slf4j
public class Tools {

    /**
     * 获取流中的内容
     *
     * @param is
     * @return
     */
    public static String getFileContent(InputStream is) {
        StringBuilder sb = new StringBuilder();
        try {
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static JSONObject getMappingObject(String index, ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> mappingMap) {
        try {
            ImmutableOpenMap<String, MappingMetaData> typeMap = mappingMap.get(index);
            MappingMetaData propertieMap = typeMap.get(Constant.TYPE);
            Map<String, Object> m = propertieMap.getSourceAsMap();
            String json = JSONObject.toJSONString(m);
            JSONObject jsonObject = JSONObject.parseObject(json);
            return jsonObject;
        } catch (Exception e) {
            log.error("获取Mapping对象时出现异常...{}", e.getMessage());
            return null;
        }
    }

    public static JSONObject getSettingObject(String index, ImmutableOpenMap<String, Settings> settingMap) {
        try {

            Settings settings = settingMap.get(index);
            log.info(settings.toString());
            return null;
        } catch (Exception e) {
            log.error("获取Setting对象时出现异常...{}", e.getMessage());
            return null;
        }
    }

    /**
     * 格式化字符串
     *
     * @param field
     * @return
     */
    public static String formatField(String field) {
        //去除下划线
        String str = field.replaceAll("_", "");
        //转换成小写
        String column = str.toLowerCase();
        return column;
    }

    /**
     * 格式化字符串
     *
     * @param value
     * @return
     */
    public static String formatValue(String value) {

        String valueStr = "";
        if (!StringUtils.isEmpty(value)) {
            valueStr = value.replaceAll("-", "/");
        }
        return valueStr;
    }

    public static JSONObject getFieldType(String mapping) {
        JSONObject mappingJson = JSONObject.parseObject(mapping);
        return mappingJson.getJSONObject("properties");
    }

    final static boolean isChildDate = true;
    final static List<String> DATE_LiST = Arrays.asList("inserttime", "enddate", "tradingday", "highestadjustedpricedate", "updatetime", "xgrq");

    public static void getValueByType(int type, JSONObject rowObject, String field, ResultSet resultSet) throws SQLException {
        String column = Tools.formatField(field);

        if (type == 5 || type == 4 || type == -5 || type == -6) {
            //长整型 5（SMALLINT）、4（INTEGER）、-5（BIGINT）、-6（TINYINT）
            long columnValue = resultSet.getLong(field);
            rowObject.put(column, columnValue);

        } else if (type == 93) {
            //时间类型 93（TIMESTAMP、DATESTAMP）
            Timestamp timeStamp = resultSet.getTimestamp(field);
            if (timeStamp == null) {
                rowObject.put(column, timeStamp);
            }
            Date date = new Date(timeStamp.getTime());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String columnValue = simpleDateFormat.format(date);

            //子属性的解析方式
            SimpleDateFormat simpleDateFormatChild = new SimpleDateFormat("MM-dd");
            String columnValueChild = simpleDateFormatChild.format(date);
            boolean flag = true;

            //TODO：针对需要修改date为child date的场景
            if (isChildDate) {
                for (int i = 0; i < DATE_LiST.size(); i++) {
                    if (DATE_LiST.get(i).equals(column.toLowerCase())) {
                        JSONObject endDate = new JSONObject();
                        endDate.put("value", columnValue);
                        JSONObject jsonCli = new JSONObject();
                        jsonCli.put("value", columnValueChild);
                        endDate.put("child", jsonCli);
                        rowObject.put(column, endDate);
                        flag = false;
                        break;
                    }
                }
            }
            if (flag) {
                rowObject.put(column, columnValue);
            }

        } else if (type == -7) {
            //布尔类型 -7（BIT）
            boolean columnValue = resultSet.getBoolean(field);
            rowObject.put(column, columnValue);

        } else if (type == 7 || type == 8 || type == 3) {
            //小数类型 7（FLOAT）、8（DOUBLE）、3（DECIMAL）
            //统一用double类型接收
            double columnValue = resultSet.getDouble(field);
            rowObject.put(column, columnValue);
        } else {
            //字符串类型
            //12（VARCHAR）、1(CHAR)、-1(CARCHAR)、91(Date)
            String columnValue = resultSet.getString(field);
            rowObject.put(column, columnValue);
        }
    }

}
