package com.aluracursos.literalura.model;
import jakarta.persistence.*;
import java.util.List;
import java.util.stream.Collectors;
@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String titulo;

    @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<LenguajeLibro> lenguajes;

    @ManyToMany(mappedBy = "libro", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Autor> autores;

    public Libro(){}

    public Libro(DatosLibro datosLibro){
        this.titulo = datosLibro.titulo();
        // Crear objetos LenguajeLibro a partir de los idiomas en datosLibro
        this.lenguajes = datosLibro.lenguaje().stream()
                .map(l -> new LenguajeLibro(this, l))
                .collect(Collectors.toList());
        this.autores = datosLibro.autores().stream().map(DatosAutor::toAutor).collect(Collectors.toList());
        this.autores.forEach(autor -> autor.setLibro(this)); // Enlaza cada autor con este libro
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    public List<LenguajeLibro> getLenguajes() {
        return lenguajes;
    }

    public void setLenguajes(List<LenguajeLibro> lenguajes) {
        this.lenguajes = lenguajes;
    }

    @Override
    public String toString() {
        return  "Titulo: " + titulo + "\n" +
                "Autor/es=" + autores + "\n" +
                "#######################################";
    }
}
