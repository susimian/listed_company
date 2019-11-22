package com.gcloud.listedcompany.dao;

import com.gcloud.listedcompany.pojo.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyDao extends JpaRepository<Company, Long> {
}
