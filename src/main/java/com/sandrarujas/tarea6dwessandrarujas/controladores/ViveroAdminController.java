package com.sandrarujas.tarea6dwessandrarujas.controladores;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sandrarujas.tarea6dwessandrarujas.modelo.Credencial;
import com.sandrarujas.tarea6dwessandrarujas.modelo.Ejemplar;
import com.sandrarujas.tarea6dwessandrarujas.modelo.Mensaje;
import com.sandrarujas.tarea6dwessandrarujas.modelo.Persona;
import com.sandrarujas.tarea6dwessandrarujas.modelo.Planta;
import com.sandrarujas.tarea6dwessandrarujas.servicios.Controlador;

@Controller
public class ViveroAdminController {

	@Autowired
	Controlador controlador;

	@GetMapping("/ViveroAdmin")
	public String mostrarMenuAdmin(Model model) {
		return "ViveroAdmin";
	}

	// GESTIÓN PERSONAS
    @GetMapping("/CrearPersona")
    public String mostrarFormularioRegistro(Model model) {
        return "CrearPersona";
    }

    @PostMapping("/CrearPersona")
    public String registrarPersona(@RequestParam String nombre,
                                   @RequestParam String email,
                                   @RequestParam String usuario,
                                   @RequestParam String contraseña,
                                   Model model) {
        try {
            // Verifica si el email ya está en uso
            if (controlador.getServiciosPersona().emailExistente(email)) {
                model.addAttribute("mensajeError", "El email ya está registrado.");
                return "CrearPersona";
            }

            // Verifica si el usuario ya existe o es inválido
            if (controlador.getServiciosCredencial().usuarioExistente(usuario) || usuario.length() < 3) {
                model.addAttribute("mensajeError", "Usuario registrado o no válido.");
                return "CrearPersona";
            }
            
            if(usuario.contains(" ")) {
            	model.addAttribute("mensajeError", "Usuario con espacios.");
                return "CrearPersona";
            }
            
        

            // Verifica la contraseña
            if (!controlador.getServiciosCredencial().validarPassword(contraseña)) {
                model.addAttribute("mensajeError", "La contraseña debe tener entre 6 y 20 caracteres.");
                return "CrearPersona";
            }

            // Crear nueva persona y credencial
            Persona persona = new Persona();
            persona.setNombre(nombre);
            persona.setEmail(email);

            Credencial credencial = new Credencial();
            credencial.setUsuario(usuario);
            credencial.setPassword(contraseña);
            credencial.setPersona(persona);
            persona.setCredencial(credencial);

            // Validación final
            if (!controlador.getServiciosPersona().validarPersona(persona)) {
                model.addAttribute("mensajeError", "Los datos no son válidos.");
                return "CrearPersona";
            }

            // Guardar en la base de datos
            controlador.getServiciosPersona().insertar(persona);
            model.addAttribute("mensajeExito", "Usuario registrado correctamente.");

        } catch (Exception ex) {
            model.addAttribute("mensajeError", "Error al registrar usuario: " + ex.getMessage());
        }

        return "CrearPersona";
    }
	
	

    @GetMapping("/VerPersonas")
    public String mostrarPersonas(Model model) {
        // Obtener la lista de todas las personas
		ArrayList<Persona> personas = (ArrayList<Persona>) controlador.getServiciosPersona().totalPersonas();

        if (personas == null || personas.isEmpty()) {
            model.addAttribute("personas", new ArrayList<>()); // Pasar una lista vacía
        } else {
            model.addAttribute("personas", personas);
        }

        return "VerPersonas";
    }

	// GESTIÓN PLANTAS
	@GetMapping("/CrearPlanta")
	public String crearPlanta() {
		return "CrearPlanta";
	}

	@PostMapping("/CrearPlanta")
	public String crearPlanta(@RequestParam("codigo") String codigo, @RequestParam("nombreComun") String nombreComun,
			@RequestParam("nombreCientifico") String nombreCientifico, Model model) {
		try {
			// Limpiamos posibles espacios antes y después de la entrada
			codigo = codigo.trim().toUpperCase();
			nombreComun = nombreComun.trim();
			nombreCientifico = nombreCientifico.trim();

			// Validamos el código de la planta
			boolean correcto = controlador.getServiciosPlanta().validarCodigoPlanta(codigo);
			boolean existe = controlador.getServiciosPlanta().codigoExistente(codigo);

			if (!correcto) {
				// Si el formato del código es incorrecto
				model.addAttribute("mensajeError", "El formato del código no es correcto.");
				return "CrearPlantas"; // Volver a la vista de registro de planta
			}

			if (existe) {
				// Si el código ya existe
				model.addAttribute("mensajeError", "Código de planta ya existente.");
				return "CrearPlanta"; // Volver a la vista de registro de planta
			}

			// Creamos la nueva planta
			Planta nuevaPlanta = new Planta();
			nuevaPlanta.setCodigo(codigo);
			nuevaPlanta.setNombreComun(nombreComun);
			nuevaPlanta.setNombreCientifico(nombreCientifico);

			// Validamos la planta
			boolean datosPlanta = controlador.getServiciosPlanta().validarPlanta(nuevaPlanta);
			if (!datosPlanta) {
				model.addAttribute("mensajeError", "Los datos que has introducido no son correctos.");
				return "CrearPlantas"; // Volver a la vista de registro de planta
			}

			// Si todo es correcto, registrar la planta (por ejemplo, en la base de datos)
			controlador.getServiciosPlanta().insertar(nuevaPlanta);

			// Redirigimos a la página de éxito o a alguna otra página
			model.addAttribute("mensajeExito", "Planta registrada exitosamente.");
			return "ViveroAdmin"; // Puede ser redirigido a cualquier otra vista de administración

		} catch (Exception e) {
			model.addAttribute("mensajeError", "Error al registrar la planta: " + e.getMessage());
			return "CrearPlantas"; // Volver a la vista de registro de planta
		}
	}

	// Mostrar la página con la opción seleccionada
	@GetMapping("/ModificarPlanta")
	public String mostrarMenuModificarPlanta(Model model,
			@RequestParam(name = "opcion", required = false) Integer opcion) {
		if (opcion != null) {
			model.addAttribute("opcion", opcion);
		}
		return "ModificarPlanta";
	}

	// Procesar la actualización de la planta (nombre común o nombre científico)
	@PostMapping("/ModificarPlanta")
	public String actualizarPlanta(@RequestParam("codigo") String codigo,
			@RequestParam(name = "nombreComun", required = false) String nombreComun,
			@RequestParam(name = "nombreCientifico", required = false) String nombreCientifico,
			@RequestParam("opcion") Integer opcion, Model model) {
		try {
			// Validación del código de planta
			boolean valido = controlador.getServiciosPlanta().validarCodigoPlanta(codigo);
			if (!valido) {
				model.addAttribute("mensaje", "El código de la planta no es válido.");
				return "ModificarPlanta";
			}

			// Verificar si la planta existe
			boolean existe = controlador.getServiciosPlanta().codigoExistente(codigo);
			if (!existe) {
				model.addAttribute("mensaje", "El código de la planta no existe en la base de datos.");
				return "ModificarPlanta";
			}

			// Realizar la actualización según la opción seleccionada
			boolean actualizado = false;
			if (opcion == 1 && nombreComun != null) {
				actualizado = controlador.getServiciosPlanta().actualizarNombreComun(codigo, nombreComun);
				if (actualizado) {
					model.addAttribute("mensajeExito", "El nombre común de la planta ha sido actualizado.");
				} else {
					model.addAttribute("mensaje", "Error al actualizar el nombre común.");
				}
			} else if (opcion == 2 && nombreCientifico != null) {
				actualizado = controlador.getServiciosPlanta().actualizarNombreCientifico(codigo, nombreCientifico);
				if (actualizado) {
					model.addAttribute("mensajeExito", "El nombre científico de la planta ha sido actualizado.");
				} else {
					model.addAttribute("mensaje", "Error al actualizar el nombre científico.");
				}
			} else {
				model.addAttribute("mensaje", "Opción no válida.");
			}
		} catch (Exception e) {
			model.addAttribute("mensaje", "Error al intentar actualizar la planta: " + e.getMessage());
		}

		return "ModificarPlanta"; // Regresar a la misma página
	}

	// GESTIÓN MENSAJES
	// Método GET para mostrar la lista de ejemplares y el formulario
	@GetMapping("/CrearMensaje")
	public String mostrarFormularioCrearMensaje(Model model) {
		List<Ejemplar> ejemplares = (List<Ejemplar>) controlador.getServiciosEjemplar().verEjemplares();

		if (ejemplares == null || ejemplares.isEmpty()) {
			model.addAttribute("mensajeError", "No hay ejemplares disponibles.");
		} else {
			model.addAttribute("ejemplares", ejemplares);
		}

		return "CrearMensaje";
	}

	// Método POST para seleccionar el ejemplar y redirigir al formulario de mensaje
	@PostMapping("/SeleccionarEjemplar")
	public String seleccionarEjemplar(@RequestParam Long idEjemplar, Model model) {
		Ejemplar ejemplar = controlador.getServiciosEjemplar().buscarPorID(idEjemplar);

		if (ejemplar == null) {
			model.addAttribute("mensajeError", "No se encontró un ejemplar con ese ID.");
			return "CrearMensaje";
		}

		model.addAttribute("ejemplarSeleccionado", ejemplar);
		return "CrearMensaje"; // Carga nuevamente la vista pero con el ejemplar seleccionado
	}

	// Método POST para crear el mensaje con el ejemplar seleccionado
	@PostMapping("/CrearMensaje")
	public String crearMensaje(@RequestParam Long idEjemplar, @RequestParam String mensajeTexto, Model model) {
		boolean exito = false;

		if (mensajeTexto != null && !mensajeTexto.trim().isEmpty()) {
			try {
				Ejemplar ejemplar = controlador.getServiciosEjemplar().buscarPorID(idEjemplar);
				if (ejemplar != null) {
					String usuarioAutenticado = controlador.obtenerUsuarioConectado();
					Persona p = controlador.getServiciosPersona().buscarPorNombre(usuarioAutenticado);

					if (p == null) {
						model.addAttribute("mensajeError", "No se ha encontrado la persona autenticada.");
					} else {
						Mensaje nuevoMensaje = new Mensaje(LocalDateTime.now(), mensajeTexto, p, ejemplar);
						controlador.getServiciosMensaje().addMensaje(nuevoMensaje);
						exito = true;
					}
				} else {
					model.addAttribute("mensajeError", "No se encontró un ejemplar con ese ID.");
				}
			} catch (Exception e) {
				model.addAttribute("mensajeError", "Error al crear el mensaje: " + e.getMessage());
			}
		} else {
			model.addAttribute("mensajeError", "El mensaje no puede estar vacío.");
		}

		if (exito) {
			model.addAttribute("mensajeExito", "Mensaje añadido con éxito.");
		}

		List<Ejemplar> ejemplares = (List<Ejemplar>) controlador.getServiciosEjemplar().verEjemplares();
		model.addAttribute("ejemplares", ejemplares);

		return "CrearMensaje";
	}
}
