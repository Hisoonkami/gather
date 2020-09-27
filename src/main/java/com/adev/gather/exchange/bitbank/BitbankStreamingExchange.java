package com.adev.gather.exchange.bitbank;

import com.adev.common.exchange.ProductSubscription;
import com.adev.common.exchange.StreamingExchange;
import com.adev.common.exchange.StreamingMarketDataService;
import com.adev.common.exchange.http.service.HttpStreamingService;
import io.reactivex.Completable;

public class BitbankStreamingExchange implements StreamingExchange {

    private final HttpStreamingService httpStreamingService;
    private BitbankStreamingMarketDataService streamingMarketDataService;

    public BitbankStreamingExchange() {
        httpStreamingService =new HttpStreamingService("bitbank");
        streamingMarketDataService=new BitbankStreamingMarketDataService(httpStreamingService);
    }

    @Override
    public Completable connect(ProductSubscription... args) {
        return httpStreamingService.connect();
    }

    @Override
    public Completable disconnect() {
        return httpStreamingService.disconnect();
    }

    @Override
    public boolean isAlive() {
        return httpStreamingService.isAlive();
    }

    @Override
    public StreamingMarketDataService getStreamingMarketDataService() {
        return streamingMarketDataService;
    }
}
