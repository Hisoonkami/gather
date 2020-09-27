package com.adev.gather.config;

import com.adev.gather.domain.KlineHistory;
import com.adev.gather.domain.TradeHistory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CollectionAspect {

    @Autowired
    private CollectionNameProvider collectionNameProvider;

    @Pointcut("execution(public * com.adev.gather.repository.*.*(..))")
    public void setCollectionName(){
    }

    @Before("setCollectionName()")
    public void doBefore(JoinPoint joinPoint){
        Object[] args=joinPoint.getArgs();
        String collectionName=null;
        if(null!=args&&args.length>0){
            Object argsOne=args[0];
            if(argsOne instanceof String){
                collectionName=String.valueOf(argsOne);
            }else if(argsOne instanceof TradeHistory){
                TradeHistory tradeHistory=(TradeHistory)argsOne;
                collectionName=tradeHistory.getExchange();
            }else if(argsOne instanceof KlineHistory){
                KlineHistory klineHistory=(KlineHistory)argsOne;
                collectionName=klineHistory.getExchange();
            }
            collectionNameProvider.setCollectionName(collectionName);
        }
    }
}
