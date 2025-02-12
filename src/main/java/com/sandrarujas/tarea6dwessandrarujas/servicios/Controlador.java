package com.sandrarujas.tarea6dwessandrarujas.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Controlador {
	
    /*Dependencias del paquete servicios*/
	@Autowired
	ServiciosPlanta serviciosPlanta;
	
	@Autowired
	ServiciosEjemplar serviciosEjemplar;
	
	@Autowired
	ServiciosPersona serviciosPersona;
	
	@Autowired
	ServiciosCredencial serviciosCredencial;
	
	@Autowired
	ServiciosMensaje serviciosMensaje;
	
	 private String usuarioConectado;
	 
    /* Método para obtener el usuario conectado*/
    public String obtenerUsuarioConectado() {
        return usuarioConectado;
    }
    
    /*Método para iniciar sesión con un usuario*/
    public void iniciarSesion(String usuario) {
        this.usuarioConectado = usuario;
    }
                   
    /*Método para cerrar sesión */
	public void cerrarSesion() {
		this.usuarioConectado = null;
	}

	/*Métodos para obtener las clases servicios de cada clase en las fachadas*/
	public ServiciosPlanta getServiciosPlanta() {
		return serviciosPlanta;
	}

	public ServiciosEjemplar getServiciosEjemplar() {
		return serviciosEjemplar;
	}
	
	public ServiciosPersona getServiciosPersona() {
		return serviciosPersona;
	}
	
	public ServiciosCredencial getServiciosCredencial() {
		return serviciosCredencial;
	}
	
	public ServiciosMensaje getServiciosMensaje() {
		return serviciosMensaje;
	}

	

}


