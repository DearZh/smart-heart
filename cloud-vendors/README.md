## 阿里云
阿里云只支持AK/SK签名认证机制，且没有提供工单的SDK
```
阿里工单接口签名机制规则可参考：
https://help.aliyun.com/document_detail/163279.html?spm=a2c4g.11186623.6.543.3a595382HQQAeZ
https://help.aliyun.com/document_detail/25492.html?spm=5176.2020520129.0.dexternal.643c46aebtA4VN
阿里的接口签名策略与AWS的接口签名策略相同，AWS可参考：
https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/dev/RESTAuthentication.html

目前的工单&资费的需求只涉及到GET请求的对接，按照签名的构建规则当前程序中所创建的SignatureBuild类所产生的签名也只适应于GET请求，
涉及到Post&文件上传等接口对接需另外改造签名产生规则，关于post接口的签名构建规则，详情可看阿里云或上述AWS的说明；
Arnold.zhao 2021/1/6
```
## 腾讯云
腾讯云也只支持 AK/SK签名机制，但资费的接口支持直接调用SDK对接，所以直接使用SDK即可；

## 华为云
支持Token签名机制和AK/SK签名，最初对接华为云没发现竟然也有AK/SK的签名机制，所以程序中直接对接了Token的认证方式；

```
华为Token和 Ak/SK签名对接规则，看这里:
https://support.huaweicloud.com/api-ticket/ticket_api_00009.html
```
AK/SK 的签名规则业内标准上比较统一，但具体细则上略有不同，各家对比整体90+% 相似度，华为在得到对应的签名后
对应的请求发送方式和阿里&腾讯均不同，整体大同小异，且华为云针对AK/SK的签名提供了SDK，但方法的封装似乎较为原生；

## 开发方式
封装Http后，使用HttpRequest的方式，开发华为云和阿里云的接口对接，腾讯云则使用他们自家的SDK进行对接；


## 2021-1-7
好吧，阿里的签名会偶发性调用接口的时候提示，签名错误，10次接口请求大概会有2-3次，提示签名异常，但
生成签名的代码是不变的，但却会出现这种偶发性异常。。追提了阿里的工单，检查了结果是否是云端解签名的时候的问题
还是本地的代码问题，，最终给出的方案是，他们提供一套工单的SDK，直接使用他们SDK就行。。。尽管在他们的阿里云
SDK平台上，该SDK还没上线，就发给了我一个maven的地址链接...o(╥﹏╥)o 是否靠谱不好说，感觉是还没完全上线的东西。。。
希望后续SDK更新不要太频繁

-----
华为在今天之前的一天。。2020-01-06 工单的API彻底进行了二次升级。。。并且发布了SDK的方案。。。beta版。。
公测版本也是有点尴尬，大概率是要使用华为SDK来做对接了。。。然而。。。工单的API explorer 中竟然还没提供
接口的代码示例。。。。

阿里是API中有SDK的代码示例，但是没上线SDK。。。

华为是上线了SDK，但是API中没有写SDK的代码示例。。。。

这尼玛，这么快速迭代还遗漏东西的方式。感觉后续上线不靠谱啊。。淦。。。
需求需要，最终还是要用他们的SDK来做了。

--------
此版本的hutool http代码及 AK/SK签名 以及 华为Token等代码，拉出去一个新的cloud-nosdk 分支，就此沉寂。

一个项目拉进来三家的SDK。。。希望不要出现jar依赖冲突的问题。。不然只能用
SPI的方法。。做类加载的方式了。。。。。。