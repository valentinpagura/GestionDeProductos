package gestiondeproductos.logica;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Ropa extends Producto {
    private StringProperty marca = new SimpleStringProperty();
    private IntegerProperty talle = new SimpleIntegerProperty();

    // Constructor
    public Ropa(String marca, int talle, int id, String nombre, double precio, String categoria, int stock, int otroInt) {
        super(id, nombre, precio, categoria, stock, otroInt);
        this.marca.set(marca); // Inicializamos la propiedad
        this.talle.set(talle); // Inicializamos la propiedad
    }

    @Override
    public String getCategoria() {
        return "Ropa";
    }

    // Métodos para obtener los valores
    public String getMarca() {
        return marca.get();
    }

    public int getTalle() {
        return talle.get();
    }

    // Métodos para establecer los valores
    public void setMarca(String marca) {
        this.marca.set(marca);
    }

    public void setTalle(int talle) {
        this.talle.set(talle);
    }

    // Métodos para obtener las propiedades (necesarios para enlazar con JavaFX)
    public StringProperty marcaProperty() {
        return marca;
    }

    public IntegerProperty talleProperty() {
        return talle;
    }
}