##
swagger 地址：http://192.168.20.167:8080/swagger-ui.html#


# 流程标识
特殊的业务标识，涉及到一个业务需要对应多个接口处理的会在下述给出对应的调用流程

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
华为获取工单留言(需注意：获取工单留言涉及到实时聊天的问题)：
```
1、/osm/{type}/showCaseDetail/{caseId}   查询工单详情
    获取工单的详情信息，同时也会返回对应的message集合[10条],
    此处前端不要使用该详情中所返回的message集合消息，而是调用工单留言接口，获取对应的留言数据
    
2、/osm/{type}/caseAction/{caseId}/{page}    查询工单留言
   默认进入工单详情界面时查询page为1，获取默认的20条留言数据即可；
   数据加载为懒加载方式，如需点击查看更多的聊天记录，重新掉用该接口获取内容即可；
   
3、/osm/{type}/listCaseStatus    新建工单回复
   用户在对应的详情界面回复工单时，调用该接口
   
4、/osm/{type}/listUnread/{caseId}   查询工单未读消息的数量|华为
   前端在对应的详情界面时，需定时调用该接口，查看当前工单是否已经被回复了内容
   
5、/osm/{type}/caseUnread/{caseId}   设置工单消息为已读 | 华为
   查询获取到对应的未读消息数量后，调用该接口设置消息为已读
   
6、重新使用2步骤的查询工单留言接口，获取新的留言内容   

7、对于用户不再对应工单详情中等待聊天回复的场景，如果出现了未读的消息，
应在对应的工单列表中，展示未读消息数量即可
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
## 自建
租户中心向平台发起处理工单
