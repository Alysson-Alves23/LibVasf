package com.libvasf.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tb_categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoria_id")
    private Long id;

    @Column(name = "categoria_nome", length = 45, nullable = false)
    private String nome;

    @OneToMany(mappedBy = "categoria")
    private List<LivroCategoria> livroCategorias;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<LivroCategoria> getLivroCategorias() {
        return livroCategorias;
    }

    public void setLivroCategorias(List<LivroCategoria> livroCategorias) {
        this.livroCategorias = livroCategorias;
    }
}
