package com.sandrarujas.tarea6dwessandrarujas.servicios;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sandrarujas.tarea6dwessandrarujas.modelo.Mensaje;
import com.sandrarujas.tarea6dwessandrarujas.repositorios.MensajeRepository;

@Service
public class ServiciosMensaje {
	
	@Autowired
	MensajeRepository mensajeRepository;
	
		/*
	     * Método añadir mensaje.
	     * @param mensaje. El mensaje que nos introduce el usuario.
	     */
	 	public void addMensaje(Mensaje mensaje) {
		 mensajeRepository.save(mensaje);
	    }
	 	
	 	/*
	     * Método buscar mensaje por fecha.
	     * @param primeraFecha. segundaFecha. Fechas en las que queremos comprobar si hay mensajes
	     * @return la lista de mensajes según esas fechas.
	     */
	    public ArrayList<Mensaje> buscarMensajePorFecha(LocalDate primeraFecha, LocalDate segundaFecha) {
	        ArrayList<Mensaje> mensajes = mensajeRepository.buscarMensajePorFecha(primeraFecha, segundaFecha);
	        return new ArrayList<>(mensajes);
	    }

	    /*
	     * Método ver lista de mensajes.
	     * @return la lista de mensajes según esas fechas.
	     */
	    public ArrayList<Mensaje> verMensajes() {
	        return (ArrayList<Mensaje>) mensajeRepository.findAll();
	    }

	    /*
	     * Método buscar mensaje por el id de la persona.
	     * @param idPersona. Nos introducen el id de la persona.
	     * @return la lista de mensajes de dicha persona.
	     */
	    public ArrayList<Mensaje> buscarMensajesPorPersona(long idPersona) {
	        ArrayList<Mensaje> mensajes = mensajeRepository.buscarMensajePorID(idPersona);
	        return new ArrayList<>(mensajes);
	    }

	    /*
	     * Método buscar mensaje por el código de la planta
	     * @param codigoPlanta. Nos introducen el código de la planta.
	     * @return la lista de mensajes de dicho código.
	     */
	    public ArrayList<Mensaje> buscarMensajesPorPlanta(String codigoPlanta) {
	        ArrayList<Mensaje> mensajes = mensajeRepository.buscarMensajePorPlanta(codigoPlanta);
	        return new ArrayList<>(mensajes);
	    }

	    /*
	     * Método buscar mensaje por el ejemplar.
	     * @param idPersona. Nos introducen el id del ejemplar
	     * @return la lista de mensajes de los ejemplares con ese id.
	     */
	    public ArrayList<Mensaje> buscarMensajesPorEjemplar(long idEjemplar) {
	        ArrayList<Mensaje> mensajes = mensajeRepository.buscarMensajePorEjemplar(idEjemplar);
	        return new ArrayList<>(mensajes);
	    }

	    /*
	     * Método validar mensaje. 
	     * Comprueba que no esté vacío el mensaje.
	     */
	    public boolean validarMensaje(String mensaje) {
	        if (mensaje == null || mensaje.trim().isEmpty()) {
	            System.out.println("El mensaje está vacio.");
	            return false;
	        }
	   
	        return true;
	    }

	
}

