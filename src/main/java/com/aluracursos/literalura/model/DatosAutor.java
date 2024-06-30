package com.aluracursos.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosAutor(
        @JsonAlias("name")
        String nombre,
        @JsonAlias("birth_year")
        String fechaNacimiento,
        @JsonAlias("death_year")
        String fechaMuerte
) {
        public Autor toAutor() {
                return new Autor(this);
        }
}
