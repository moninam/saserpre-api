package com.saserpe.api.repository;

import com.saserpe.api.model.Hilos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface HilosRepository extends JpaRepository<Hilos,Integer> {
    @Transactional
    @Query(nativeQuery = true, value = "Select * from Hilos hilo " +
            "where hilo.activo = true "+
            "order by hilo.tipo_accion desc ,hilo.actualizacion desc ")
    List<Hilos> getHilosDesc();

    @Transactional
    @Query(nativeQuery = true, value = "Select * from Hilos hilo " +
            "order by hilo.actualizacion desc limit 1 ")
    Hilos getActualHilo();

    @Query(nativeQuery = true, value = "Select * from Hilos hilo " +
            "where hilo.id_hilo = ?1 ")
    Hilos getHiloByIdHilo(String idHilo);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE Hilos hilo " +
            "SET hilo.actualizacion = ?1 " +
            "where hilo.id_hilo = ?2")
    void updateHoraHilo(String actualizacion,String idHilo);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE Hilos hilo " +
            "SET hilo.activo = false " +
            "where hilo.id_hilo = ?1")
    void disableHilo(String idHilo);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE Hilos hilo " +
            "SET hilo.activo = true " +
            "where hilo.id_hilo = ?1")
    void enableHilo(String idHilo);
}
