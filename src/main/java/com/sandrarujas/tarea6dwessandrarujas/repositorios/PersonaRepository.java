package com.sandrarujas.tarea6dwessandrarujas.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.sandrarujas.tarea6dwessandrarujas.modelo.Persona;

public interface PersonaRepository  extends JpaRepository <Persona, Long>{

   	 boolean existsByEmail(String email);
	 
	 /*Método que nos permite buscar el nombre ignorando las letras mayúsculas o minúsculas*/
	 Persona findByNombreContainingIgnoreCase(String nombre);

}