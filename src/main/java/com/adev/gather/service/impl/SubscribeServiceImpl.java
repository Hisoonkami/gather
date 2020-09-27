package com.adev.gather.service.impl;

import com.adev.common.base.domian.CoinPairBase;
import com.adev.common.base.domian.Kline;
import com.adev.common.base.domian.Ticker;
import com.adev.common.base.domian.Trade;
import com.adev.common.base.enums.Enums;
import com.adev.common.exchange.StreamingExchange;
import com.adev.common.exchange.StreamingExchangeFactory;
import com.adev.gather.config.SubscribeConfig;
import com.adev.gather.domain.CurrencyPair;
import com.adev.gather.domain.PairTicker;
import com.adev.gather.domain.TradeHistory;
import com.adev.gather.repository.CurrencyPairRepository;
import com.adev.gather.repository.TickerRepository;
import com.adev.gather.repository.TradeHistoryRepository;
import com.adev.gather.service.SubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscribeServiceImpl implements SubscribeService {

    @Autowired
    private TickerRepository tickerRepository;

    @Autowired
    private CurrencyPairRepository currencyPairRepository;

    @Autowired
    private TradeHistoryRepository tradeHistoryRepository;

    @Override
    public void subscribe(SubscribeConfig.SubscribeExchange exchange) {
        List<CurrencyPair> currencyPairs=currencyPairRepository.findByExchange(exchange.getName());
        if(null!=currencyPairs&&!currencyPairs.isEmpty()){
            StreamingExchange streamingExchange=getStreamingExchange(exchange);
            if(null!=streamingExchange){
                for (CurrencyPair currencyPair:currencyPairs){
                    streamingExchange.getStreamingMarketDataService().getTicker(currencyPair.getPairName()).subscribe(ticker -> {
                        handleTicker(ticker);
                    });
                    streamingExchange.getStreamingMarketDataService().getTrades(currencyPair.getPairName()).subscribe(trade -> {
                        handleTrade(trade);
                    });
                    streamingExchange.getStreamingMarketDataService().getCurrentKLine(currencyPair.getPairName()).subscribe(kline -> {
                        handleKline(kline);
                    });
                }
            }
        }
    }

    private StreamingExchange getStreamingExchange(SubscribeConfig.SubscribeExchange exchange){
        StreamingExchange streamingExchange=StreamingExchangeFactory.INSTANCE.createExchange(exchange.getClassName());
        if(null!=streamingExchange){
            streamingExchange.connect(null).blockingAwait();
        }
        return streamingExchange;
    }

    private void handleTicker(Ticker ticker){
        PairTicker pairTicker=new PairTicker(ticker);
        String id=pairTicker.getId();
        PairTicker story=tickerRepository.findById(id).orElse(null);
        if(null==story||null==story.getTimestamp()||story.getTimestamp().compareTo(ticker.getTimestamp())<0){//ticker空或者时间没有当前的新则保存
            tickerRepository.save(pairTicker);
        }
    }

    private void handleTrade(Trade trade){
        TradeHistory tradeHistory=tradeHistoryRepository.findFirstByExchangeAndTradeId(trade.getExchange(),trade.getTradeId());
        if(null==tradeHistory){
            tradeHistory=new TradeHistory(trade);
            tradeHistoryRepository.save(tradeHistory);
        }
    }

    private void handleKline(Kline kline){
        System.out.println(kline);
    }
}
