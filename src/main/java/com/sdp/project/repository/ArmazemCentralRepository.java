package com.sdp.project.repository;

import com.sdp.project.model.ArmazemCentral;
import com.sdp.project.model.ItemEntrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArmazemCentralRepository extends JpaRepository<ArmazemCentral, Integer> {
    ItemEntrega findByLista(Integer itemId);
    ArmazemCentral findFirstBy();
}
