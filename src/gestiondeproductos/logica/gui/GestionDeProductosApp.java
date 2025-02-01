package gestiondeproductos.logica.gui;

import gestiondeproductos.logica.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class GestionDeProductosApp extends Application {
    private final GestorDeProductos gestor = new GestorDeProductos();
    private TableView<Producto> tabla;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Gestión de Productos");

        // Configuración de la tabla
        configurarTabla();

        // Panel para los botones
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(
            crearBoton("Agregar Producto", this::agregarProducto),
            crearBoton("Eliminar Producto", this::eliminarProducto),
            crearBoton("Actualizar Producto", this::actualizarProducto)
        );

        // Layout principal
        HBox root = new HBox(10);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(tabla, vbox);

        // Escena y ventana principal
        Scene scene = new Scene(root, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void configurarTabla() {
        tabla = new TableView<>();

        TableColumn<Producto, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());

        TableColumn<Producto, String> nombreCol = new TableColumn<>("Nombre");
        nombreCol.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());

        TableColumn<Producto, Double> precioCol = new TableColumn<>("Precio");
        precioCol.setCellValueFactory(cellData -> cellData.getValue().precioProperty().asObject());

        TableColumn<Producto, Integer> stockCol = new TableColumn<>("Stock");
        stockCol.setCellValueFactory(cellData -> cellData.getValue().stockProperty().asObject());

        TableColumn<Producto, String> categoriaCol = new TableColumn<>("Categoría");
        categoriaCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCategoria()));

        tabla.getColumns().addAll(idCol, nombreCol, precioCol, stockCol, categoriaCol);
    }

    private Button crearBoton(String texto, Runnable accion) {
        Button boton = new Button(texto);
        boton.setOnAction(e -> accion.run());
        return boton;
    }

    private void agregarProducto() {
        Stage dialog = new Stage();
        dialog.setTitle("Agregar Producto");

        // Componentes del formulario
        ComboBox<String> tipoCombo = new ComboBox<>();
        tipoCombo.getItems().addAll("Electrónico", "Alimento", "Ropa");
        tipoCombo.setValue("Electrónico");

        TextField nombreField = new TextField();
        TextField precioField = new TextField();
        TextField stockField = new TextField();
        TextField campoEspecificoField = new TextField();
        Label campoEspecificoLabel = new Label();

        // Actualizar etiqueta según el tipo de producto
        tipoCombo.setOnAction(e -> actualizarEtiquetaCampoEspecifico(tipoCombo, campoEspecificoLabel));

        Button agregarButton = new Button("Agregar");
        agregarButton.setOnAction(e -> {
            try {
                Producto producto = crearProductoDesdeFormulario(
                    tipoCombo.getValue(),
                    nombreField.getText(),
                    Double.parseDouble(precioField.getText()),
                    Integer.parseInt(stockField.getText()),
                    campoEspecificoField.getText()
                );
                gestor.crear(producto);
                actualizarTabla();
                dialog.close();
            } catch (NumberFormatException ex) {
                mostrarAlerta("Error", "Por favor, ingrese valores válidos.");
            }
        });

        // Layout del formulario
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(
            new Label("Tipo de Producto:"), tipoCombo,
            new Label("Nombre:"), nombreField,
            new Label("Precio:"), precioField,
            new Label("Stock:"), stockField,
            campoEspecificoLabel, campoEspecificoField,
            agregarButton
        );

        Scene scene = new Scene(layout, 300, 450);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void actualizarEtiquetaCampoEspecifico(ComboBox<String> tipoCombo, Label campoEspecificoLabel) {
        switch (tipoCombo.getValue()) {
            case "Electrónico":
                campoEspecificoLabel.setText("Tipo:");
                break;
            case "Alimento":
                campoEspecificoLabel.setText("Fecha Caducidad:");
                break;
            case "Ropa":
                campoEspecificoLabel.setText("Talle:");
                break;
        }
    }

    private Producto crearProductoDesdeFormulario(String tipo, String nombre, double precio, int stock, String campoEspecifico) {
        int id = gestor.leer().size() + 1;

        switch (tipo) {
            case "Electrónico":
                return new Electronico(id, nombre, precio, TipoElectronico.valueOf(campoEspecifico), stock, 0);
            case "Alimento":
                return new Alimento(id, nombre, precio, campoEspecifico, true, stock);
            case "Ropa":
                return new Ropa("Marca", Integer.parseInt(campoEspecifico), id, nombre, precio, "Ropa", stock, 0);
            default:
                throw new IllegalArgumentException("Tipo de producto no válido");
        }
    }

    private void eliminarProducto() {
        Producto seleccionado = tabla.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText(null);
            confirmacion.setContentText("¿Está seguro que desea eliminar este producto?");

            if (confirmacion.showAndWait().get() == ButtonType.OK) {
                gestor.eliminar(seleccionado.getId());
                actualizarTabla();
            }
        } else {
            mostrarAlerta("Error", "Por favor, seleccione un producto para eliminar.");
        }
    }

    private void actualizarProducto() {
        Producto seleccionado = tabla.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            Stage dialog = new Stage();
            dialog.setTitle("Actualizar Producto");

            TextField nombreField = new TextField(seleccionado.getNombre());
            TextField precioField = new TextField(String.valueOf(seleccionado.getPrecio()));
            TextField stockField = new TextField(String.valueOf(seleccionado.getStock()));

            Button actualizarButton = new Button("Actualizar");
            actualizarButton.setOnAction(e -> {
                try {
                    seleccionado.setNombre(nombreField.getText());
                    seleccionado.setPrecio(Double.parseDouble(precioField.getText()));
                    seleccionado.setStock(Integer.parseInt(stockField.getText()));

                    gestor.actualizar(seleccionado);
                    actualizarTabla();
                    dialog.close();
                } catch (NumberFormatException ex) {
                    mostrarAlerta("Error", "Por favor, ingrese valores válidos.");
                }
            });

            VBox layout = new VBox(10);
            layout.setPadding(new Insets(10));
            layout.getChildren().addAll(
                new Label("Nombre:"), nombreField,
                new Label("Precio:"), precioField,
                new Label("Stock:"), stockField,
                actualizarButton
            );

            Scene scene = new Scene(layout, 300, 300);
            dialog.setScene(scene);
            dialog.showAndWait();
        } else {
            mostrarAlerta("Error", "Por favor, seleccione un producto para actualizar.");
        }
    }

    private void actualizarTabla() {
        tabla.getItems().setAll(gestor.leer());
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}