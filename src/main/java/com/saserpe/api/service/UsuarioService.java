package com.saserpe.api.service;

import com.saserpe.api.model.Transaccion;
import com.saserpe.api.model.Usuario;
import com.saserpe.api.repository.TransaccionRepository;
import com.saserpe.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository repository;

    public List<Usuario> listAll(){
        return repository.findAll();
    }

    public void save(Usuario usr){
        repository.save(usr);
    }

    public Usuario findById(String RFCUsuario){
        return repository.findById(RFCUsuario).get();
    }

    public void delete(String RFCUsuario){
        repository.deleteById(RFCUsuario);
    }
}
