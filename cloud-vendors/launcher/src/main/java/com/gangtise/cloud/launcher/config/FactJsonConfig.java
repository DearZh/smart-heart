package com.gangtise.cloud.launcher.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.Charset;

/**
 * @Description:
 * @Author: Arnold.zhao
 * @Date: 2021/1/12
 */
@Configuration
public class FactJsonConfig {

    /**
     * SpringMVC List<HttpMessageConverter> 中增加 FastJsonHttpMessageConverter <br/>
     * 对外@ResponseBody 时对象转换json时使用该FastJson规则
     *
     * @return
     */
    @Bean
    FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setCharset(Charset.forName("UTF-8"));
        fastJsonConfig.setSerializerFeatures(
                //数值字段为null则输出0，而非null,
                SerializerFeature.WriteNullNumberAsZero,
                //字符串类型如果为null，则输出“”，而非null
                SerializerFeature.WriteNullStringAsEmpty,
                //Boolean字段如果为null，则输出false，而非null
                SerializerFeature.WriteNullBooleanAsFalse,
                SerializerFeature.WriteDateUseDateFormat
        );
        converter.setFastJsonConfig(fastJsonConfig);
        return converter;
    }

   /* @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        *//*
            需注意，converters中默认还包含很多的HttpMessageConverter，如：ByteArrayHttpMessageConverter等很多 <br/>
            此处使用converters.clear();将会导致所有的转换器全部清空，尽管在下面代码中有重新添加两个转换器，<br/>
            但如果后续程序中需要使用到其它转换器，请记得在此处重新add一下；arnold.zhao 2019/5/2
         *//*
        converters.clear();
        StringHttpMessageConverter converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        converters.add(converter);
        converters.add(fastJsonHttpMessageConverter());
    }*/
}
