package com.sandrarujas.tarea6dwessandrarujas.repositorios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sandrarujas.tarea6dwessandrarujas.modelo.Planta;

import jakarta.transaction.Transactional;

@Repository
public interface PlantaRepository extends JpaRepository <Planta, Long>{
	
	/*Método que nos permite modificar el nombre común según el codigo que nos proporcionen. El
	 * transactional se utiliza cuando vamos a modificar los datos en la bbdd*/
	@Modifying
	@Transactional
	@Query("UPDATE Planta p SET p.nombreComun = :nombreComun WHERE p.codigo = :codigo")
	int actualizarNombreComun(@Param("codigo") String codigo, @Param("nombreComun") String nombreComun);

	/*Método que nos permite modificar el nombre científico según el codigo que nos proporcionen. El
	 * transactional se utiliza cuando vamos a modificar los datos en la bbdd*/
	@Modifying
	@Transactional
	@Query("UPDATE Planta p SET p.nombreCientifico = :nombreCientifico WHERE p.codigo = :codigo")
	int actualizarNombreCientifico(@Param("codigo") String codigo, @Param("nombreCientifico") String nombreCientifico);

	/*Método que nos permite encontrar planta según el código*/
    Optional<Planta> findByCodigo(String codigo);

	/*Método que nos permite comprobar si existe el código*/
	boolean existsByCodigo(String codigo);
	
	/*Método que nos permite ordenar todas las plantas alfabéticamente*/
	List<Planta> findAllByOrderByNombreComunAsc();

}