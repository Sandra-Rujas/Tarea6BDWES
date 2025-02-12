package com.sandrarujas.tarea6dwessandrarujas.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sandrarujas.tarea6dwessandrarujas.modelo.Ejemplar;

import jakarta.transaction.Transactional;


@Repository
public interface EjemplarRepository extends JpaRepository <Ejemplar, Long>{
	
	/*Método que nos permite a través de una consulta mostrar los ejemplares que hay en la base de datos con dicho código*/
	 @Query("SELECT e FROM Ejemplar e WHERE e.planta.codigo = :codigoPlanta")
	 List<Ejemplar> ejemplaresPorPlanta(@Param("codigoPlanta") String codigoPlanta);

	 /*Método que a través de una consulta nos permite modificar el nombre de la planta según el Id. 
	  * El @ Transactional se utiliza para todas aquellas consultas que nos permiten modificar un dato*/
	@Transactional
    @Modifying
    @Query("UPDATE Ejemplar e SET e.nombre = :nuevoNombre WHERE e.id = :idEjemplar")
    int editarNombre(@Param("idEjemplar") Long idEjemplar, @Param("nuevoNombre") String nuevoNombre);

}

