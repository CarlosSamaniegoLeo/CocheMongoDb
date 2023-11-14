package com.example.cochesmongo1;

import Util.AlertUtil;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class HelloController {

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnModificar;

    @FXML
    private Button btnNuevo;

    @FXML
    private ComboBox<String> cbTipo;

    @FXML
    private TableColumn<?, ?> colMarca;

    @FXML
    private TableColumn<?, ?> colMatricula;

    @FXML
    private TableColumn<?, ?> colModelo;

    @FXML
    private TableColumn<?, ?> colTipo;

    @FXML
    private TableView<Coche> tblCoches;

    @FXML
    private TextField txtMarca;

    @FXML
    private TextField txtMatricula;

    @FXML
    private TextField txtModelo;

    private Coche cocheSeleccionado;
    private MongoClient conexion;
    private MongoDatabase database;
    private MongoCollection<Document> cochesCollection;
    private OperacionesCoches operacionesCoches;

    @FXML
    void initialize() {
        tblCoches.getItems().clear();
        ObservableList<String> tiposCoches = FXCollections.observableArrayList(
                "Automóviles de Pasajeros",
                "SUV (Vehículo Utilitario Deportivo)",
                "Camionetas y Pickups",
                "Furgonetas",
                "Crossovers",
                "Vehículos Eléctricos (EV)",
                "Híbridos",
                "Vehículos Todo Terreno (ATV) y Cuatrimotos",
                "Motocicletas y Scooters",
                "Bicicletas Eléctricas",
                "Autobuses y Autocares",
                "Ciclomotores y Motonetas"
        );

        cbTipo.setItems(tiposCoches);

        try {
            conexion = ConnectionDB.conectar();

            if (conexion != null) {
                database = conexion.getDatabase("coches");
                cochesCollection = database.getCollection("datos");

                operacionesCoches = new OperacionesCoches(cochesCollection);

                cargarDatosCoches();
                configurarColumnas();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // No cerrar la conexión aquí
        }
    }

    @FXML
    void limpiar(ActionEvent event) {
        txtMatricula.setText("");
        txtModelo.setText("");
        txtMarca.setText("");
        cbTipo.getSelectionModel().clearSelection();
    }

    @FXML
    void eliminarCoche(ActionEvent event) {
        if (conexion != null) {
            try {
                String matricula = txtMatricula.getText();
                operacionesCoches.eliminarCoche(matricula);

                AlertUtil.mostrarCorrecto("Coche Eliminado");
                initialize();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void guardarCoche(ActionEvent event) {
        if (conexion != null) {
            try {
                String matricula = txtMatricula.getText();
                String marca = txtMarca.getText();
                String modelo = txtModelo.getText();
                String tipo = cbTipo.getValue().toString();

                operacionesCoches.guardarCoche(matricula, marca, modelo, tipo);

                AlertUtil.mostrarCorrecto("Coche Insertado");
                initialize();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void modificarCoche(ActionEvent event) {
        if (conexion != null) {
            try {
                String matricula = txtMatricula.getText();

                if (matricula.isEmpty()) {
                    AlertUtil.mostrarError("Por favor, ingresa una matrícula para modificar el coche.");
                    return;
                }

                String nuevaMarca = txtMarca.getText();
                String nuevoModelo = txtModelo.getText();
                String nuevoTipo = cbTipo.getValue().toString();

                operacionesCoches.modificarCoche(matricula, nuevaMarca, nuevoModelo, nuevoTipo);

                AlertUtil.mostrarCorrecto("Coche Modificado");
                initialize();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void seleccionarCoche(Event event) {
        cocheSeleccionado = tblCoches.getSelectionModel().getSelectedItem();
        cargarCoche(cocheSeleccionado);
    }

    private void cargarCoche(Coche coche) {
        if (coche != null) {
            txtMatricula.setText(coche.getMatricula());
            txtMarca.setText(coche.getMarca());
            txtModelo.setText(coche.getModelo());
            cbTipo.setValue(coche.getTipo());
        } else {
            // Puedes manejar el caso cuando coche es nulo
            // Por ejemplo, limpiar los campos de texto
            txtMatricula.clear();
            txtMarca.clear();
            txtModelo.clear();
            cbTipo.getSelectionModel().clearSelection();
        }
    }

    private void cargarDatosCoches() {
        List<Coche> almacenados = operacionesCoches.obtenerCoches();
        tblCoches.getItems().addAll(almacenados);
    }

    private void configurarColumnas() {
        colMatricula.setCellValueFactory(new PropertyValueFactory<>("matricula"));
        colMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
    }
}
