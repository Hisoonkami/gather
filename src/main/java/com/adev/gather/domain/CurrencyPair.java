package com.adev.gather.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "currency_pair")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
public class CurrencyPair {
    @Version
    @JsonIgnore
    private Long version;

    @Id
    @GeneratedValue
    private Long id;

    private String pairName;

    private String exchange;

    private String currency;

    private String counterName;

    @LastModifiedDate
    private Date lastModifiedDate;

    @LastModifiedBy
    private String lastModifiedBy;

    @CreatedDate
    private Date createdDate;

    @CreatedBy
    private String createdBy;
}
