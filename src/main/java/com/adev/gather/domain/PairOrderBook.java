package com.adev.gather.domain;

import com.adev.common.base.domian.OrderBook;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@RedisHash(value = "pair_order_book", timeToLive = -1)
public class PairOrderBook extends OrderBook {
    @Id
    private String id;

    public PairOrderBook(OrderBook orderBook) {
        String id=orderBook.getExchange()+":"+orderBook.getCurrencyPair();
        this.setId(id);
        this.setAsks(orderBook.getAsks());
        this.setBids(orderBook.getBids());
        this.setExchange(orderBook.getExchange());
        this.setCurrencyPair(orderBook.getCurrencyPair());
        this.setTimestamp(orderBook.getTimestamp());
    }
}
