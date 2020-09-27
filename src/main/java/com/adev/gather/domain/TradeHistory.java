package com.adev.gather.domain;

import com.adev.common.base.domian.Trade;
import com.adev.common.base.utils.DataUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document(collection="Trade_History_#{@collectionNameProvider.getCollectionName()}")
@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode(exclude = {"id"}, callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TradeHistory extends Trade {
    @Id
    private ObjectId id;

    public TradeHistory(Trade trade) {
        DataUtils.copyFatherAttribute(trade,this);
    }
}
