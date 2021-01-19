package com.sdp.project.model;


import javax.persistence.*;

@Entity
@Table(name = "item_entrega")
public class ItemEntrega {
    private Integer id;
    private Item item;
    private int quantidade;

    public ItemEntrega() {}

    public ItemEntrega(Item item, int quantidade) {
        this.item = item;
        this.quantidade = quantidade;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name="item")
    public Item getItem() { return item; }
    public void setItem(Item item) {
        this.item = item;
    }

    @Column(name = "quantidade", nullable = false)
    public int getQuantidade() {
        return quantidade;
    }
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}