package com.libvasf.models;

import com.libvasf.models.Emprestimo;
import com.libvasf.models.Publicacao;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tb_livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "livro_id")
    private Long id;

    @Column(name = "livro_categoria", length = 45, nullable = false)
    private String categoria;

    @Column(name = "livro_titulo", length = 45, nullable = false)
    private String titulo;

    @Column(name = "livro_ISBN", nullable = false)
    private Integer isbn;

    @Column(name = "livro_disponivel", nullable = false)
    private Boolean disponivel;

    @Column(name = "livro_copias", nullable = false)
    private int copias;
    @OneToMany(mappedBy = "livro")
    private List<Emprestimo> emprestimos;

    @OneToMany(mappedBy = "livro")
    private List<Publicacao> publicacoes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getIsbn() {
        return isbn;
    }

    public void setIsbn(Integer isbn) {
        this.isbn = isbn;
    }

    public Boolean getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }

    public List<Emprestimo> getEmprestimos() {
        return emprestimos;
    }

    public void setEmprestimos(List<Emprestimo> emprestimos) {
        this.emprestimos = emprestimos;
    }

    public List<Publicacao> getPublicacoes() {
        return publicacoes;
    }

    public void setPublicacoes(List<Publicacao> publicacoes) {
        this.publicacoes = publicacoes;
    }


    public void setNumeroCopias(int i) {
    }
}
