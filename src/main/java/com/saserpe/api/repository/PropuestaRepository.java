package com.saserpe.api.repository;

import com.saserpe.api.model.Propuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PropuestaRepository extends JpaRepository<Propuesta,Integer> {

     @Query(nativeQuery = true, value = "Select * from Propuesta bec " +
            "where bec.RFC_empresa = ?1 " +
            "and bec.RFC_usuario = ?2 " +
            "order by bec.fecha_prop desc limit 1")
    Propuesta findByRFCEmpUsr(String RFCEmpresa, String RFCUsuario);

    @Query(nativeQuery = true, value = "Select * from Propuesta bec " +
            "where bec.id_hilo = ?1 "+
            "order by bec.precio_accion_prop desc limit 1")
     Propuesta getWinnerCompra(String idHilo);

    @Query(nativeQuery = true, value = "Select * from Propuesta bec " +
            "where bec.id_hilo = ?1 "+
            "order by bec.precio_accion_prop asc limit 1")
    Propuesta getWinnerVenta(String idHilo);

    @Query(nativeQuery = true, value = "Select * from Propuesta bec " +
            "where bec.id_hilo = ?1 ")
    List<Propuesta> getPropuestasByHilo(String idHilo);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM Propuesta prop " +
            "where prop.id_hilo = ?1 ")
    void deletePropuesta(String idHilo);
}
