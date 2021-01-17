package com.sdp.project.model;

import javax.persistence.*;

@Entity
@Table(name = "Entregas")
public class Entregas {

    private long id;
    private long itemId;
    private int quantidadeItem;
    private String localEntrega;

    public Entregas() {

    }

    public Entregas(long ItemId, int QuantidadeItem, String LocalEntrega) {
    /*    EntregaId int PRIMARY KEY,
          ItemId int NOT NULL,
          QuantidadeItem int NOT NULL,
          LocalEntrega varchar(255) NOT NULL*/
        this.itemId = ItemId;
        this.quantidadeItem = QuantidadeItem;
        this.localEntrega = LocalEntrega;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "item_id", nullable = false)
    public long getItemId() {
        return itemId;
    }
    public void setItemId(long ItemId) {
        this.itemId = ItemId;
    }

    @Column(name = "quantidade_item", nullable = false)
    public int getQuantidadeItem() {
        return quantidadeItem;
    }
    public void setQuantidadeItem(int QuantidadeItem) {
        this.quantidadeItem = QuantidadeItem;
    }

    @Column(name = "local_entrega", nullable = false)
    public String getLocalEntrega() {
        return localEntrega;
    }
    public void setLocalEntrega(String LocalEntrega) {
        this.localEntrega = LocalEntrega;
    }

    @Override
    public String toString() {
        return "Entrega [id=" + id + ", ItemID=" + itemId + ", Quantidade=" + quantidadeItem + ", LocalEntrega=" + localEntrega
                + "]";
    }

}