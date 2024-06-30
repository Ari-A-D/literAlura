package com.aluracursos.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "libro_lenguaje")
public class LenguajeLibro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "libro_id")
    private Libro libro;

    @Column(name = "lenguaje")
    private String lenguaje;

    public LenguajeLibro() {}

    public LenguajeLibro(Libro libro, String lenguaje) {
        this.libro = libro;
        this.lenguaje = lenguaje;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public String getLenguaje() {
        return lenguaje;
    }

    public void setLenguaje(String lenguaje) {
        this.lenguaje = lenguaje;
    }

    @Override
    public String toString() {
        return lenguaje;
    }
}
