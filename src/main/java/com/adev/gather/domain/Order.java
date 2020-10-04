package com.adev.gather.domain;

import com.adev.common.base.domian.EntityBase;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_info")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
public class Order extends EntityBase {
    @Id
    @Column(length = 100)
    private String id;

    @Column(length = 100)
    private String exchange;

    @Column(length = 100)
    private String currencyPair;

    private BigDecimal price;

    private BigDecimal number;

    private BigDecimal amount;
}
