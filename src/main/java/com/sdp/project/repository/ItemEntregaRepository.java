package com.sdp.project.repository;

import com.sdp.project.model.Entrega;
import com.sdp.project.model.Item;
import com.sdp.project.model.ItemEntrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemEntregaRepository extends JpaRepository<ItemEntrega, Integer> {
    List<ItemEntrega> findByItemId(Integer itemId);
}
