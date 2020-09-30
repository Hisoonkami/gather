package com.adev.gather.controller;

import com.adev.common.base.domian.BaseResult;
import com.adev.gather.service.OrderBookService;
import com.adev.gather.service.TickerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = {"/api/orderBooks"})
public class OrderBookController {

    @Autowired
    private OrderBookService orderBookService;

    @RequestMapping(value = {"/search/findByExchangeAndPairName"},method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<BaseResult> findByExchangeAndPairName(@RequestParam(value = "exchange") String exchange,@RequestParam(value = "pairName") String pairName){
        return ResponseEntity.ok(BaseResult.success(orderBookService.findByExchangeAndPairName(exchange,pairName)));
    }
}
