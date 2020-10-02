package com.adev.gather.domain;

import com.adev.common.base.domian.EntityBase;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "currency")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
public class Currency extends EntityBase {
    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 100)
    private String name;

    @Column(length = 50)
    private String symbol;//缩写

    @Column(length = 100)
    private String issuetime;//发行时间

    @Column(length = 50)
    private String issueprice;//发行价格

    @Column(length = 500)
    private String website;//官网

    @Column(length = 500)
    private String explorer;//区块浏览器

    @Column(length = 500)
    private String whitepaper;//白皮书

    @Column(length = 500)
    private String offcialWallet;//官方钱包

    @Column(length = 100)
    private String algorithm;//核心算法

    @Column(length = 100)
    private String proof_type;//共识机制

    @Column(length = 500)
    private String logo;//图标地址

    @Column
    private BigDecimal total_supply; //总市值
    @Column
    private BigDecimal max_supply;//最大发行量
}
