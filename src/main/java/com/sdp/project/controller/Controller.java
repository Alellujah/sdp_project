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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sdp.project.exception.ResourceNotFoundException;
import com.sdp.project.model.Employee;
import com.sdp.project.repository.EmployeeRepository;

@RestController
@RequestMapping("/api/v1")
public class Controller {
    @Autowired
    private EmployeeRepository employeeRepository;
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
            item.setQuantidade(itemDetails.getItemStock());
            itemsRepository.save(item);
            ArmazemCentral newItem = new ArmazemCentral(itemId,itemDetails.getItemStock(),item.getNome(),item.getDescricao());
            final ArmazemCentral updatedItem = armazemCentralRepository.save(newItem);
            return ResponseEntity.ok(updatedItem);
        } else {
            return (ResponseEntity<ArmazemCentral>) ResponseEntity.notFound();
        }
    }

    @PutMapping("/updateStoragedItem/{id}")
    public ResponseEntity<ArmazemCentral> updateArmazem(@PathVariable(value = "id") Long itemId,
                                             @RequestBody ArmazemCentral itemDetails) throws ResourceNotFoundException {
        ArmazemCentral item = armazemCentralRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found for this id :: " + itemId));

        item.setItemStock(itemDetails.getItemStock());
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
    public ResponseEntity<List<Entregas>> updateDelivery(@PathVariable(value = "id") Long itemId,
                                                        @RequestBody Entregas itemDetails) throws ResourceNotFoundException {

        Entregas item = entregasRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found for this id :: " + itemId));
        if(item != null){
            List<Entregas> listaDeEntregas = entregasRepository.findAll();
            listaDeEntregas.forEach(e -> {
                e.setLocalEntrega(itemDetails.getLocalEntrega());
                entregasRepository.save(e);
            });
            return ResponseEntity.ok(listaDeEntregas);
        }
        else{
            return (ResponseEntity<List<Entregas>>) ResponseEntity.notFound();
        }
    }

    // Dummy stuff -------------
    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") Long employeeId)
            throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));
        return ResponseEntity.ok().body(employee);
    }

    @PostMapping("/employees")
    public Employee createEmployee( @RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable(value = "id") Long employeeId,
                                                    @RequestBody Employee employeeDetails) throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

        employee.setEmailId(employeeDetails.getEmailId());
        employee.setLastName(employeeDetails.getLastName());
        employee.setFirstName(employeeDetails.getFirstName());
        final Employee updatedEmployee = employeeRepository.save(employee);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/employees/{id}")
    public Map<String, Boolean> deleteEmployee(@PathVariable(value = "id") Long employeeId)
            throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

        employeeRepository.delete(employee);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}