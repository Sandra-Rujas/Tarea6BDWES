package com.sandrarujas.tarea6dwessandrarujas.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sandrarujas.tarea6dwessandrarujas.modelo.Planta;
import com.sandrarujas.tarea6dwessandrarujas.servicios.Controlador;

import jakarta.servlet.http.HttpSession;

@Controller
public class ViveroInvitadoController {

	@Autowired
	Controlador controlador;
	

	/**
     * Muestra la página de bienvenida del vivero invitado.
     * @param nombre El nombre a mostrar, por defecto es "Mundo".
     * @param model El objeto Model que se pasa a la vista.
     * @return El nombre de la vista a mostrar ("ViveroInvitado").
     */
	
	@GetMapping({ "/" })
	public String welcome(@RequestParam(name = "nombre", required = false, defaultValue = "Mundo") String nombre,
			Model model) {
		return "ViveroInvitado";
	}

	/**
     * Muestra el formulario de login.
     * @param error El mensaje de error, si existe.
     * @param model El objeto Model que se pasa a la vista.
     * @return El nombre de la vista de login.
     */
	
    @GetMapping("/login")
    public String loginForm(@RequestParam(name = "error", required = false) String error, Model model) {
        
        return "login";
    }

    
    /**
     * Procesa el formulario de login y autentica al usuario.
     * @param usuario El nombre de usuario ingresado.
     * @param password La contraseña ingresada.
     * @param model El objeto Model para pasar datos a la vista.
     * @param session La sesión HTTP donde se almacena la información del usuario autenticado.
     * @return Redirige a la vista de administrador o personal según el ID del usuario.
     */
    
    @PostMapping("/login")
    public String login(@RequestParam("username") String usuario, @RequestParam("password") String password,
            Model model, HttpSession session) {
        try {
            boolean autenticar = controlador.getServiciosCredencial().autenticar(usuario, password);

            
                session.setAttribute("usuarioAutenticado", usuario);
                
                Integer userId = controlador.getServiciosCredencial().obtenerUserIdPorUsername(usuario); 

                session.setAttribute("userId", userId); 

                if (userId != null && userId == 1) {
                    return "redirect:/ViveroAdmin";  
                } else {
                    return "redirect:/ViveroPersonal";  
                }
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "No se ha podido iniciar sesión: " + e.getMessage());
            return "login";
        }
    }

    /**
     * Muestra todas las plantas disponibles en el vivero.
     * @param model El objeto Model que se pasa a la vista.
     * @return El nombre de la vista que muestra la lista de plantas.
     */
    
	@GetMapping("/VerPlantas")
	public String mostrarPlantas(Model model) {
		
		try {
			List<Planta> plantas = controlador.getServiciosPlanta().verPlantas(); 									

			if (plantas == null || plantas.isEmpty()) {
				model.addAttribute("mensaje", "No hay plantas disponibles en la base de datos.");
			} else {
				model.addAttribute("plantas", plantas);
			}

			return "VerPlantas";
		} catch (Exception e) {
			model.addAttribute("mensaje", "Error al cargar las plantas: " + e.getMessage());
			return "VerPlantas"; 
		}
	}
	
	
	/** 
	 * Cierra la aplicación de forma inmediata.
	 * @return No devuelve un valor útil, ya que la aplicación se cierra antes de ejecutar el retorno.
	 */
	@GetMapping("/salir")
	public String cerrarAplicacion() {
	    System.exit(0); 
	    return "redirect:/"; 
	}

}
