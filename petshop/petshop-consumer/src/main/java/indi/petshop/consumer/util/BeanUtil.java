package indi.petshop.consumer.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2018.11.05
 */
@Component("consumerBeanUtil")
public class BeanUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BeanUtil.applicationContext = applicationContext;
    }

    public static Object getBean(String beanName) {
        return applicationContext != null?applicationContext.getBean(beanName):null;
    }

    public static <T> T getBean(String beanName,Class<T> clazz){
        return applicationContext != null?applicationContext.getBean(beanName,clazz):null;
    }

}
