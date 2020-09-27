package com.adev.gather.service.impl;

import com.adev.common.base.service.impl.BaseServiceImpl;
import com.adev.gather.domain.PairTicker;
import com.adev.gather.repository.TickerRepository;
import com.adev.gather.service.TickerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TickerServiceImpl extends BaseServiceImpl<PairTicker, String> implements TickerService {

    @Autowired
    private TickerRepository tickerRepository;

    @Override
    public PairTicker findByExchangeAndPairName(String exchange, String pairName) {
        String id=exchange+":"+pairName;
        return tickerRepository.findById(id).orElse(null);
    }
}
