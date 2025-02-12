package com.sandrarujas.tarea6dwessandrarujas.modelo;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="personas")
public class Persona implements Serializable{

		//Atributos
        private static final long serialVersionUID = 1L;

        @Id
        @GeneratedValue(strategy=GenerationType.IDENTITY)
        private Long id;

        @Column(nullable= false)
        private String nombre;

        @Column(unique=true, nullable = false)
        private String email;

        @OneToOne(mappedBy= "persona", cascade= CascadeType.ALL)
        private Credencial credencial;

        @OneToMany(mappedBy = "persona")
        private List<Mensaje> mensajes = new LinkedList<Mensaje>();
        
      //Constructores

        public Persona() {
        }

        public Persona(String nombre, String email) {
                this.nombre = nombre;
                this.email = email;
        }

        
      //Getters y Setters
        
        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getNombre() {
                return nombre;
        }

        public void setNombre(String nombre) {
                this.nombre = nombre;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public Credencial getCredenciales() {
                return credencial;
        }

        public void setCredencial(Credencial credencial) {
                this.credencial = credencial;
        }


        public List<Mensaje> getMensajes() {
                return mensajes;
        }

        public void setMensajes(List<Mensaje> mensajes) {
                this.mensajes = mensajes;
        }

        
        //Métodos Equals y HashCode
        
        @Override
        public int hashCode() {
                return Objects.hash(credencial, email, id, nombre);
        }

        @Override
        public boolean equals(Object obj) {
                if (this == obj)
                        return true;
                if (obj == null)
                        return false;
                if (getClass() != obj.getClass())
                        return false;
                Persona other = (Persona) obj;
                return Objects.equals(credencial, other.credencial) && Objects.equals(email, other.email)
                                && Objects.equals(id, other.id) && Objects.equals(nombre, other.nombre);
        }

        //Método toString
        
        @Override
        public String toString() {
                return "Persona ID:" + id + ", Nombre:" + nombre + ", Email:" + email + ", Credenciales:" + credencial;
        }



}
