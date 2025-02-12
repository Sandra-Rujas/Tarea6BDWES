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

	@GetMapping({ "/", "/welcome" })
	public String welcome(@RequestParam(name = "nombre", required = false, defaultValue = "Mundo") String nombre,
			Model model) {

		model.addAttribute("nombre", nombre);
		return "ViveroInvitado";
	}

	   // Muestra el formulario de login
    @GetMapping("/login")
    public String loginForm(@RequestParam(name = "error", required = false) String error, Model model) {
        
        return "login";
    }

    // Procesa el login
    @PostMapping("/login")
    public String login(@RequestParam("username") String usuario, @RequestParam("password") String password,
            Model model, HttpSession session) {
        try {
            // Autenticamos el usuario (puedes agregar aquí tu lógica de autenticación)
            boolean autenticar = controlador.getServiciosCredencial().autenticar(usuario, password);

            
                // Guardamos el nombre de usuario en la sesión
                session.setAttribute("usuarioAutenticado", usuario);
                
                // Aquí obtenemos el userId directamente (lo puedes obtener de tu base de datos)
                Integer userId = obtenerUserIdPorUsername(usuario); // Método que obtiene el userId por username

                session.setAttribute("userId", userId); // Guardamos el userId en la sesión

                // Redirigir al admin si el userId es 1, de lo contrario al personal
                if (userId != null && userId == 1) {
                    return "redirect:/ViveroAdmin";  // Admin
                } else {
                    return "redirect:/ViveroPersonal";  // Personal
                }
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "No se ha podido iniciar sesión: " + e.getMessage());
            return "login";
        }
    }

	private Integer obtenerUserIdPorUsername(String username) {
		// Lógica para obtener el userId del usuario
		if (username.equals("admin")) {
			return 1; // Administrador
		} else {
			return 2; // Usuario normal
		}
	}

	@GetMapping("/VerPlantas")
	public String mostrarPlantas(Model model) {
		
		try {
			// Obtenemos la lista de plantas desde el servicio
			List<Planta> plantas = controlador.getServiciosPlanta().verPlantas(); // Devuelve una lista, no es necesario
																					// hacer el cast

			if (plantas == null || plantas.isEmpty()) {
				// Si no hay plantas, pasamos un mensaje al modelo
				model.addAttribute("mensaje", "No hay plantas disponibles en la base de datos.");
			} else {
				// Si hay plantas, las pasamos al modelo
				model.addAttribute("plantas", plantas);
			}

			// Devolvemos la vista para mostrar las plantas
			return "VerPlantas";
		} catch (Exception e) {
			// Capturamos la excepción y pasamos un mensaje de error al modelo
			model.addAttribute("mensaje", "Error al cargar las plantas: " + e.getMessage());
			return "VerPlantas"; // En caso de error, seguimos mostrando la vista
		}
	}

}
