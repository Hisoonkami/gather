package com.adev.gather.domain;

import com.adev.common.base.domian.Kline;
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
@RedisHash(value = "current_kline", timeToLive = 86400000)
public class CurrentKline extends Kline {

    @Id
    private String id;

    public CurrentKline(Kline kline) {
        String id=kline.getExchange()+":"+kline.getCurrencyPair()+":"+kline.getTimestamp();
        this.setId(id);
        DataUtils.copyFatherAttribute(kline,this);
    }
}
