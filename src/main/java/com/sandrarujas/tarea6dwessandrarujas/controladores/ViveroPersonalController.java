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
	
    @GetMapping("/ViveroPersonal")
    public String welcome(@RequestParam(name="nombre", required = false, defaultValue = "Mundo") String nombre, Model model) {
        model.addAttribute("nombre", nombre);
        return "ViveroPersonal";
    }


    @GetMapping("/logout")
    public String logout() {
        return "redirect:/"; 
    }
    
    
    // GESTION EJEMPLARES
    

    // Mostrar formulario para filtrar ejemplares
    @GetMapping("/FiltrarEjemplar")
    public String verEjemplaresFormulario(Model model, HttpSession session) {
    	Integer userId = (Integer) session.getAttribute("userId");
    	List<Planta> plantas = controlador.getServiciosPlanta().verPlantas();
    	model.addAttribute("plantas", plantas);
        return "FiltrarEjemplar";  // Página para filtrar ejemplares
    }

  
    
 // GESTION EJEMPLARES
    
    @GetMapping("/CrearEjemplar")
    public String crearEjemplarFormulario(HttpSession session, Model model) {
        // Obtener el userId desde la sesión
        Integer userId = (Integer) session.getAttribute("userId");

        // Obtener la lista de plantas (si no se redirige)
        List<Planta> plantas = controlador.getServiciosPlanta().verPlantas();

        // Si no hay plantas, mostrar un mensaje
        if (plantas == null || plantas.isEmpty()) {
            model.addAttribute("mensaje", "No hay plantas añadidas en la BBDD.");
            return "CrearEjemplar";
        }

        // Añadir la lista de plantas al modelo
        model.addAttribute("plantas", plantas);
        return "CrearEjemplar";  // Página con el formulario
    }
  
    
    @PostMapping("/CrearEjemplar")
    public String crearEjemplar(@RequestParam("codigoPlanta") String codigoPlanta, Model model, HttpSession session) {
        Ejemplar ejemplar = null;
        Mensaje mensaje = null;
        String mensajeTexto = "";
        boolean correcto = false;
        
        // Recuperar el usuario autenticado desde la sesión
        String usuarioAutenticado = (String) session.getAttribute("usuarioAutenticado");

        // Verificar que se ha iniciado sesión
        if (usuarioAutenticado == null || usuarioAutenticado.isEmpty()) {
            model.addAttribute("mensaje", "Debes iniciar sesión primero.");
            return "redirect:/login"; // Redirigir al login si no está autenticado
        }

        // Verificar que se ha seleccionado un código de planta
        if (codigoPlanta == null || codigoPlanta.isEmpty()) {
            model.addAttribute("mensaje", "Debes seleccionar un código de planta.");
            return "CrearEjemplar";
        }

        // Verificar si el código de planta existe
        boolean plantaExiste = controlador.getServiciosPlanta().codigoExistente(codigoPlanta);
        if (!plantaExiste) {
            model.addAttribute("mensaje", "No existe una planta con ese código.");
            return "CrearEjemplar";
        }

        // Buscar la planta por su código
        Planta planta = controlador.getServiciosPlanta().buscarPorCodigo(codigoPlanta);
        ejemplar = new Ejemplar();
        ejemplar.setPlanta(planta);
        ejemplar.setNombre(codigoPlanta);

        // Insertar el ejemplar en la base de datos
        controlador.getServiciosEjemplar().insertar(ejemplar);
        ejemplar.setNombre(ejemplar.getPlanta().getCodigo() + "_" + ejemplar.getId());
        controlador.getServiciosEjemplar().cambiarNombre(ejemplar.getId(), ejemplar.getNombre());

        mensajeTexto = "Ejemplar creado con ID: " + ejemplar.getId();

        // Crear un mensaje asociado al ejemplar
        LocalDateTime fechaHora = LocalDateTime.now();
        
        // Buscar la persona autenticada por el nombre de usuario
        Persona persona = controlador.getServiciosPersona().buscarPorNombre(usuarioAutenticado);

        if (persona != null) {
            // Crear el mensaje y asociarlo con la persona y el ejemplar
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
    @PostMapping("/FiltrarEjemplar")
    public String verEjemplares(@RequestParam("codigoPlanta") String codigoPlanta, Model model) {

        // Comprobar si la planta existe
        boolean existe = controlador.getServiciosPlanta().codigoExistente(codigoPlanta);
        if (!existe) {
            model.addAttribute("mensaje", "No se encontró ninguna planta con el código: " + codigoPlanta);
            return "FiltrarEjemplar";  
        }

        // Obtener los ejemplares para esa planta
        List<Ejemplar> ejemplares = controlador.getServiciosEjemplar().ejemplaresPorTipoPlanta(codigoPlanta);
        if (ejemplares.isEmpty()) {
            model.addAttribute("mensaje", "No hay ejemplares para la planta con código: " + codigoPlanta);
        } else {
            model.addAttribute("ejemplares", ejemplares);  // Pasar los ejemplares al modelo
            model.addAttribute("codigoPlanta", codigoPlanta);  // Pasar el código de la planta al modelo
        }

        return "FiltrarEjemplar";  // Renderizar la página con los resultados
    }

    
    
    @GetMapping("/VerMensajesEjemplar")
    public String mostrarSeleccionEjemplar(Model model) {
        List<Ejemplar> ejemplares = (List<Ejemplar>) controlador.getServiciosEjemplar().verEjemplares();
        System.out.println("Cargando ejemplares en la página: " + ejemplares);
        model.addAttribute("ejemplares", ejemplares);
        return "VerMensajesEjemplar"; 
    }
    
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
    
    @GetMapping("/VerMensajes")
    public String mostrarMenuMensajes(@RequestParam(value = "seccion", required = false) String seccion, Model model) {
        model.addAttribute("seccion", seccion);
        model.addAttribute("mensajes", controlador.getServiciosMensaje().verMensajes());
        model.addAttribute("personas", controlador.getServiciosPersona().totalPersonas());
        model.addAttribute("plantas", controlador.getServiciosPlanta().verPlantas());
        return "VerMensajes";
    }

    @PostMapping("/MensajesPorPersona")
    public String verMensajesPorPersona(@RequestParam("idPersona") Long idPersona, Model model) {
        model.addAttribute("seccion", "mensajesPorPersona");
        model.addAttribute("mensajesPorPersona", controlador.getServiciosMensaje().buscarMensajesPorPersona(idPersona));
        model.addAttribute("personas", controlador.getServiciosPersona().totalPersonas());
        model.addAttribute("plantas", controlador.getServiciosPlanta().verPlantas());
        return "VerMensajes";
    }

    @PostMapping("/MensajesPorPlanta")
    public String verMensajesPorPlanta(@RequestParam("codigoPlanta") String codigoPlanta, Model model) {
        model.addAttribute("seccion", "mensajesPorPlanta");
        model.addAttribute("mensajesPorPlanta", controlador.getServiciosMensaje().buscarMensajesPorPlanta(codigoPlanta));
        model.addAttribute("personas", controlador.getServiciosPersona().totalPersonas());
        model.addAttribute("plantas", controlador.getServiciosPlanta().verPlantas());
        return "VerMensajes";
    }

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
