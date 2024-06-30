package com.aluracursos.literalura.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nombre;
    private Integer fechaNacimiento;
    private Integer fechaMuerte;

    @ManyToOne
    @JoinColumn(name = "libro_id") // Define la columna de unión en la tabla de autores
    private Libro libro;

    public Autor(){}

    public Autor(DatosAutor datosAutor){
        this.nombre = datosAutor.nombre();
        this.fechaNacimiento = parseFecha(datosAutor.fechaNacimiento());
        this.fechaMuerte = parseFecha(datosAutor.fechaMuerte());
    }

    private Integer parseFecha(String fecha) {
        try {
            return fecha != null ? Integer.valueOf(fecha) : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = Integer.valueOf(fechaNacimiento);
    }

    public Integer getFechaMuerte() {
        return fechaMuerte;
    }

    public void setFechaMuerte(String fechaMuerte) {
        this.fechaMuerte = Integer.valueOf(fechaMuerte);
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Autor autor = (Autor) o;
        return Objects.equals(nombre, autor.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }

    @Override
    public String toString() {
        return  "Nombre: " + nombre + "\n" +
                "Fecha Nacimiento: " + fechaNacimiento + "\n" +
                "Fecha de fallecimiento: " + fechaMuerte + "\n" +
                "------------------------------------------------";
    }
}
