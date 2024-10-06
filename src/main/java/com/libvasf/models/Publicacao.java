package com.libvasf.models;

import javax.persistence.*;

@Entity
@Table(name = "rel_publicacao")
public class Publicacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "publicacao_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fk_livro_id", nullable = false)
    private Livro livro;

    @ManyToOne
    @JoinColumn(name = "fk_autor_id", nullable = false)
    private Autor autor;

    @Column(name = "puclicacao_ano", nullable = false)
    private Integer ano;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }
}
