package com.adev.gather.controller;

import com.adev.common.base.domian.BaseResult;
import com.adev.gather.service.CurrencyPairService;
import com.adev.gather.service.TickerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = {"/api/tickers"})
public class TickerController {

    @Autowired
    private TickerService tickerService;

    @RequestMapping(value = {"/search/findByExchangeAndPairName"},method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<BaseResult> findByExchangeAndPairName(@RequestParam(value = "exchange") String exchange,@RequestParam(value = "pairName") String pairName){
        return ResponseEntity.ok(BaseResult.success(tickerService.findByExchangeAndPairName(exchange,pairName)));
    }
}
