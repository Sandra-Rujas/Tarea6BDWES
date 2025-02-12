package com.sandrarujas.tarea6dwessandrarujas.repositorios;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.sandrarujas.tarea6dwessandrarujas.modelo.Mensaje;

@Repository
public interface MensajeRepository extends JpaRepository <Mensaje, Long>{
	
	/*Método que nos permite a través de una consulta buscar un mensaje según dos rangos de fecha*/
    @Query("SELECT m FROM Mensaje m WHERE DATE(m.fechaHora) BETWEEN :primeraFecha AND :segundaFecha")
    ArrayList<Mensaje> buscarMensajePorFecha(@Param("primeraFecha") LocalDate primeraFecha,@Param("segundaFecha") LocalDate segundaFecha);

    /*Método que nos permite a través de una consulta obtener los mensajes según el id de la persona*/
    @Query("SELECT m FROM Mensaje m WHERE m.persona.id = :idPersona")
    ArrayList<Mensaje>buscarMensajePorID(@Param("idPersona")long idPersona);

    /*Método que nos permite a través de una consulta buscar los mensajes en referencia a un código de la planta*/
    @Query("SELECT m FROM Mensaje m INNER JOIN m.ejemplar e INNER JOIN e.planta p WHERE p.codigo = :codigoPlanta")
    ArrayList<Mensaje>buscarMensajePorPlanta(@Param("codigoPlanta") String codigoPlanta);
    
    /*Método que nos permite a través de una consulta buscar un mensaje según el ejemplar de la planta que elijamos*/
    @Query("SELECT m FROM Mensaje m WHERE m.ejemplar.id = :idEjemplar")
    ArrayList<Mensaje> buscarMensajePorEjemplar(@Param("idEjemplar")long idEjemplar);

}