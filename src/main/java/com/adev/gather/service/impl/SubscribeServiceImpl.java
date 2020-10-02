package com.adev.gather.service.impl;

import com.adev.common.base.domian.Kline;
import com.adev.common.base.domian.OrderBook;
import com.adev.common.base.domian.Ticker;
import com.adev.common.base.domian.Trade;
import com.adev.common.exchange.StreamingExchange;
import com.adev.common.exchange.StreamingExchangeFactory;
import com.adev.gather.config.SubscribeConfig;
import com.adev.gather.domain.*;
import com.adev.gather.repository.*;
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

    @Autowired
    private KlineHistoryRepository klineHistoryRepository;

    @Autowired
    private OrderBookRepository orderBookRepository;

    @Autowired
    private CurrentTradeRepository currentTradeRepository;

    @Autowired
    private CurrentKlineRepository currentKlineRepository;

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
                    streamingExchange.getStreamingMarketDataService().getOrderBook(currencyPair.getPairName()).subscribe(orderBook -> {
                        handleOrderBook(orderBook);
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
        if(null==tradeHistory){//保存历史交易记录
            tradeHistory=new TradeHistory(trade);
            tradeHistoryRepository.save(tradeHistory);
        }
        CurrentTrade currentTrade=new CurrentTrade(trade);
        if(!currentTradeRepository.existsById(currentTrade.getId())){//保存最近的交易记录
            currentTradeRepository.save(currentTrade);
        }
    }

    private void handleKline(Kline kline){
        KlineHistory klineHistory=klineHistoryRepository.findFirstByExchangeAndCurrencyPairAndTimestamp(kline.getExchange(),kline.getCurrencyPair(),kline.getTimestamp());
        if(null==klineHistory){//保存历史k线
            klineHistory=new KlineHistory(kline);
            klineHistoryRepository.save(klineHistory);
        }
        CurrentKline currentKline=new CurrentKline(kline);
        if(!currentKlineRepository.existsById(currentKline.getId())){//保存最新k线
            currentKlineRepository.save(currentKline);
        }
    }

    private void handleOrderBook(OrderBook orderBook){
        String id=orderBook.getExchange()+":"+orderBook.getCurrencyPair();
        PairOrderBook pairOrderBook=orderBookRepository.findById(id).orElse(null);
        if(null==pairOrderBook||pairOrderBook.getTimestamp().compareTo(orderBook.getTimestamp())<0){//不存在或者比当前数据更老就更新
            pairOrderBook=new PairOrderBook(orderBook);
            orderBookRepository.save(pairOrderBook);
        }
    }
}
