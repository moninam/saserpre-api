package com.saserpe.api.repository;

import com.saserpe.api.model.Acceso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AccesoRepository extends JpaRepository<Acceso,String> {
    @Transactional
    @Query(nativeQuery = true, value = "Select * from Acceso login " +
            "where login.RFC_usuario = ?1 ")
    List<Acceso> getAccesosByRFCUsuario(String RFCUsuario);
}
