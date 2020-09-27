package com.adev.gather.domain;

import com.adev.common.base.domian.Kline;
import com.adev.common.base.utils.DataUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document(collection="Kline_History_#{@collectionNameProvider.getCollectionName()}")
@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode(exclude = {"id"}, callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class KlineHistory extends Kline {
    @Id
    private ObjectId id;

    public KlineHistory(Kline kline) {
        DataUtils.copyFatherAttribute(kline,this);
    }
}
