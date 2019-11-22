package com.gcloud.listedcompany.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String company;
    @Column(columnDefinition = "text")
    private String introduce;
    private String website;
    private String address;
    // 分区号，摊位号
    private String fqh;
    private String twh;
    private String email;
    private String position;
    private String area;
    private String number;
    @Column(columnDefinition = "text")
    private String speciality;
    private String education;
    private String salary;
    @Column(columnDefinition = "text")
    private String jobRequire;

    private String uniKey;
}
