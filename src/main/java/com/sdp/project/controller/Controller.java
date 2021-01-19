package com.sdp.project.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.sdp.project.model.ArmazemCentral;
import com.sdp.project.model.Entrega;
import com.sdp.project.model.Item;
import com.sdp.project.model.ItemEntrega;
import com.sdp.project.repository.ArmazemCentralRepository;
import com.sdp.project.repository.EntregaRepository;
import com.sdp.project.repository.ItemRepository;
import com.sdp.project.repository.ItemEntregaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    private EntregaRepository entregasRepository;
    @Autowired
    private ItemRepository itemsRepository;
    @Autowired
    private ItemEntregaRepository itemEntregaRepository;

    @PostMapping("/createItem")
    public Item createItem(@RequestBody Item item) {
        return itemsRepository.save(item);
    }

    @GetMapping("/item")
    public List<Item> getAllItems() {
        return itemsRepository.findAll();
    }

    @PutMapping("/item/{id}")
    public ResponseEntity<Item> updateItems(@PathVariable(value = "id") Integer itemId,
                                                   @RequestBody Item itemDetails) throws ResourceNotFoundException {
        Item item = itemsRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + itemId));

        item.setNome((itemDetails.getNome()));
        item.setDescricao(itemDetails.getDescricao());
        final Item updatedItem = itemsRepository.save(item);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/item/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteItems(@PathVariable(value = "id") Integer itemId)
            throws ResourceNotFoundException {
        Item item = itemsRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found for this id :: " + itemId));
        List<ItemEntrega> itemEntrega = itemEntregaRepository.findByItemId(itemId);
        if(itemEntrega.size() == 0){
            itemsRepository.delete(item);
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return new ResponseEntity(response, HttpStatus.OK);
        }
        else {
            Map<String, Boolean> response = new HashMap<>();
            response.put("item is being used!!", Boolean.TRUE);
            return new ResponseEntity(response, HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/armazem")
    public List<ArmazemCentral> getAllArmazemItems() {
        ArmazemCentral armazemCentral = armazemCentralRepository.findFirstBy();
        if(armazemCentral == null){
            ArmazemCentral newArmazem = new ArmazemCentral();
            armazemCentral = newArmazem;
            armazemCentralRepository.save(newArmazem);
        }
        return armazemCentralRepository.findAll();
    }

    @PostMapping("/depositItem/{id}")
    public ResponseEntity<ItemEntrega> updateItemEntrega(@PathVariable(value = "id") Integer itemId,
                                             @RequestBody int quantidade) throws ResourceNotFoundException{



        Item item = itemsRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Item not found for this id :: " + itemId));
        if(item != null){
            ArmazemCentral armazemCentral = armazemCentralRepository.findFirstBy();
            if(armazemCentral == null){
                ArmazemCentral newArmazem = new ArmazemCentral();
                armazemCentral = newArmazem;
                armazemCentralRepository.save(newArmazem);
            }
            ItemEntrega itemEntrega = armazemCentral.getItemById(itemId);
            if(itemEntrega == null) {
                ItemEntrega newItem = new ItemEntrega(item,quantidade);
                armazemCentral.getLista().add(newItem);
                final ItemEntrega updatedItem = itemEntregaRepository.save(newItem);
                armazemCentralRepository.save(armazemCentral);
                return ResponseEntity.ok(updatedItem);
            } else {
                itemEntrega.setQuantidade(itemEntrega.getQuantidade() + quantidade);
                final ItemEntrega updatedItem =  itemEntregaRepository.save(itemEntrega);;
                return ResponseEntity.ok(updatedItem);
            }
        } else {
            return (ResponseEntity<ItemEntrega>) ResponseEntity.notFound();
        }
    }

    @DeleteMapping("/deleteStoragedItem/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteArmazemItem(@PathVariable(value = "id") Integer idStock)
            throws ResourceNotFoundException {
        ArmazemCentral armazemCentral = armazemCentralRepository.findFirstBy();
        if(armazemCentral == null){
            ArmazemCentral newArmazem = new ArmazemCentral();
            armazemCentral = newArmazem;
            armazemCentralRepository.save(newArmazem);
        }
        ItemEntrega itemEntrega = armazemCentral.getItemEntregaById(idStock);
        if(itemEntrega != null){
            armazemCentral.getLista().remove(itemEntrega);
            armazemCentralRepository.save(armazemCentral);
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return new ResponseEntity(response, HttpStatus.OK);
        }
        Map<String, Boolean> response = new HashMap<>();
        response.put("in use", Boolean.TRUE);
        return new ResponseEntity(response, HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/deleteAllStoragedItem")
    public Map<String, Boolean> deleteAllArmazemItem()
    {
        ArmazemCentral armazemCentral = armazemCentralRepository.findFirstBy();
        if(armazemCentral == null){
            ArmazemCentral newArmazem = new ArmazemCentral();
            armazemCentral = newArmazem;
            armazemCentralRepository.save(newArmazem);
        }
        armazemCentralRepository.deleteAll();
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted all", Boolean.TRUE);
        return response;
    }

    @GetMapping("/delivery")
    public List<Entrega> getAllDelivery() {
        return entregasRepository.findAll();
    }

    @PostMapping("/createDelivery")
    public ResponseEntity<Entrega> createEntrega(@RequestBody CreateDeliveryRequest request) throws  ResourceNotFoundException{
        List<ItemEntrega> lista = new ArrayList<ItemEntrega>();
        for (var item:request.items.entrySet()
             ) {
            lista.add(new ItemEntrega(itemsRepository.findById(item.getKey()).orElseThrow(), item.getValue()));
        }
        Entrega newDelivery = new Entrega(lista, request.local);
        entregasRepository.saveAndFlush(newDelivery);
        return ResponseEntity.ok(newDelivery);
    }

    @PutMapping("/updateDelivery/{id}")
    public ResponseEntity<Entrega> updateDelivery(@PathVariable(value = "id") Integer deliveryId,
                                                        @RequestBody List<ItemEntrega> items) throws ResourceNotFoundException {
        Entrega delivery = entregasRepository.findEntregaById(deliveryId);
        if(delivery != null){
            delivery.setList(items);
            entregasRepository.save(delivery);
            return ResponseEntity.ok(delivery);
        }
        else{
            return (ResponseEntity<Entrega>) ResponseEntity.notFound();
        }
    }

    @DeleteMapping("/deleteDelivery/{id}")
    public Map<String, Boolean> deleteDelivery(@PathVariable(value = "id") Integer deliveryId)
            throws ResourceNotFoundException {
        Entrega delivery = entregasRepository.findById(deliveryId).orElseThrow();
        delivery.getList().removeAll(delivery.getList());
        entregasRepository.delete(delivery);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }

}

class CreateDeliveryRequest{
    public HashMap<Integer, Integer> items;
    public String local;
}