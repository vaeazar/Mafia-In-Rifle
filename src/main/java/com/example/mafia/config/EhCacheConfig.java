//package com.example.mafia.config;
//
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cache.ehcache.EhCacheCacheManager;
//import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
//import org.springframework.context.annotation.AdviceMode;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//
//@Configuration
//@EnableCaching(proxyTargetClass = true, mode = AdviceMode.PROXY)
//public class EhCacheConfig {
//    @Bean
//    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
//        EhCacheManagerFactoryBean factoryBean = new EhCacheManagerFactoryBean();
//        factoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
//        factoryBean.setShared(true);
//        return factoryBean;
//    }
//
//    @Bean
//    public EhCacheCacheManager ehCacheCacheManager(EhCacheManagerFactoryBean factoryBean) {
//        EhCacheCacheManager cacheManager = new EhCacheCacheManager();
//        cacheManager.setCacheManager(factoryBean.getObject());
//        return cacheManager;
//    }
//}
