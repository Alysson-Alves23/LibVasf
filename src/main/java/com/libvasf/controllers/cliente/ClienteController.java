package com.libvasf.controllers.cliente;

import com.libvasf.models.Cliente;
import com.libvasf.services.ClienteService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteController {

    private static final Logger logger = Logger.getLogger(ClienteController.class.getName());
    private static final ClienteService clienteService = new ClienteService();

    public boolean salvarCliente(String nome, String cpf, String email, String telefone) {
        Cliente cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setCpf(cpf);
        cliente.setEmail(email);
        cliente.setTelefone(telefone);

        try {
            clienteService.salvarCliente(cliente);
            logger.info("Cliente salvo com sucesso: " + cliente);
        } catch (Exception e) {

            logger.log(Level.SEVERE, "Erro ao salvar o cliente: " + cliente, e);
            return false;
        }
        return true;
    }

    public void editarCliente(Long id, String nome, String cpf, String email, String telefone, String senha) {
        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNome(nome);
        cliente.setCpf(cpf);
        cliente.setEmail(email);
        cliente.setTelefone(telefone);
        cliente.setSenha(senha);

        try {
            clienteService.editarCliente(cliente);
            logger.info("Cliente editado com sucesso: " + cliente);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao editar o cliente: " + cliente, e);
        }
    }

    public Cliente buscarClientePorId(Long id) {
        try {
            return clienteService.buscarClientePorId(id);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao buscar o cliente por ID: " + id, e);
            return null;
        }
    }

    public List<Cliente> listarClientes() {
        try {
            return clienteService.listarClientes();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao listar os clientes", e);
            return null;
        }
    }

    public void removerCliente(Long id) {
        try {
            clienteService.removerCliente(id);
            logger.info("Cliente removido com sucesso: ID = " + id);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erro ao remover o cliente: ID = " + id, e);
        }
    }
}