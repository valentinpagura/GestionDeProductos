package gestiondeproductos.logica;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

// Implementamos Comparable para ordenamiento natural por precio
public abstract class Producto implements Comparable<Producto> {
    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty nombre = new SimpleStringProperty();
    private DoubleProperty precio = new SimpleDoubleProperty();
    private IntegerProperty stock = new SimpleIntegerProperty();

    // Constructor
    public Producto(int id, String nombre, double precio, String categoria, int stock, int otroInt) {
        this.id.set(id);
        this.nombre.set(nombre);
        this.precio.set(precio);
        this.stock.set(stock);
    }

    // Implementación de Comparable para ordenamiento natural por precio
    @Override
    public int compareTo(Producto otro) {
        return Double.compare(this.getPrecio(), otro.getPrecio());
    }

    // Getters para los valores
    public int getId() {
        return id.get();
    }

    public String getNombre() {
        return nombre.get();
    }

    public double getPrecio() {
        return precio.get();
    }

    public int getStock() {
        return stock.get();
    }

    // Setters para los valores
    public void setId(int id) {
        this.id.set(id);
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public void setPrecio(double precio) {
        this.precio.set(precio);
    }

    public void setStock(int stock) {
        this.stock.set(stock);
    }

    // Getters para las propiedades (necesarios para enlazar con JavaFX)
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public DoubleProperty precioProperty() {
        return precio;
    }

    public IntegerProperty stockProperty() {
        return stock;
    }

    // Método abstracto para obtener la categoría
    public abstract String getCategoria();
}