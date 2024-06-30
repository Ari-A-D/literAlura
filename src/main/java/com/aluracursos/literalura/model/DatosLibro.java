package com.aluracursos.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro(
        @JsonAlias("title")
        String titulo,
        @JsonAlias("languages")
        List<String> lenguaje,
        @JsonAlias("authors")
        List<DatosAutor> autores
) {

}
