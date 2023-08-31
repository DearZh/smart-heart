package com.smart.heart.es6.example.json.query.template;

import com.smart.heart.es6.example.json.constant.Constant;
import com.smart.heart.es6.example.json.query.bean.ESBoolQueryBean;

import java.net.UnknownHostException;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/3/11
 */
public class Test {


    public static void main(String[] args) throws UnknownHostException {

        /**
         * 封装数据至通用的 ESBoolQueryBean 的愿意是，前端只需要传送规定格式的DSL JSON语句，后端自动解析为格式化的ESBoolQueryBean对象，然后直接查询即可。
         * 而无需针对对应的业务各自实现对应的Query查询的封装。具体看场景来定。目前封装并不是非完善。待改进地方：
         * 1、支持DSL的嵌套查询；BoolQuery 需支持无限嵌套查询（因为实际业务中的确会存在嵌套查询的场景）
         * 2、支持统计的通用查询
         * 3、ESBoolQueryBean 对象属性 Map<String, ESBoolQueryBean.ConditionBean> 需替换Map的数据结构，避免持续使用IdentityHashMap的情况。
         */

        ESBoolQueryBean esQueryBean = new ESBoolQueryBean();
        esQueryBean.setIndex("httpnews");

        //开发时问题，must下面可以有多个 term，由于设计了Map来接收，所以多个term时，key会自动去重，为了避免这种情况，只能先暂时使用IdentityHashMap
        //IdentityHashMap 是可以支持key不去重的map；但是需要保证key值不能是一个对象。所以下面在put数据时，都是重新new String() 进行的操作。。
        Map<String, ESBoolQueryBean.ConditionBean> boolMustMap = new IdentityHashMap<>();

        //表示查询关系为 term，字段 matchcontent 所对应值为“content内容测试”的数据
        boolMustMap.put(initString(Constant.U_TERM), new ESBoolQueryBean.ConditionBean("matchcontent", "content内容测试"));
        //表示查询关系为 term，字段 title 所对应值为“title内容”的数据
        boolMustMap.put(initString(Constant.U_TERM), new ESBoolQueryBean.ConditionBean("title", "title内容"));
        //表示查询关系为 range，字段 clickseq 所对应值,大于等于 2000 ，小于等于 3000 的数据
        boolMustMap.put(initString(Constant.U_RANGE), new ESBoolQueryBean.ConditionBean("clickseq", 2000, 3000));
        esQueryBean.setBoolMustMap(boolMustMap);

        ESQueryTemplate esQueryTemplate = new ESBoolQueryTemplate();
        List<Map<String, Object>> list = esQueryTemplate.search(esQueryBean);
        System.out.println(list.size());
    }

    private static String initString(String str) {
        return new String(str);
    }

}
