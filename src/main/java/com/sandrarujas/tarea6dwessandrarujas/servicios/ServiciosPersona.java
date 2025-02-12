package com.sandrarujas.tarea6dwessandrarujas.servicios;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sandrarujas.tarea6dwessandrarujas.modelo.Persona;
import com.sandrarujas.tarea6dwessandrarujas.repositorios.PersonaRepository;

@Service
public class ServiciosPersona {
	
	@Autowired
	private PersonaRepository personaRepository;

		/*Método insertar persona.
		 * @param persona. La persona que queremos añadir.
		 */
	    public void insertar(Persona persona) {
	    	personaRepository.saveAndFlush(persona);
	    }

	    /*Método que nos permite ver todas las personas.
	     * @return todas las personas.*/
	    public Collection<Persona> totalPersonas() {
	        return personaRepository.findAll();
	    }

	    /*Método que nos permite comprobar si el email existe.
	     * @param email. El email de la persona.
	     * @return true si existe el email, false si no.
	     */
	    public boolean emailExistente(String email) {
	        return personaRepository.existsByEmail(email);
	    }
	    

	    /*Método buscar persona por nombre.
	     * @param nombre. El nombre de la persona que queremos encontrar.
	     * @return el nombre ignorando las mayúsculas y minúsculas.
	     */
	    public Persona buscarPorNombre(String nombre){
	    	return personaRepository.findByNombreContainingIgnoreCase(nombre);
	    }
	    
	    /*Método validar persona.
	     * @param persona.La persona que queremos validar.
	     * @return false si la persona es null. Si el nombre está vacío.
	     * Si el email no cumple con los requisitos especificados.
	     */
	    public boolean validarPersona(Persona persona) {
	        if (persona == null) {
	            return false;
	        }
	        if (persona.getNombre() == null || persona.getNombre().isEmpty()) {
	            return false;
	        }
	        if (persona.getEmail() == null || persona.getEmail().isEmpty()) {
	            return false;
	        }
	        if (persona.getEmail().length() < 5 || !persona.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$") || persona.getEmail().length() > 40) {
	            return false;
	        }
	        return true;
	    }



}
