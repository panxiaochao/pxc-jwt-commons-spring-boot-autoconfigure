package com.github.panxiaochao.config;

import com.github.panxiaochao.properties.PxcJwtProperties;
import com.github.panxiaochao.service.PxcJwtHandlerService;
import com.github.panxiaochao.service.impl.PxcJwtHandlerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Lypxc
 * @description: 自动配置文件
 * @date 2021/12/17 13:31
 */
@Configuration
@ConditionalOnClass(PxcJwtHandlerService.class)
@EnableConfigurationProperties(PxcJwtProperties.class)
@ConditionalOnProperty(prefix = PxcJwtProperties.PXCJWT_PREFIX, name = "enabled", havingValue = "true")
public class PxcJwtAutoConfiguration {
    //private static final Logger logger = LoggerFactory.getLogger(PxcJwtAutoConfiguration.class);

    @Autowired
    private PxcJwtProperties pxcJwtProperties;

    /**
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public PxcJwtHandlerService pxcJwtHandlerService() {
        //logger.info("初始化pxcJwtHandlerService...");
        return new PxcJwtHandlerServiceImpl(pxcJwtProperties);
    }
}
