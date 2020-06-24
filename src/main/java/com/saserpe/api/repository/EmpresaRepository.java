package com.saserpe.api.repository;

import com.saserpe.api.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa,String> {

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE Empresa empr " +
            "SET empr.precio_accion_empr = ?1 " +
            "where empr.RFC_empresa = ?2")
    void updatePrecioAccion(float precioAccion,String RFCEmpresa);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE Empresa empr " +
            "SET empr.acciones_empr_disp = ?1 " +
            "where empr.RFC_empresa = ?2")
    void updateAccionesDisp(Integer accionDisp,String RFCEmpresa);
}
