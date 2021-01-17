package com.sdp.project.model;

import javax.persistence.*;

@Entity
@Table(name = "Items")
public class Items {
//    ItemId int IDENTITY(1,1) PRIMARY KEY,
//    Nome varchar(255) NOT NULL,
//    Descricao varchar(255) NOT NULL
    private long id;
    private String nome;
    private String descricao;
    private int quantidade;

    public Items() {

    }

    public Items(String nome, String descricao, int quantidade) {
        this.nome = nome;
        this.descricao = descricao;
        this.quantidade = quantidade;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "nome", nullable = false)
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Column(name = "descricao", nullable = false)
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Column(name = "quantidade", nullable = false)
    public int getQuantidade() {
        return quantidade;
    }
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return "Employee [id=" + id + ", nome=" + nome + ", descricao=" + descricao + "]";
    }

}