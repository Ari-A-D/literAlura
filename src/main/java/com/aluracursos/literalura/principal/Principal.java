package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.*;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvertirDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private ConvertirDatos conversor = new ConvertirDatos();
    private final LibroRepository repositorio;

    @Autowired
    public Principal(LibroRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void menu(){
        var opcion = -1;
        while (opcion != 6){
            var menu = """
                    ------------LiterAlura Menu------------
                    
                    1- Buscar Libro.
                    2- Listar Libro.
                    3- Listar Autores.
                    4- Listar Autores Vivos.
                    5- Listar por Idioma
                    6- Salir de la aplicacion.
                    
                    Seleccione una opcion:
                    """;
            var subMenu = """
                    #####################################
                    Seleccione el idioma que desea buscar:
                    
                    1 -> "Ingles";
                    2 -> "Espanol";
                    3 -> "Frances";
                    4 -> "Portugues";
                    
                    Su seleccion:
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();
            switch (opcion){
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    listarLibros();
                    break;
                case 3:
                    listarAutores();
                    break;
                case 4:
                    System.out.println("Ingresar el año para buscar la lista de autores vivos a partir de el");
                    var anio = teclado.nextInt();
                    teclado.nextLine();
                    listarAutoresVivosEnAnio(anio);
                    break;
                case 5:
                    System.out.println(subMenu);
                    var opcionIdioma = teclado.nextInt();
                    teclado.nextLine();
                    listarLibrosPorIdioma(selectorInternoIdioma(opcionIdioma));
                    break;
                case 6:
                    break;
                default:
                    System.out.println("Opcion invalida");
            }
        }

    }
    public void buscarLibro() {
        System.out.println("Escribe el nombre del libro que deseas buscar: ");
        var nombreLibro = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreLibro.replace(" ", "+"));
        var datos = conversor.obtenerDatos(json, Datos.class);
        Optional<DatosLibro> libroBuscado = datos.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(nombreLibro.toUpperCase()))
                .findFirst();

        if (libroBuscado.isPresent()) {
            DatosLibro datosLibro = libroBuscado.get();
            boolean libroYaGuardado = repositorio.findAll().stream()
                    .anyMatch(libro -> libro.getTitulo().equalsIgnoreCase(datosLibro.titulo()));
            if (libroYaGuardado) {
                System.out.println("El libro ya se encuentra guardado");
            } else {
                Libro libro = new Libro(datosLibro);
                repositorio.save(libro);
                System.out.println("Libro guardado correctamente");
            }
        } else {
            System.out.println("Libro no encontrado");
        }
    }

    public void listarLibros() {
        List<Libro> libros = repositorio.findAll();
        System.out.println("LIBROS\n");
        libros.forEach(libro -> {
            String idiomas = libro.getLenguajes().stream()
                    .map(LenguajeLibro::getLenguaje)
                    .collect(Collectors.joining(", ")); // Joining los idiomas con ", "

            String autores = libro.getAutores().stream()
                    .map(Autor::getNombre)
                    .collect(Collectors.joining(", ")); // Joining los autores con ", "
            System.out.println("*-Título: '" + libro.getTitulo()+"'");
            System.out.println("  Idioma(s): '" + traducirIdioma(idiomas)+"'");
            System.out.println("  Autor(es): '" + autores+"'");
        });
    }


    public void listarAutores() {
        Set<Autor> autoresUnicos = repositorio.findAll().stream()
                .flatMap(libro -> libro.getAutores().stream())
                .collect(Collectors.toSet());
        System.out.println("AUTORES\n");
        autoresUnicos.forEach(System.out::println);
    }


    public void listarAutoresVivosEnAnio(int anio) {
        boolean hayAutores = repositorio.findAll().stream()
                .flatMap(libro -> libro.getAutores().stream())
                .filter(autor -> autor.getFechaMuerte() != 0 && autor.getFechaMuerte() < anio)
                .peek(System.out::println) // Mostrar los autores vivos encontrados
                .findAny()
                .isPresent();
        if (!hayAutores) {
            System.out.println("No hay autores vivos de ese año para atrás.");
        }
    }


    public void listarLibrosPorIdioma(String idioma) {
        String nombreIdioma = traducirIdioma(idioma);
        List<Libro> librosEnIdioma = repositorio.findAll().stream()
                .filter(libro -> libro.getLenguajes().stream().anyMatch(lenguaje -> lenguaje.getLenguaje().equalsIgnoreCase(idioma)))
                .collect(Collectors.toList());

        if (!librosEnIdioma.isEmpty()) {
            System.out.println("LENGUAJE: " + nombreIdioma+"\n");
            librosEnIdioma.forEach(libro -> System.out.println("-*-"+libro.getTitulo()));
        } else {
            System.out.println("No se encontraron libros en el idioma: " + nombreIdioma);
        }
    }


    public String traducirIdioma(String codigoIdioma) {
        return switch (codigoIdioma.toLowerCase()) {
            case "en" -> "Inglés";
            case "es" -> "Español";
            case "fr" -> "Francés";
            case "pr" -> "Portugués";
            default -> codigoIdioma;
        };
    }

    public String selectorInternoIdioma(int numero) {
        return switch (numero) {
            case 1 -> "en";
            case 2 -> "es";
            case 3 -> "fr";
            case 4 -> "pr";
            default -> throw new IllegalArgumentException("Número de idioma no válido: " + numero);
        };
    }
}
