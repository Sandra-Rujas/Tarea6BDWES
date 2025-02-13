package com.sandrarujas.tarea6dwessandrarujas.controladores;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sandrarujas.tarea6dwessandrarujas.modelo.Ejemplar;
import com.sandrarujas.tarea6dwessandrarujas.modelo.Mensaje;
import com.sandrarujas.tarea6dwessandrarujas.modelo.Persona;
import com.sandrarujas.tarea6dwessandrarujas.modelo.Planta;
import com.sandrarujas.tarea6dwessandrarujas.servicios.Controlador;

import jakarta.servlet.http.HttpSession;

@Controller
public class ViveroPersonalController {

	@Autowired
	Controlador controlador;
	
	
	/**
     * Muestra la página de bienvenida del vivero personal.
     * @param nombre El nombre que se mostrará en la página, por defecto es "Mundo".
     * @param model El objeto Model para pasar datos a la vista.
     * @return El nombre de la vista de bienvenida.
     */
	
    @GetMapping("/ViveroPersonal")
    public String welcome(@RequestParam(name="nombre", required = false, defaultValue = "Mundo") String nombre, Model model) {
        model.addAttribute("nombre", nombre);
        return "ViveroPersonal";
    }

    /**
     * Realiza el cierre de sesión y redirige a la página de inicio.
     * @return La redirección a la página principal.
     */

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/"; 
    }
    
    
    // GESTION EJEMPLARES
    
    /**
     * Muestra el formulario para filtrar ejemplares por código de planta.
     * @param model El objeto Model para pasar datos a la vista.
     * @param session La sesión HTTP para obtener información del usuario.
     * @return El nombre de la vista para filtrar ejemplares.
     */
    
    @GetMapping("/FiltrarEjemplar")
    public String verEjemplaresFormulario(Model model, HttpSession session) {
    	Integer userId = (Integer) session.getAttribute("userId");
    	List<Planta> plantas = controlador.getServiciosPlanta().verPlantas();
    	model.addAttribute("plantas", plantas);
        return "FiltrarEjemplar";  
    }

    
 // GESTION EJEMPLARES
    
    /**
     * Muestra el formulario para crear un nuevo ejemplar.
     * @param session La sesión HTTP para obtener la información del usuario.
     * @param model El objeto Model para pasar datos a la vista.
     * @return El nombre de la vista para crear un ejemplar.
     */
    
    @GetMapping("/CrearEjemplar")
    public String crearEjemplarFormulario(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");

        List<Planta> plantas = controlador.getServiciosPlanta().verPlantas();

        if (plantas == null || plantas.isEmpty()) {
            model.addAttribute("mensaje", "No hay plantas añadidas en la BBDD.");
            return "CrearEjemplar";
        }

        model.addAttribute("plantas", plantas);
        return "CrearEjemplar";  
    }
  
    
    /**
     * Procesa la creación de un nuevo ejemplar.
     * @param codigoPlanta El código de la planta seleccionada.
     * @param model El objeto Model para pasar datos a la vista.
     * @param session La sesión HTTP que contiene la información del usuario autenticado.
     * @return El nombre de la vista para la creación del ejemplar.
     */
    
    @PostMapping("/CrearEjemplar")
    public String crearEjemplar(@RequestParam("codigoPlanta") String codigoPlanta, Model model, HttpSession session) {
        Ejemplar ejemplar = null;
        Mensaje mensaje = null;
        String mensajeTexto = "";
        boolean correcto = false;
        
        String usuarioAutenticado = (String) session.getAttribute("usuarioAutenticado");

        if (usuarioAutenticado == null || usuarioAutenticado.isEmpty()) {
            model.addAttribute("mensaje", "Debes iniciar sesión primero.");
            return "redirect:/login"; 
        }

        if (codigoPlanta == null || codigoPlanta.isEmpty()) {
            model.addAttribute("mensaje", "Debes seleccionar un código de planta.");
            return "CrearEjemplar";
        }

        boolean plantaExiste = controlador.getServiciosPlanta().codigoExistente(codigoPlanta);
        if (!plantaExiste) {
            model.addAttribute("mensaje", "No existe una planta con ese código.");
            return "CrearEjemplar";
        }

        Planta planta = controlador.getServiciosPlanta().buscarPorCodigo(codigoPlanta);
        ejemplar = new Ejemplar();
        ejemplar.setPlanta(planta);
        ejemplar.setNombre(codigoPlanta);

        controlador.getServiciosEjemplar().insertar(ejemplar);
        ejemplar.setNombre(ejemplar.getPlanta().getCodigo() + "_" + ejemplar.getId());
        controlador.getServiciosEjemplar().cambiarNombre(ejemplar.getId(), ejemplar.getNombre());

        mensajeTexto = "Ejemplar creado con ID: " + ejemplar.getId();

        LocalDateTime fechaHora = LocalDateTime.now();
        
        Persona persona = controlador.getServiciosPersona().buscarPorNombre(usuarioAutenticado);

        if (persona != null) {
            mensaje = new Mensaje(fechaHora, mensajeTexto, persona, ejemplar);
            controlador.getServiciosMensaje().addMensaje(mensaje);
            mensajeTexto = "Ejemplar añadido con éxito junto con el mensaje.";
            correcto = true;
        } else {
            mensajeTexto = "No se ha encontrado la persona conectada.";
        }

        model.addAttribute("mensaje", mensajeTexto);
        return "CrearEjemplar";
    }
    
    
    /**
     * Procesa el formulario para filtrar ejemplares de acuerdo al código de la planta.
     * @param codigoPlanta El código de la planta para filtrar los ejemplares.
     * @param model El objeto Model para pasar datos a la vista.
     * @return El nombre de la vista con los ejemplares filtrados.
     */
    
    @PostMapping("/FiltrarEjemplar")
    public String verEjemplares(@RequestParam("codigoPlanta") String codigoPlanta, Model model) {

        boolean existe = controlador.getServiciosPlanta().codigoExistente(codigoPlanta);
        if (!existe) {
            model.addAttribute("mensaje", "No se encontró ninguna planta con el código: " + codigoPlanta);
            return "FiltrarEjemplar";  
        }

        List<Ejemplar> ejemplares = controlador.getServiciosEjemplar().ejemplaresPorTipoPlanta(codigoPlanta);
        if (ejemplares.isEmpty()) {
            model.addAttribute("mensaje", "No hay ejemplares para la planta con código: " + codigoPlanta);
        } else {
            model.addAttribute("ejemplares", ejemplares);  
            model.addAttribute("codigoPlanta", codigoPlanta);  
        }

        return "FiltrarEjemplar";  
    }

    
    /**
     * Muestra la selección de ejemplares para ver los mensajes asociados.
     * @param model El objeto Model para pasar datos a la vista.
     * @return El nombre de la vista para ver los ejemplares.
     */
    
    @GetMapping("/VerMensajesEjemplar")
    public String mostrarSeleccionEjemplar(Model model) {
        List<Ejemplar> ejemplares = (List<Ejemplar>) controlador.getServiciosEjemplar().verEjemplares();
        System.out.println("Cargando ejemplares en la página: " + ejemplares);
        model.addAttribute("ejemplares", ejemplares);
        return "VerMensajesEjemplar"; 
    }
    
    
    /**
     * Muestra los mensajes asociados a un ejemplar seleccionado.
     * @param idEjemplar El ID del ejemplar seleccionado.
     * @param model El objeto Model para pasar datos a la vista.
     * @return El nombre de la vista con los mensajes del ejemplar.
     */
    
    @PostMapping("/VerMensajesEjemplar")
    public String verMensajes(@RequestParam("idEjemplar") Long idEjemplar, Model model) {
        try {
            List<Mensaje> mensajes = controlador.getServiciosMensaje().buscarMensajesPorEjemplar(idEjemplar);
            
            model.addAttribute("mensajes", mensajes.isEmpty() ? null : mensajes);
            model.addAttribute("ejemplares", controlador.getServiciosEjemplar().verEjemplares());
            
        } catch (Exception e) {
            model.addAttribute("mensaje", "Error al buscar los mensajes: " + e.getMessage());
        }
        return "VerMensajesEjemplar";
    }
    
    // GESTION MENSAJES
    
    /**
     * Muestra el menú de gestión de mensajes.
     * @param seccion La sección seleccionada (opcional).
     * @param model El objeto Model para pasar datos a la vista.
     * @return El nombre de la vista para ver los mensajes.
     */
    
    @GetMapping("/VerMensajes")
    public String mostrarMenuMensajes(@RequestParam(value = "seccion", required = false) String seccion, Model model) {
        model.addAttribute("seccion", seccion);
        model.addAttribute("mensajes", controlador.getServiciosMensaje().verMensajes());
        model.addAttribute("personas", controlador.getServiciosPersona().totalPersonas());
        model.addAttribute("plantas", controlador.getServiciosPlanta().verPlantas());
        return "VerMensajes";
    }

    /**
     * Filtra los mensajes por persona.
     * @param idPersona El ID de la persona para filtrar los mensajes.
     * @param model El objeto Model para pasar datos a la vista.
     * @return El nombre de la vista con los mensajes filtrados por persona.
     */
    
    @PostMapping("/MensajesPorPersona")
    public String verMensajesPorPersona(@RequestParam("idPersona") Long idPersona, Model model) {
        model.addAttribute("seccion", "mensajesPorPersona");
        model.addAttribute("mensajesPorPersona", controlador.getServiciosMensaje().buscarMensajesPorPersona(idPersona));
        model.addAttribute("personas", controlador.getServiciosPersona().totalPersonas());
        model.addAttribute("plantas", controlador.getServiciosPlanta().verPlantas());
        return "VerMensajes";
    }

    /**
     * Filtra los mensajes por planta.
     * @param codigoPlanta El código de la planta para filtrar los mensajes.
     * @param model El objeto Model para pasar datos a la vista.
     * @return El nombre de la vista con los mensajes filtrados por planta.
     */
    
    @PostMapping("/MensajesPorPlanta")
    public String verMensajesPorPlanta(@RequestParam("codigoPlanta") String codigoPlanta, Model model) {
        model.addAttribute("seccion", "mensajesPorPlanta");
        model.addAttribute("mensajesPorPlanta", controlador.getServiciosMensaje().buscarMensajesPorPlanta(codigoPlanta));
        model.addAttribute("personas", controlador.getServiciosPersona().totalPersonas());
        model.addAttribute("plantas", controlador.getServiciosPlanta().verPlantas());
        return "VerMensajes";
    }

    
    /**
     * Filtra los mensajes por fecha.
     * @param fechaInicioStr La fecha de inicio para el filtro.
     * @param fechaFinStr La fecha de fin para el filtro.
     * @param model El objeto Model para pasar datos a la vista.
     * @return El nombre de la vista con los mensajes filtrados por fecha.
     */
    
    @PostMapping("/MensajesPorFecha")
    public String verMensajesPorFecha(@RequestParam("fechaInicio") String fechaInicioStr,
                                      @RequestParam("fechaFin") String fechaFinStr, Model model) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fechaInicio = LocalDate.parse(fechaInicioStr, formatter);
        LocalDate fechaFin = LocalDate.parse(fechaFinStr, formatter);

        model.addAttribute("seccion", "mensajesPorFecha");
        model.addAttribute("mensajesPorFecha", controlador.getServiciosMensaje().buscarMensajePorFecha(fechaInicio, fechaFin));
        model.addAttribute("personas", controlador.getServiciosPersona().totalPersonas());
        model.addAttribute("plantas", controlador.getServiciosPlanta().verPlantas());
        
        return "VerMensajes";
    }

}
