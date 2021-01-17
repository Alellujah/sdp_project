package com.sdp.project.repository;

import com.sdp.project.model.Entregas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntregasRepository extends JpaRepository<Entregas, Long> {
    Entregas findByItemId(long itemId);
}
