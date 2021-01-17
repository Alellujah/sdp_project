package com.sdp.project.model;

import javax.persistence.*;

@Entity
@Table(name = "ArmazemCentral")
public class ArmazemCentral {
//    ItemId int PRIMARY KEY,
//    ItemStock int NOT NULL
    private long id;
    private long ItemId;
    private int ItemStock;
    private String ItemName;
    private String ItemDesc;

    public ArmazemCentral() {

    }

    public ArmazemCentral(long ItemId, int ItemStock, String ItemName, String ItemDesc) {
        this.ItemStock = ItemStock;
        this.ItemId = ItemId;
        this.ItemName = ItemName;
        this.ItemDesc = ItemDesc;
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
        return ItemId;
    }
    public void setItemId(long ItemId) {
        this.ItemId = ItemId;
    }

    @Column(name = "item_stock", nullable = false)
    public int getItemStock() {
        return ItemStock;
    }
    public void setItemStock(int ItemStock) {
        this.ItemStock = ItemStock;
    }

    @Column(name = "item_name", nullable = false)
    public String getItemName() {
        return ItemName;
    }
    public void setItemName(String ItemName) {
        this.ItemName = ItemName;
    }

    @Column(name = "item_description", nullable = false)
    public String getItemDesc() {
        return ItemDesc;
    }
    public void setItemDesc(String ItemDesc) {
        this.ItemDesc = ItemDesc;
    }

    @Override
    public String toString() {
        return "ArmazemCentral [id=" + id + ", ItemId="+ ItemId + ", ItemStock=" + ItemStock + "]";
    }

}