package com.gcloud.listedcompany.dao;

import com.gcloud.listedcompany.pojo.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobDao extends JpaRepository<Job, Integer> {
    int countByUniKey(String key);
}
