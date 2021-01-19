package com.sdp.project.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ArmazemCentral")
public class ArmazemCentral {
    private int id;
    private List<ItemEntrega> lista;

    public ArmazemCentral() {
    }
    public ArmazemCentral(List<ItemEntrega> lista) {
        this.lista = lista;
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
    @JoinColumn(name = "items")
    public List<ItemEntrega> getLista() {
        return lista;
    }
    public void setLista(List<ItemEntrega> lista) {
        this.lista = lista;
    }

    public ItemEntrega getItemById(long id) {
        for (var item: lista
             ) {
            if (item.getItem().getId() == id ){
                return item;
            }
        }
        return null;
    }
}