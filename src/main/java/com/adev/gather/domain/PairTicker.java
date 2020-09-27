package com.adev.gather.domain;

import com.adev.common.base.domian.Ticker;
import com.adev.common.base.utils.DataUtils;
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
@RedisHash(value = "pair_ticker", timeToLive = -1)
public class PairTicker extends Ticker {

    @Id
    private String id;

    public PairTicker(Ticker ticker) {
        String id=ticker.getExchange()+":"+ticker.getCurrencyPair();
        this.setId(id);
        DataUtils.copyFatherAttribute(ticker,this);
    }
}
