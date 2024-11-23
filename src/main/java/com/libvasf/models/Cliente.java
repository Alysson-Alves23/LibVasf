package com.libvasf.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tb_cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cliente_id")
    private Long id;

    @Column(name = "cliente_nome", length = 45, nullable = false)
    private String nome;

    @Column(name = "cliente_cpf", length = 45, nullable = false)
    private String cpf;

    @Column(name = "cliente_email", length = 45, nullable = false)
    private String email;
    @Column(name = "cliente_telefone", length = 45, nullable = false)
    private String telefone;
    @Column(name = "cliente_senha", length = 45, nullable = false)
    private String senha;

    @OneToMany(mappedBy = "cliente")
    private List<Emprestimo> emprestimos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Emprestimo> getEmprestimos() {
        return emprestimos;
    }

    public void setEmprestimos(List<Emprestimo> emprestimos) {
        this.emprestimos = emprestimos;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTelefone(String number) {
        this.telefone = number;
    }

    public String getTelefone() {
        return this.telefone;
    }
}
