package gestiondeproductos.logica;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Alimento extends Producto {
    private StringProperty fechaVencimiento = new SimpleStringProperty();
    private BooleanProperty esPerecedero = new SimpleBooleanProperty();

    // Constructor corregido
    public Alimento(int id, String nombre, double precio, String fechaVencimiento, boolean esPerecedero, int stock) {
        super(id, nombre, precio, "Alimento", stock, 0); // Se pasa 0 como valor por defecto para "otroInt"
        this.fechaVencimiento.set(fechaVencimiento); // Inicializamos la propiedad
        this.esPerecedero.set(esPerecedero);         // Inicializamos la propiedad
    }

    @Override
    public String getCategoria() {
        return "Alimento";
    }

    // Métodos para obtener los valores
    public String getFechaVencimiento() {
        return fechaVencimiento.get();
    }

    public boolean isEsPerecedero() {
        return esPerecedero.get();
    }

    // Métodos para establecer los valores
    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento.set(fechaVencimiento);
    }

    public void setEsPerecedero(boolean esPerecedero) {
        this.esPerecedero.set(esPerecedero);
    }

    // Métodos para obtener las propiedades (necesarios para enlazar con JavaFX)
    public StringProperty fechaVencimientoProperty() {
        return fechaVencimiento;
    }

    public BooleanProperty esPerecederoProperty() {
        return esPerecedero;
    }
}