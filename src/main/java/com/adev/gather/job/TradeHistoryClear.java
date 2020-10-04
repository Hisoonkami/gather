package com.adev.gather.job;

import com.adev.gather.repository.TradeHistoryRepository;
import com.adev.gather.service.ExchangeService;
import com.adev.gather.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TradeHistoryClear {

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private TradeHistoryRepository tradeHistoryRepository;

    @Autowired
    private RedisUtil redisUtil;
    @Scheduled(cron="30 0 0/1 * * ? ")
    public void tradeHistoryDelete(){
        exchangeService.findAll().forEach(exchange->{
            boolean lock=redisUtil.lock(exchange.getName(),exchange.getName(),5*1000L);
            Long timestamp=System.currentTimeMillis()-2*24*60*60*1000L;
            if(lock){
                tradeHistoryRepository.deleteByTimestampBefore(exchange.getName(),timestamp);
                redisUtil.unlock(exchange.getName(),exchange.getName());
            }

        });
    }
}
