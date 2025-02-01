package gestiondeproductos.logica;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GestorDeProductos implements Crud<Producto> {
    private final List<Producto> productos;

    public GestorDeProductos() {
        this.productos = new ArrayList<>();
    }

    @Override
    public void crear(Producto producto) {
        if (existeProducto(producto.getId())) {
            throw new RuntimeException("Producto duplicado");
        }
        productos.add(producto);
    }

    @Override
    public List<Producto> leer() {
        return productos;
    }

    @Override
    public Producto leer(int id) {
        for (Producto p : productos) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    @Override
    public void actualizar(Producto producto) {
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getId() == producto.getId()) {
                productos.set(i, producto);
                return;
            }
        }
    }

    @Override
    public void eliminar(int id) {
        productos.removeIf(p -> p.getId() == id);
    }

    private boolean existeProducto(int id) {
        return productos.stream().anyMatch(p -> p.getId() == id);
    }

    public void ordenarPorPrecio() {
        Collections.sort(productos);
    }

    public void ordenarPor(Comparator<Producto> comparator) {
        Collections.sort(productos, comparator);
    }

    public List<? extends Producto> filtrarPorPrecioMayorA(double precioMinimo) {
        return productos.stream()
                       .filter(p -> p.getPrecio() > precioMinimo)
                       .collect(Collectors.toList());
    }

    public void procesarProductos(List<? super Producto> destino, Predicate<Producto> filtro) {
        productos.stream()
                .filter(filtro)
                .forEach(destino::add);
    }

    public List<Producto> filtrarPorNombre(String texto) {
        return productos.stream()
                       .filter(p -> p.getNombre().toLowerCase().contains(texto.toLowerCase()))
                       .collect(Collectors.toList());
    }

    public List<Producto> filtrarPorRangoPrecio(double min, double max) {
        return productos.stream()
                       .filter(p -> p.getPrecio() >= min && p.getPrecio() <= max)
                       .collect(Collectors.toList());
    }

    public List<Producto> filtrarPorCategoria(String categoria) {
        return productos.stream()
                       .filter(p -> p.getCategoria().equalsIgnoreCase(categoria))
                       .collect(Collectors.toList());
    }

    // Método para exportar a JSON
    public void exportarJSON(String archivo) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Writer writer = new FileWriter(archivo)) {
            gson.toJson(productos, writer); // Exporta la lista "productos" a un json
        } catch (IOException e) {
            System.err.println("Error al exportar a JSON: " + e.getMessage());
        }
    }

    // Método para importar desde JSON
    public void importarJSON(String archivo) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(archivo)) {
            // Define el tipo de la lista de productos
            Type tipoListaProductos = new TypeToken<List<Producto>>() {}.getType();

            // Convierte el JSON en una lista de productos
            List<Producto> productosImportados = gson.fromJson(reader, tipoListaProductos);

            // Verifica si la lista de productos importados es nula
            if (productosImportados != null) {
                // Limpia la lista actual de productos
                this.productos.clear();

                // Agrega los productos importados a la lista
                this.productos.addAll(productosImportados);

                System.out.println("Productos importados desde '" + archivo + "' correctamente.");
            } else {
                System.err.println("El archivo JSON está vacío o no contiene datos válidos.");
            }
        } catch (IOException e) {
            System.err.println("Error al importar desde JSON: " + e.getMessage());
        }
    }
}