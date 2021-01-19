package com.sdp.project.model;

import javax.persistence.*;

@Entity
@Table(name = "Item")
public class Item {
    private Integer id;
    private String nome;
    private String descricao;

    public Item() {}

    public Item(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
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

}