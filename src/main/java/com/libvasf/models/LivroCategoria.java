package com.libvasf.models;

import javax.persistence.*;

@Entity
@Table(name = "rel_livro_categoria")
public class LivroCategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "livro_categoria_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fk_categoria_id", nullable = false)
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "fk_livro_id", nullable = false)
    private Livro livro;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }
}
