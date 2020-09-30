package com.adev.gather.exchange.bitbank;

import com.adev.common.base.domian.*;
import com.adev.common.base.utils.DataUtils;
import com.adev.common.base.utils.TimeUtils;
import com.adev.common.exchange.StreamingMarketDataService;
import com.adev.common.exchange.exception.NotYetImplementedForExchangeException;
import com.adev.common.exchange.http.domain.RequestParam;
import com.adev.common.exchange.http.service.HttpStreamingService;
import com.adev.common.exchange.utils.TradeTypeUtil;
import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.reactivex.Observable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BitbankStreamingMarketDataService implements StreamingMarketDataService {

    private final HttpStreamingService httpStreamingService;
    private ObjectMapper mapper = new ObjectMapper();

    public BitbankStreamingMarketDataService(HttpStreamingService httpStreamingService) {
        this.httpStreamingService = httpStreamingService;
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public Observable<Ticker> getTicker(String currencyPair, Object... args) {
        String url="https://public.bitbank.cc/"+currencyPair.replace("-","_")+"/ticker";
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(url);
        return httpStreamingService.pollingRestApi(requestParam).map(s->{
            JsonNode jsonNode = mapper.readTree(s);
            Ticker ticker=new Ticker();
            ticker.setExchange("bitbank");
            ticker.setCurrencyPair(currencyPair);
            String successCode=jsonNode.findValue("success").asText();
            if("1".equalsIgnoreCase(successCode)){
                JsonNode dataNode=jsonNode.get("data");
                if(null!=dataNode){
                    ticker.setLast(DataUtils.objToBigDecimal(dataNode.get("last")));
                    ticker.setLow(DataUtils.objToBigDecimal(dataNode.get("low")));
                    ticker.setHigh(DataUtils.objToBigDecimal(dataNode.get("high")));
                    ticker.setVolume(DataUtils.objToBigDecimal(dataNode.get("vol")));
                    ticker.setTimestamp(DataUtils.objToLong(dataNode.get("timestamp")));
                }
            }
            return ticker;
        });
    }

    @Override
    public Observable<Trade> getTrades(String currencyPair, Object... args) {
        String url="https://public.bitbank.cc/"+currencyPair.replace("-","_")+"/transactions";
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(url);
        return httpStreamingService.pollingRestApi(requestParam).flatMapIterable(s->{
            List<Trade> tradeList=new ArrayList<>();
            JsonNode jsonNode = mapper.readTree(s);
            String successCode=jsonNode.findValue("success").asText();
            if("1".equalsIgnoreCase(successCode)) {
                JsonNode dataNode = jsonNode.get("data");
                if(null!=dataNode){
                    ArrayNode transactionsNode=(ArrayNode)dataNode.get("transactions");
                    if(null!=transactionsNode){
                        for (JsonNode item:transactionsNode){
                            Trade trade=new Trade();
                            trade.setExchange("bitbank");
                            trade.setCurrencyPair(currencyPair);
                            trade.setPrice(DataUtils.objToBigDecimal(item.get("price")));
                            trade.setOriginalAmount(DataUtils.objToBigDecimal(item.get("amount")));
                            trade.setTradeType(TradeTypeUtil.getOrderType(item.get("side").asText().toLowerCase()));
                            trade.setTimestamp(DataUtils.objToLong(item.get("executed_at")));
                            trade.setTradeId(DataUtils.objToLong(item.get("transaction_id")));
                            tradeList.add(trade);
                        }
                    }
                }
            }
            return tradeList;
        });
    }

    @Override
    public Observable<Kline> getCurrentKLine(String currencyPair, Object... args) {
        String dateStr=TimeUtils.getGMTFormat("yyyyMMdd",System.currentTimeMillis());
        String url="https://public.bitbank.cc/"+currencyPair.replace("-","_")+"/candlestick/1min/"+dateStr;
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(url);
        return httpStreamingService.pollingRestApi(requestParam).flatMapIterable(s -> {
            List<Kline> klineList=new ArrayList<>();
            JsonNode jsonNode = mapper.readTree(s);
            String successCode=jsonNode.findValue("success").asText();
            if("1".equalsIgnoreCase(successCode)) {
                JsonNode dataNode=jsonNode.get("data");
                if(null!=dataNode){
                    ArrayNode candlestickArrayNode=(ArrayNode)dataNode.get("candlestick");
                    if(null!=candlestickArrayNode){
                        JsonNode candlestickNode=candlestickArrayNode.get(0);
                        if(null!=candlestickNode){
                            ArrayNode ohlcvArray=(ArrayNode)candlestickNode.get("ohlcv");
                            if(null!=ohlcvArray){
                                for (JsonNode ohlcvItem:ohlcvArray){
                                    ArrayNode ohlcvItemArray=(ArrayNode)ohlcvItem;
                                    Kline kline=new Kline();
                                    kline.setExchange("bitbank");
                                    kline.setCurrencyPair(currencyPair);
                                    kline.setOpen(DataUtils.objToBigDecimal(ohlcvItemArray.get(0)));
                                    kline.setHigh(DataUtils.objToBigDecimal(ohlcvItemArray.get(1)));
                                    kline.setLow(DataUtils.objToBigDecimal(ohlcvItemArray.get(2)));
                                    kline.setLast(DataUtils.objToBigDecimal(ohlcvItemArray.get(3)));
                                    kline.setOriginalAmount(DataUtils.objToBigDecimal(ohlcvItemArray.get(4)));
                                    kline.setTimestamp(DataUtils.objToLong(ohlcvItemArray.get(5)));
                                    klineList.add(kline);
                                }
                            }
                        }
                    }
                }
            }
            return klineList;
        });
    }

    @Override
    public Observable<Kline> getHistoryKLine(String currencyPair, Object... args) {
        throw new NotYetImplementedForExchangeException();
    }

    /**
     * 请求示例 ： https://public.bitbank.cc/eth_btc/depth 返回格式 ： { "success":1, "data":{ "asks":[ [ "0.03151996", "0.4539" ]],"bids":[ [ "0.03151995", "18.4493" ]]}}
     */
    @Override
    public Observable<OrderBook> getOrderBook(String currencyPair, Object... args) {
        String url="https://public.bitbank.cc/"+currencyPair.replace("-","_")+"/depth";
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(url);
        return httpStreamingService.pollingRestApi(requestParam).map(s -> {
            OrderBook orderBook=new OrderBook();
            orderBook.setExchange("bitbank");
            orderBook.setCurrencyPair(currencyPair);
            JsonNode jsonNode = mapper.readTree(s);
            String successCode=jsonNode.findValue("success").asText();
            if("1".equalsIgnoreCase(successCode)) {
                JsonNode dataNode=jsonNode.get("data");
                if(null!=dataNode){
                    orderBook.setTimestamp(DataUtils.objToLong(dataNode.get("timestamp")));
                    ArrayNode asksArray=(ArrayNode)dataNode.get("asks");
                    if(null!=asksArray){
                        List<PriceAndVolume> asks=new ArrayList<>();
                        for (JsonNode askItem:asksArray){
                            ArrayNode askItemArray=(ArrayNode)askItem;
                            PriceAndVolume priceAndVolume=new PriceAndVolume(DataUtils.objToBigDecimal(askItemArray.get(0)),DataUtils.objToBigDecimal(askItemArray.get(1)));
                            asks.add(priceAndVolume);
                        }
                        if(asks.size()>20){
                            asks=asks.subList(0,20);
                        }
                        orderBook.setAsks(asks);
                    }
                    ArrayNode bidsArray=(ArrayNode)dataNode.get("bids");
                    if(null!=bidsArray){
                        List<PriceAndVolume> bids=new ArrayList<>();
                        for (JsonNode bidItem:bidsArray){
                            ArrayNode bidItemArray=(ArrayNode)bidItem;
                            PriceAndVolume priceAndVolume=new PriceAndVolume(DataUtils.objToBigDecimal(bidItemArray.get(0)),DataUtils.objToBigDecimal(bidItemArray.get(1)));
                            bids.add(priceAndVolume);
                        }
                        if(bids.size()>20){
                            bids=bids.subList(0,20);
                        }
                        orderBook.setBids(bids);
                    }
                }
            }
            return orderBook;
        });
}
}
