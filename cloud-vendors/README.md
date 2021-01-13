##
swagger 地址：http://localhost:8080/swagger-ui.html#


## 华为
华为新建工单：

```
1、 /osm/{type}/listProductCatgories 查询产品类型列表（华为支持根据产品名称进行产品类别的搜索）
    得到对应的产品ID
    
2、/osm/{type}/listProducts/{productCategoryId} 获取问题类别
    根据对应的产品ID，查询得到对应的产品问题类别ID
    
3、获取工单来源
    
3、/osm/{type}/insertOsm 新建工单
    必填参数项：
    type：HUAWEI
    productCategoryId：产品ID
    withBusinessTypeId：产品问题类别ID
    withSourceId ：工单来源ID
    withSimpleDescription：工单问题
    
```

## 阿里

阿里新建工单：
```
1、 /osm/{type}/listProductCatgories 查询产品类型列表
    得到对应的产品Code
    
2、/osm/{type}/listProducts/{productCategoryId} 获取问题类别
    根据对应的产品Code，查询得到对应的产品问题类别ID
    
3、/osm/{type}/insertOsm 新建工单
    必填参数项：
    email：接受工单消息的邮箱
    type：ALIBABA
    productCategoryId：产品Code
    withBusinessTypeId：产品问题类别ID
    withSimpleDescription：工单问题
```