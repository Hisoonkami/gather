package com.adev.gather.service;

import com.adev.gather.config.SubscribeConfig;

public interface SubscribeService {
    void subscribe(SubscribeConfig.SubscribeExchange exchange);
}
