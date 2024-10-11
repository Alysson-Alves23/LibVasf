package com.libvasf.models;

import com.libvasf.models.Emprestimo;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "tb_usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id ;

    @Column(name = "nome", length = 45,nullable = false)
    private String nome;

    @Column(name = "email", length = 45, unique = true,nullable = false)
    private String email;

    @Column(name = "senha", length = 45, nullable = false)
    private String senha;

    @Column(name = "isAdmin", nullable = false)
    private int isAdmin = 0;

    @OneToMany(mappedBy = "usuario")
    private List<Emprestimo> emprestimos;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    public List<Emprestimo> getEmprestimos() {
        return emprestimos;
    }

    public void setEmprestimos(List<Emprestimo> emprestimos) {
        this.emprestimos = emprestimos;
    }
}
