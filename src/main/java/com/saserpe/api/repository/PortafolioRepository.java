package com.saserpe.api.repository;

import com.saserpe.api.model.Portafolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PortafolioRepository extends JpaRepository<Portafolio,Integer> {

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE Portafolio porta " +
            "SET porta.acciones_usr = ?1 " +
            "where porta.RFC_usuario = ?2 " +
            "and porta.RFC_empresa = ?3")
    void updateUsrAcciones(Integer acciones, String RFC_usuario,String RFC_empresa);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE Portafolio porta " +
            "SET porta.acciones_usr = ?1 " +
            "where porta.RFC_empresa = ?2")
    void updatePrecioAccion(float precioA, String RFC_empresa);

    @Query(nativeQuery = true, value = "Select * from Portafolio porta " +
            "where porta.RFC_usuario = ?1 "+
            "and porta.RFC_empresa = ?2 limit 1")
    Portafolio getElemento(String RFCUsuario, String RFCEmpresa);

    @Query(nativeQuery = true, value = "Select * from Portafolio porta " +
            "where porta.RFC_usuario = ?1 limit 1")
    Portafolio getElementByRFCUsuario(String RFCUsuario);
}
