package com.adev.gather.exchange.kraken;

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
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class KrakenStreamingMarketDataService implements StreamingMarketDataService {

    private final HttpStreamingService httpStreamingService;
    private ObjectMapper mapper = new ObjectMapper();

    public KrakenStreamingMarketDataService(HttpStreamingService httpStreamingService) {
        this.httpStreamingService = httpStreamingService;
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public Observable<Ticker> getTicker(String currencyPair, Object... args) {
        String pair=currencyPair.replace("-","");
        String url="https://api.kraken.com/0/public/Ticker?pair="+pair;
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(url);
        return httpStreamingService.pollingRestApi(requestParam).map(s->{
            JsonNode jsonNode = mapper.readTree(s);
            Ticker ticker=new Ticker();
            ticker.setExchange("kraken");
            ticker.setCurrencyPair(currencyPair);
            JsonNode tickerNode = jsonNode.get("result").get(pair.toUpperCase());
            if(null==tickerNode){
                Iterator<JsonNode> it=jsonNode.get("result").iterator();
                if(null!=it&&it.hasNext()){
                    tickerNode=it.next();
                }
            }

            if(null==tickerNode){
                return ticker;
            }
            BigDecimal high=DataUtils.objToBigDecimal(tickerNode.get("h").get(1));
            ticker.setHigh(high);
            BigDecimal low=DataUtils.objToBigDecimal(tickerNode.get("l").get(1));
            ticker.setLow(low);
            BigDecimal last=DataUtils.objToBigDecimal(tickerNode.get("c").get(1));
            ticker.setLast(last);
            BigDecimal volume=DataUtils.objToBigDecimal(tickerNode.get("v").get(1));
            ticker.setVolume(volume);
            BigDecimal open=DataUtils.objToBigDecimal(tickerNode.get("o"));
            ticker.setOpen(open);
            ticker.setTimestamp(System.currentTimeMillis());
            return ticker;
        });
    }

    @Override
    public Observable<Trade> getTrades(String currencyPair, Object... args) {
        String pair=currencyPair.replace("-","");
        String url="https://api.kraken.com/0/public/Trades?pair="+pair;
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(url);
        return httpStreamingService.pollingRestApi(requestParam).flatMapIterable(s -> {
            List<Trade> tradeList=new ArrayList<>();
            JsonNode jsonNode = mapper.readTree(s);
            ArrayNode tradesNode = (ArrayNode)jsonNode.get("result").get(pair.toUpperCase());
            if(null!=tradesNode){
                for (JsonNode tradeNode:tradesNode){
                    ArrayNode tradeNodeArray=(ArrayNode)tradeNode;
                    Trade trade=new Trade();
                    trade.setExchange("kraken");
                    trade.setCurrencyPair(currencyPair);
                    trade.setPrice(DataUtils.objToBigDecimal(tradeNodeArray.get(0)));
                    trade.setOriginalAmount(DataUtils.objToBigDecimal(tradeNodeArray.get(1)));
                    BigDecimal d=BigDecimal.valueOf(tradeNodeArray.get(2).asDouble());
                    String timestampStr=d.toPlainString();
                    if(StringUtils.isNotBlank(timestampStr)){
                        String[] timestampArr=timestampStr.split("\\.");
                        trade.setTimestamp(new Long(timestampArr[0]+"000"));
                        trade.setTradeId(new Long(timestampArr[0]+timestampArr[1]));
                    }
                    trade.setTradeType(TradeTypeUtil.getOrderType(tradeNodeArray.get(3).asText().toLowerCase()));
                    tradeList.add(trade);
                }
            }
            return tradeList;
        });
    }

    @Override
    public Observable<Kline> getCurrentKLine(String currencyPair, Object... args) {
        String pair=currencyPair.replace("-","");
        long timestamp=System.currentTimeMillis();
        timestamp=timestamp-15*60*60*1000L;
        String url="https://api.kraken.com/0/public/OHLC?pair="+pair+"&interval=1&since="+timestamp;
        RequestParam requestParam=new RequestParam();
        requestParam.setUrl(url);
        return httpStreamingService.pollingRestApi(requestParam).flatMapIterable(s -> {
            List<Kline> klineList = new ArrayList<>();
            JsonNode jsonNode = mapper.readTree(s);
            ArrayNode klineListNode = (ArrayNode) jsonNode.get("result").get(pair.toUpperCase());
            if(null!=klineListNode) {
                for (JsonNode klineNode : klineListNode) {
                    Kline kline=new Kline();
                    ArrayNode klineNodeArray=(ArrayNode)klineNode;
                    kline.setTimestamp(DataUtils.objToLong(klineNodeArray.get(0)));
                    kline.setOpen(DataUtils.objToBigDecimal(klineNodeArray.get(1)));
                    kline.setHigh(DataUtils.objToBigDecimal(klineNodeArray.get(2)));
                    kline.setLow(DataUtils.objToBigDecimal(klineNodeArray.get(3)));
                    kline.setLast(DataUtils.objToBigDecimal(klineNodeArray.get(4)));
                    kline.setOriginalAmount(DataUtils.objToBigDecimal(klineNodeArray.get(5)));
                    klineList.add(kline);
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
        return null;
    }
}
