package com.sandrarujas.tarea6dwessandrarujas.servicios;

import org.springframework.stereotype.Component;

/*La anotación Component es igual que @Service, @Controller..
* Y se utiliza para indicar que la clase sesión la utilizaremos como Componente.
 */
@Component
public class Sesion {

	// Atributo
	private String usuario;
	
	// Constructor
	public Sesion() {
		this.usuario = "";
	}

	// Getter y Setter
	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	/*
     * Método para cerrar sesión del usuario conectado.
     * 
     */
	public void cerrarSesion() {
		this.setUsuario("");
	}

}
