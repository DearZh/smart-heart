/**
 * @Description: 阿里接口签名构建
 * @Author: Arnold.zhao
 * @Date: 2021/1/6
 */
package com.gangtise.cloud.vendors.alibaba.signature;

/**
 * 阿里工单接口签名机制规则可参考：
 * https://help.aliyun.com/document_detail/163279.html?spm=a2c4g.11186623.6.543.3a595382HQQAeZ
 * 阿里的接口签名策略与AWS的接口签名策略相同，AWS可参考：
 * https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/dev/RESTAuthentication.html
 * <p>
 * 目前的工单&资费的需求只涉及到GET请求的对接，按照签名的构建规则当前程序中所创建的SignatureBuild类所产生的签名也只适应于GET请求，
 * 涉及到Post&文件上传等接口对接需另外改造签名产生规则，关于post接口的签名构建规则，详情可看阿里云或上述AWS的说明；
 * Arnold.zhao 2021/1/6
 */