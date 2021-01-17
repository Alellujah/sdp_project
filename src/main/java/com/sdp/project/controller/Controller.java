package com.sdp.project.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.sdp.project.model.ArmazemCentral;
import com.sdp.project.model.Entregas;
import com.sdp.project.model.Items;
import com.sdp.project.repository.ArmazemCentralRepository;
import com.sdp.project.repository.EntregasRepository;
import com.sdp.project.repository.ItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sdp.project.exception.ResourceNotFoundException;

@CrossOrigin(origins = "http://localhost:9000", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class Controller {
    @Autowired
    private ArmazemCentralRepository armazemCentralRepository;
    @Autowired
    private EntregasRepository entregasRepository;
    @Autowired
    private ItemsRepository itemsRepository;

    @PostMapping("/createItem")
    public Items createItem(@RequestBody Items items) {
        return itemsRepository.save(items);
    }

    @GetMapping("/items")
    public List<Items> getAllItems() {
        return itemsRepository.findAll();
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<Items> updateItems(@PathVariable(value = "id") Long itemId,
                                                   @RequestBody Items itemDetails) throws ResourceNotFoundException {
        Items item = itemsRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + itemId));

        item.setNome((itemDetails.getNome()));
        item.setDescricao(itemDetails.getDescricao());
        final Items updatedItem = itemsRepository.save(item);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/items/{id}")
    public Map<String, Boolean> deleteItems(@PathVariable(value = "id") Long itemId)
            throws ResourceNotFoundException {
        Items item = itemsRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found for this id :: " + itemId));
        ArmazemCentral armazemCentral = armazemCentralRepository.findByItemId(itemId);
        Entregas entregas = entregasRepository.findByItemId(itemId);
        if(armazemCentral == null && entregas == null){
            itemsRepository.delete(item);
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return response;
        }
        else {
            Map<String, Boolean> response = new HashMap<>();
            response.put("item is being used!!", Boolean.TRUE);
            return response;
        }
    }

    @GetMapping("/armazem")
    public List<ArmazemCentral> getAllArmazemItems() {
        return armazemCentralRepository.findAll();
    }

    @PostMapping("/depositItem/{id}")
    public ResponseEntity<ArmazemCentral> updateItem(@PathVariable(value = "id") Long itemId,
                                             @RequestBody ArmazemCentral itemDetails) throws ResourceNotFoundException{
        Items item = itemsRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Item not found for this id :: " + itemId));
        if(item != null){
            item.setQuantidade(item.getQuantidade() + itemDetails.getItemStock());
            itemsRepository.save(item);
            ArmazemCentral itemAlreadyExists = armazemCentralRepository.findByItemId(itemId);
            if(itemAlreadyExists == null) {
                ArmazemCentral newItem = new ArmazemCentral(itemId,itemDetails.getItemStock(),item.getNome(),item.getDescricao());
                final ArmazemCentral updatedItem = armazemCentralRepository.save(newItem);
                return ResponseEntity.ok(updatedItem);
            } else {
                itemAlreadyExists.setItemStock(itemAlreadyExists.getItemStock() + itemDetails.getItemStock());
                final ArmazemCentral updatedItem = armazemCentralRepository.save(itemAlreadyExists);
                return ResponseEntity.ok(updatedItem);
            }
        } else {
            return (ResponseEntity<ArmazemCentral>) ResponseEntity.notFound();
        }
    }

    @PutMapping("/updateStoragedItem/{id}")
    public ResponseEntity<ArmazemCentral> updateArmazem(@PathVariable(value = "id") Long itemId,
                                             @RequestBody ArmazemCentral itemDetails) throws ResourceNotFoundException {
        ArmazemCentral item = armazemCentralRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found for this id :: " + itemId));

        item.setItemStock(item.getItemStock() + itemDetails.getItemStock());
        final ArmazemCentral updatedItem = armazemCentralRepository.save(item);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/deleteStoragedItem/{id}")
    public Map<String, Boolean> deleteArmazemItem(@PathVariable(value = "id") Long itemId)
            throws ResourceNotFoundException {
        ArmazemCentral item = armazemCentralRepository.findByItemId(itemId);
        armazemCentralRepository.delete(item);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

    @DeleteMapping("/deleteAllStoragedItem")
    public Map<String, Boolean> deleteAllArmazemItem()
    {
        armazemCentralRepository.deleteAll();
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted all", Boolean.TRUE);
        return response;
    }

    @GetMapping("/delivery")
    public List<Entregas> getAllDelivery() {
        return entregasRepository.findAll();
    }

    @PostMapping("/createDelivery")
    public ResponseEntity<List<Entregas>> createEntrega(@RequestBody List<Entregas> entrega) throws  ResourceNotFoundException{
        List<Entregas> listaDeEntregas = new ArrayList<>();
        entrega.forEach(e -> {
            listaDeEntregas.add(e);
            entregasRepository.save(e);
        });
        return ResponseEntity.ok(listaDeEntregas);
    }

    @PutMapping("/updateDelivery/{id}")
    public ResponseEntity<Entregas> updateDelivery(@PathVariable(value = "id") Long itemId,
                                                        @RequestBody Entregas itemDetails) throws ResourceNotFoundException {

        Entregas item = entregasRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found for this id :: " + itemId));
        if(item != null){
            List<Entregas> listaDeEntregas = entregasRepository.findAll();
            listaDeEntregas.forEach(e -> {
                e.setLocalEntrega(itemDetails.getLocalEntrega());
                entregasRepository.save(e);
            });
            return ResponseEntity.ok(itemDetails);
        }
        else{
            return (ResponseEntity<Entregas>) ResponseEntity.notFound();
        }
    }

    @DeleteMapping("/deleteDelivery/{id}")
    public Map<String, Boolean> deleteDelivery(@PathVariable(value = "id") Long itemId)
            throws ResourceNotFoundException {
        Entregas item = entregasRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Item not found for this id :: " + itemId));
        entregasRepository.delete(item);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

}