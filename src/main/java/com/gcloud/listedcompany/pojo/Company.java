package com.gcloud.listedcompany.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String name;
    private String area;
    private String industry;
    private String website;
    private String business;
    private String chairman;
    private String count;
    private String phone;
    private String fax;
    @Column(columnDefinition = "text")
    private String introduce;
    private String type;
}
