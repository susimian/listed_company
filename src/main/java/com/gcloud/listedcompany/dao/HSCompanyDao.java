package com.gcloud.listedcompany.dao;

import com.gcloud.listedcompany.pojo.HSCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HSCompanyDao extends JpaRepository<HSCompany, Integer> {
    int countByUniqueKey(String uniqueKey);
}
