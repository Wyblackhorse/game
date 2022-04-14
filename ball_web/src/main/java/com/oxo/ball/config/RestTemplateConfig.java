package com.oxo.ball.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * @author none
 */
@Configuration
public class RestTemplateConfig {



    @Bean
    public RestTemplate restTemplate(@Qualifier("simpleClientHttpRequestFactory") ClientHttpRequestFactory factory) {
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("utf8")));
        return restTemplate;
    }

    @Bean("simpleClientHttpRequestFactory")
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(3000);
        return factory;
    }

    @Bean("poolClientHttpRequestFactory")
    public HttpComponentsClientHttpRequestFactory poolClientHttpRequestFactory(){
        // 长链接保持时间长度20秒
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager =
                new PoolingHttpClientConnectionManager(20, TimeUnit.SECONDS);
        // 设置最大链接数
        poolingHttpClientConnectionManager.setMaxTotal(2*getMaxCpuCore() + 3 );
        // 单路由的并发数
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(2*getMaxCpuCore());

        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setConnectionManager(poolingHttpClientConnectionManager);

        // 重试次数3次，并开启
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(3,true));
        HttpClient httpClient = httpClientBuilder.build();
        // 保持长链接配置，keep-alive
        httpClientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());

        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

        // 链接超时配置 5秒
        httpComponentsClientHttpRequestFactory.setConnectTimeout(3000);
        // 连接读取超时配置
        httpComponentsClientHttpRequestFactory.setReadTimeout(3000);
        // 连接池不够用时候等待时间长度设置，分词那边 500毫秒 ，我们这边设置成1秒
        httpComponentsClientHttpRequestFactory.setConnectionRequestTimeout(3000);

        // 缓冲请求数据，POST大量数据，可以设定为true 我们这边机器比较内存较大
        httpComponentsClientHttpRequestFactory.setBufferRequestBody(true);

//        restTemplate = new RestTemplate();
//        restTemplate.setRequestFactory(httpComponentsClientHttpRequestFactory);
//        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
        return httpComponentsClientHttpRequestFactory;
    }

    private static int getMaxCpuCore(){
        int cpuCore = Runtime.getRuntime().availableProcessors();
        return  cpuCore;
    }

}
