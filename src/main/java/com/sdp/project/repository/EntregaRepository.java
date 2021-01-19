package com.sdp.project.repository;

import com.sdp.project.model.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Integer> {
    Entrega findEntregaById(Integer itemId);
}
