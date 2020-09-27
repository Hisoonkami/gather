package com.adev.gather.domain;

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
public class Exchange {
    @Version
    @JsonIgnore
    private Long version;

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String showName;

    private String level;

    private String logo;

    private String website;

    private String setupYear;

    @LastModifiedDate
    private Date lastModifiedDate;

    @LastModifiedBy
    private String lastModifiedBy;
}
