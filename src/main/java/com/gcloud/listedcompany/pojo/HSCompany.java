package com.gcloud.listedcompany.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "company_hs")
public class HSCompany {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String code;
    private String shorter;
    private String organizationName;
    private String type;
    private String upTime;
    private String upArea;
    private String upState;
    private String downTime;
    private String isin;
    private String usName;
    private String usShorter;
    private String establishTime;
    private String registeredCapital;
    private String represent;
    private String business;

    @Column(columnDefinition = "text")
    private String scope;
    @Column(columnDefinition = "text")
    private String introduce;
    private String province;
    private String city;

    private String registerAddress;
    private String workAddress;
    private String zipCode;
    private String phone;
    private String fax;
    private String email;
    private String website;
    private String zjhFirstIndustry;
    private String zjhSecondIndustry;

    private String swhyFirstIndustry;
    private String swhySecondIndustry;
    private String swhyThirdIndustry;
    private String chairman;
    private String independent;
    private String manager;
    private String secretary;
    private String secretaryPhone;
    private String secretaryFax;
    private String secretaryEmail;
    private String businessRepresent;
    private String accountant;
    private String lawyer;

    // 所有制性质
    private String ownership;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fetchTime;
    private String uniqueKey;
}
