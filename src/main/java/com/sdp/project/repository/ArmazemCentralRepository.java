package com.sdp.project.repository;

import com.sdp.project.model.ArmazemCentral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArmazemCentralRepository extends JpaRepository<ArmazemCentral, Long> {
    ArmazemCentral findByItemId(long itemId);
}
