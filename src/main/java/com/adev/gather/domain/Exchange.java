package com.adev.gather.domain;

import com.adev.common.base.domian.EntityBase;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "exchange")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
public class Exchange extends EntityBase {
    @Id
    @GeneratedValue
    private Long id;

    private String name; //交易所名称，传参使用

    private String showName;//交易所显示名称

    private String level;//交易所级别

    private String logo;//图标

    private String website;//网站

    private String setupYear;//成立年份
}
