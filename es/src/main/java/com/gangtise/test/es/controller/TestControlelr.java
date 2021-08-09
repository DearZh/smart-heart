package com.gangtise.test.es.controller;

import com.alibaba.fastjson.JSONArray;
import com.gangtise.test.es.service.DataServiceImpl;
import com.gangtise.test.es.service.IndexServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/3/11
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestControlelr {

    @Autowired
    private IndexServiceImpl indexService;
    @Autowired
    private DataServiceImpl dataService;

    String indexName = "httpnews";


    @GetMapping("/all")
    public String testAll() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("执行验证：\n");
        stringBuffer.append("新建索引：" + create() + "\n");
        stringBuffer.append("索引批量插入数据：" + insert() + "\n");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stringBuffer.append("休眠 5s 等待数据建立索引并同步完成 >>>>> ");
        stringBuffer.append("索引查询返回结果数：" + select() + "\n");
        stringBuffer.append("索引数据更新：" + update() + "\n");
        stringBuffer.append("索引数据删除：" + delete("32548769") + "\n");
        stringBuffer.append("删除整个索引：" + remove() + "\n");
        return stringBuffer.toString();
    }

    @PostMapping("/create/index")
    public String create() {

        return indexService.createEsIndex(indexName);
    }

    @GetMapping("/remove/index")
    public String remove() {

        return indexService.removeIndex(indexName);
    }


    @PostMapping("/insert/data")
    public String insert() {
        try {
            ClassPathResource dResource = new ClassPathResource("json/data.json");
            InputStream dInputStream = dResource.getInputStream();
            String dataJson = Tools.getFileContent(dInputStream);
            JSONArray jsonArray = JSONArray.parseArray(dataJson);
            dataService.save(indexName, jsonArray);
           /* JSONObject jsonObject = jsonArray.getJSONObject(0);
            dataService.save(indexName, jsonObject.getString("id"), "test", jsonObject);*/
        } catch (Exception e) {
            log.error("insert error: ", e);
            return e.getMessage();
        }
        return "OK";
    }

    @DeleteMapping("/delete/data/{id}")
    public String delete(@PathVariable String id) {

        return dataService.remove(indexName, id);
    }

    @PutMapping("/update/data")
    public String update() {

        return dataService.update(indexName);
    }

    @GetMapping("/select/data")
    public String select() {
        return dataService.select(indexName);

    }
}
