package com.exelynt.resourcebooking.repository;

import com.exelynt.resourcebooking.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
}