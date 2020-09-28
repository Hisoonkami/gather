package com.adev.gather.exchange.coinone;

import com.adev.common.base.domian.Kline;
import com.adev.common.base.domian.OrderBook;
import com.adev.common.base.domian.Ticker;
import com.adev.common.base.domian.Trade;
import com.adev.common.base.utils.DataUtils;
import com.adev.common.exchange.StreamingMarketDataService;
import com.adev.common.exchange.exception.NotYetImplementedForExchangeException;
import com.adev.common.exchange.http.domain.RequestParam;
import com.adev.common.exchange.http.service.HttpStreamingService;
import com.adev.common.exchange.utils.TradeTypeUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import io.reactivex.Observable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CoinoneStreamingMarketDataService implements StreamingMarketDataService {

    private final HttpStreamingService httpStreamingService;
    private ObjectMapper mapper = new ObjectMapper();

    public CoinoneStreamingMarketDataService(HttpStreamingService httpStreamingService) {
        this.httpStreamingService = httpStreamingService;
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public Observable<Ticker> getTicker(String currencyPair, Object... args) {
        String url="https://api.coinone.co.kr/ticker/?currency="+currencyPair.split("-")[0];
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(url);
        return httpStreamingService.pollingRestApi(requestParam).map(s->{
            JsonNode jsonNode = mapper.readTree(s);
            Ticker ticker=new Ticker();
            ticker.setExchange("coinone");
            ticker.setCurrencyPair(currencyPair);
            String errorCode=jsonNode.findValue("errorCode").asText();
            if("0".equalsIgnoreCase(errorCode)){
                Long timestamp=jsonNode.findValue("timestamp").asLong();
                if(null!=timestamp){
                    ticker.setTimestamp(timestamp);
                }
                Double first=jsonNode.findValue("first").asDouble();
                if(null!=first){
                    ticker.setOpen(new BigDecimal(first));
                }
                Double low=jsonNode.findValue("low").asDouble();
                if(null!=low){
                    ticker.setLow(new BigDecimal(low));
                }
                Double high=jsonNode.findValue("high").asDouble();
                if(null!=high){
                    ticker.setHigh(new BigDecimal(high));
                }
                Double last=jsonNode.findValue("last").asDouble();
                if(null!=last){
                    BigDecimal lastValue=new BigDecimal(last);
                    ticker.setLast(lastValue);
                    Double yesterday_last=jsonNode.findValue("yesterday_last").asDouble();
                    if(null!=yesterday_last){
                        BigDecimal yesterday_last_value=new BigDecimal(yesterday_last);
                        BigDecimal valueChange=lastValue.subtract(yesterday_last_value);
                        ticker.setValueChange(valueChange);
                        BigDecimal priceChange=valueChange.divide(yesterday_last_value,8,BigDecimal.ROUND_DOWN);
                        ticker.setPriceChange(priceChange);
                    }
                }
                Double volume=jsonNode.findValue("volume").asDouble();
                if(null!=volume){
                    ticker.setVolume(BigDecimal.valueOf(volume));
                    BigDecimal lastValue=ticker.getLast();
                    if(null!=lastValue){
                        ticker.setQuoteVolume(lastValue.multiply(ticker.getVolume()));
                    }
                }
            }
            return ticker;
        });
    }

    /**
     * https://api.coinone.co.kr/trades/?currency=btg&period=hour
     * @param currencyPair Currency pair of the trades
     * @param args
     * @return
     */
    @Override
    public Observable<Trade> getTrades(String currencyPair, Object... args) {
        String url="https://api.coinone.co.kr/trades/?currency="+currencyPair.split("-")[0]+"&period=hour";
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(url);
        return httpStreamingService.pollingRestApi(requestParam).flatMapIterable(s->{
            List<Trade> tradeList=new ArrayList<>();
            JsonNode jsonNode = mapper.readTree(s);
            String errorCode=jsonNode.findValue("errorCode").asText();
            if("0".equalsIgnoreCase(errorCode)) {
                ArrayNode transactionsNode=(ArrayNode)jsonNode.get("completeOrders");
                if(null!=transactionsNode){
                    for (JsonNode item:transactionsNode){
                        Trade trade=new Trade();
                        trade.setExchange("coinone");
                        trade.setCurrencyPair(currencyPair);
                        trade.setPrice(DataUtils.objToBigDecimal(item.get("price")));
                        trade.setOriginalAmount(DataUtils.objToBigDecimal(item.get("qty")));
                        trade.setTradeType(TradeTypeUtil.getOrderType(item.get("is_ask").asText().toLowerCase()));
                        trade.setTimestamp(DataUtils.objToLong(item.get("timestamp")));
                        trade.setTradeId(DataUtils.objToLong(item.get("id")));
                        tradeList.add(trade);
                    }
                }
            }
            return tradeList;
        });
    }

    @Override
    public Observable<Kline> getCurrentKLine(String currencyPair, Object... args) {
        String site=null;
        String[] currencyPairArr = currencyPair.split("-");
        if("btc".equals(currencyPairArr[0]) && "krw".equals(currencyPairArr[1])){
            site="coinone";
        } else if ("krw".equals(currencyPairArr[1])) {
            site="coinone"+currencyPairArr[0];
        } else {
            site="coinone"+currencyPairArr[0]+ currencyPairArr[1];
        }
        String url="https://tb.coinone.co.kr/api/v1/chart/olhc/?site="+site+"&type=1m";
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(url);
        return httpStreamingService.pollingRestApi(requestParam).flatMapIterable(s->{
            List<Kline> klineList=new ArrayList<>();
            JsonNode jsonNode = mapper.readTree(s);
            String success=jsonNode.findValue("success").asText();
            if("true".equalsIgnoreCase(success)) {
                ArrayNode dataArrayNode=(ArrayNode)jsonNode.get("data");
                if(null!=dataArrayNode){
                    for (JsonNode dataNode:dataArrayNode){
                        Kline kline=new Kline();
                        kline.setExchange("coinone");
                        kline.setCurrencyPair(currencyPair);
                        kline.setTimestamp(DataUtils.objToLong(dataNode.get("DT")));
                        kline.setOpen(DataUtils.objToBigDecimal(dataNode.get("Open")));
                        kline.setLow(DataUtils.objToBigDecimal(dataNode.get("Low")));
                        kline.setHigh(DataUtils.objToBigDecimal(dataNode.get("High")));
                        kline.setLast(DataUtils.objToBigDecimal(dataNode.get("Close")));
                        kline.setOriginalAmount(DataUtils.objToBigDecimal(dataNode.get("Volume")));
                        klineList.add(kline);
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

    @Override
    public Observable<OrderBook> getOrderBook(String currencyPair, Object... args) {
        throw new NotYetImplementedForExchangeException();
    }
}
