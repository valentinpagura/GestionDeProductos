package gestiondeproductos.logica;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Electronico extends Producto {
    private ObjectProperty<TipoElectronico> tipoElectronicoProperty = new SimpleObjectProperty<>();

    // Constructor corregido
    public Electronico(int id, String nombre, double precio, TipoElectronico tipoElectronico, int stock, int otroInt) {
        super(id, nombre, precio, "Electrónico", stock, otroInt); // Se incluye "otroInt"
        this.tipoElectronicoProperty.set(tipoElectronico); // Inicializamos la propiedad con el tipo correcto
    }

    @Override
    public String getCategoria() {
        return "Electronico";
    }

    // Método para obtener el valor de la propiedad
    public TipoElectronico getTipoElectronico() {
        return tipoElectronicoProperty.get();
    }

    // Método para establecer el valor de la propiedad
    public void setTipoElectronico(TipoElectronico tipoElectronico) {
        this.tipoElectronicoProperty.set(tipoElectronico);
    }

    // Método para obtener la propiedad en sí (necesario para enlazar con JavaFX)
    public ObjectProperty<TipoElectronico> tipoElectronicoProperty() {
        return tipoElectronicoProperty;
    }
}