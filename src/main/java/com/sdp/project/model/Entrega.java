package com.sdp.project.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Entrega")
public class Entrega {

    private int id;
    private List<ItemEntrega> lista;
    private String localEntrega;

    public Entrega() {}

    public Entrega(List<ItemEntrega> lista, String LocalEntrega) {
        this.lista = lista;
        this.localEntrega = LocalEntrega;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "lista")
    public List<ItemEntrega> getList() {
        return lista;
    }
    public void setList(List<ItemEntrega> lista) {
        this.lista = lista;
    }

    @Column(name = "local_entrega", nullable = false)
    public String getLocalEntrega() {
        return localEntrega;
    }
    public void setLocalEntrega(String LocalEntrega) {
        this.localEntrega = LocalEntrega;
    }
}