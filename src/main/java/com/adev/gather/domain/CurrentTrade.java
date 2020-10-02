package com.adev.gather.domain;

import com.adev.common.base.domian.Kline;
import com.adev.common.base.domian.Trade;
import com.adev.common.base.utils.DataUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;

@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@RedisHash(value = "current_trade", timeToLive = 86400000)
public class CurrentTrade extends Trade {

    @Id
    private String id;

    public CurrentTrade(Trade trade) {
        String id=String.valueOf(trade.getTradeId());
        this.setId(id);
        DataUtils.copyFatherAttribute(trade,this);
    }
}
