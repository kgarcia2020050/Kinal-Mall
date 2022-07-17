/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kennethgarcia.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.kennethgarcia.bean.Cargos;
import org.kennethgarcia.db.Conexion;
import org.kennethgarcia.report.GenerarReporte;
import org.kennethgarcia.system.Principal;

/**
 *
 * @author Kenneth Gerardo Garcia Lemus
 * @date 7/07/2021
 * @time 15:22:45
 */
public class CargosController implements Initializable {

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNombre;
    @FXML
    private TableView tblCargos;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colNombre;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnReporte;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgReporte;
    @FXML
    private ImageView imgRegresar;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        cargarDatos();
    }

    private ObservableList<Cargos> listaCargos;
    private Principal escenarioPrincipal;

    private enum Operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO;
    }

    private Operaciones operacion = Operaciones.NINGUNO;

    private final String direccion = "/org/kennethgarcia/resources/image/";

    public void cargarDatos() {
        tblCargos.setItems(getCargos());
        colId.setCellValueFactory(new PropertyValueFactory<Cargos, Integer>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Cargos, String>("nombre"));

    }

    public ObservableList<Cargos> getCargos() {
        ArrayList<Cargos> cargo = new ArrayList<>();
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarCargos()}");
            ResultSet resultado = stmt.executeQuery();

            while (resultado.next()) {
                cargo.add(new Cargos(resultado.getInt("id"),
                        resultado.getString("nombre")));
            }
            resultado.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        listaCargos = FXCollections.observableArrayList(cargo);
        return listaCargos;
    }

    public void activarControles() {
        txtId.setEditable(false);
        txtId.setDisable(false);
        txtNombre.setEditable(true);
    }

    public void desactivarControles() {
        txtId.setEditable(false);
        txtId.setDisable(false);
        txtNombre.setEditable(false);
    }

    public void limpiarControles() {
        txtId.clear();
        txtNombre.clear();
    }

    public boolean existeElementoSeleccionado() {
        return tblCargos.getSelectionModel().getSelectedItem() != null;
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public void agregarCargos() {
        Cargos cargo = new Cargos();
        cargo.setNombreCargo(txtNombre.getText());
        PreparedStatement stmt = null;
        try {
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarCargos(?)}");
            stmt.setString(1, cargo.getNombre());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void editarCargos() {
        Cargos cargo = new Cargos();
        cargo.setId(Integer.parseInt(txtId.getText()));
        cargo.setNombreCargo(txtNombre.getText());
        PreparedStatement stmt = null;
        try {
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarCargos(?,?)}");
            stmt.setInt(1, cargo.getId());
            stmt.setString(2, cargo.getNombre());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void eliminarCargos() {
        try {
            PreparedStatement stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarCargos(?)}");
            stmt.setInt(1, Integer.parseInt(txtId.getText()));
            stmt.execute();
            cargarDatos();

        } catch (SQLException e) {
            if (e.getErrorCode() == 1451) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("KINAL MALL");
                alert.setHeaderText(null);
                alert.setContentText("Antes de eliminar este cargo debe de eliminar sus empleados relacionados.");
                alert.show();
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void regresar(MouseEvent event) {
        escenarioPrincipal.escenaAdministracion();
    }

    @FXML
    private void nuevo(ActionEvent event) {
        switch (operacion) {
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                imgNuevo.setImage(new Image(direccion + "salvar.png"));
                btnModificar.setText("Cancelar");
                imgEditar.setImage(new Image(direccion + "boton-x.png"));
                btnEliminar.setDisable(true);
                imgEliminar.setOpacity(0.15);
                btnReporte.setDisable(true);
                imgReporte.setOpacity(0.15);
                tblCargos.setDisable(true);
                imgRegresar.setDisable(true);
                imgRegresar.setOpacity(0.15);
                operacion = Operaciones.GUARDAR;
                break;
            case GUARDAR:
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtNombre);
                ArrayList<ComboBox> listaCombo = new ArrayList<>();
                if (escenarioPrincipal.validar(listaCampos, listaCombo)) {
                    agregarCargos();
                    cargarDatos();
                    limpiarControles();
                    desactivarControles();
                    btnNuevo.setText("Nuevo");
                    imgNuevo.setImage(new Image(direccion + "agregar-usuario (1).png"));
                    btnModificar.setText("Modificar");
                    imgEditar.setImage(new Image(direccion + "editar.png"));
                    btnEliminar.setDisable(false);
                    imgEliminar.setOpacity(1);
                    btnReporte.setDisable(false);
                    imgReporte.setOpacity(1);
                    tblCargos.setDisable(false);
                    imgRegresar.setDisable(false);
                    imgRegresar.setOpacity(1);
                    operacion = Operaciones.NINGUNO;

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("KINAL MALL");
                    alert.setHeaderText(null);
                    alert.setContentText("Llenar el campo de nombre de cargo.");
                    alert.show();
                }
                break;
        }
    }

    @FXML
    private void modificar(ActionEvent event) {
        switch (operacion) {
            case NINGUNO:
                if (tblCargos.getSelectionModel().getSelectedItem() == null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("KINAL MALL");
                    alert.setHeaderText(null);
                    alert.setContentText("Seleccione un registro para continuar.");
                    alert.show();

                } else {
                    activarControles();
                    btnModificar.setText("Actualizar");
                    imgEditar.setImage(new Image(direccion + "salvar.png"));
                    btnEliminar.setText("Cancelar");
                    imgEliminar.setImage(new Image(direccion + "boton-x.png"));
                    imgNuevo.setOpacity(0.15);
                    imgReporte.setOpacity(0.15);
                    btnNuevo.setDisable(true);
                    btnReporte.setDisable(true);
                    tblCargos.setDisable(true);
                    imgRegresar.setDisable(true);
                    imgRegresar.setOpacity(0.15);
                    operacion = Operaciones.ACTUALIZAR;
                }
                break;
            case ACTUALIZAR:
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtId);
                listaCampos.add(txtNombre);
                ArrayList<ComboBox> listaCombo = new ArrayList<>();
                if (escenarioPrincipal.validar(listaCampos, listaCombo)) {
                    editarCargos();
                    limpiarControles();
                    cargarDatos();
                    desactivarControles();
                    btnModificar.setText("Modificar");
                    imgEditar.setImage(new Image(direccion + "editar.png"));
                    btnEliminar.setText("Eliminar");
                    imgEliminar.setImage(new Image(direccion + "borrar-usuario.png"));
                    btnNuevo.setDisable(false);
                    imgNuevo.setOpacity(1);
                    btnReporte.setDisable(false);
                    imgReporte.setOpacity(1);
                    tblCargos.setDisable(false);
                    imgRegresar.setDisable(false);
                    imgRegresar.setOpacity(1);
                    operacion = Operaciones.NINGUNO;
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("KINAL MALL");
                    alert.setHeaderText(null);
                    alert.setContentText("Llenar el campo de nombre de cargo.");
                    alert.show();
                }

                break;

            case GUARDAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                imgNuevo.setImage(new Image(direccion + "agregar-usuario (1).png"));
                btnModificar.setText("Modificar");
                imgEditar.setImage(new Image(direccion + "editar.png"));
                btnEliminar.setDisable(false);
                imgEliminar.setOpacity(1);
                btnReporte.setDisable(false);
                imgReporte.setOpacity(1);
                tblCargos.setDisable(false);
                imgRegresar.setDisable(false);
                imgRegresar.setOpacity(1);
                operacion = Operaciones.NINGUNO;
                break;

        }
    }

    @FXML
    private void eliminar(ActionEvent event) {
        switch (operacion) {
            case ACTUALIZAR:
                limpiarControles();
                desactivarControles();
                btnNuevo.setDisable(false);
                imgNuevo.setOpacity(1);
                btnReporte.setDisable(false);
                imgReporte.setOpacity(1);
                imgRegresar.setDisable(false);
                imgRegresar.setOpacity(1);
                btnModificar.setText("Modificar");
                imgEditar.setImage(new Image(direccion + "editar.png"));
                btnEliminar.setText("Eliminar");
                imgEliminar.setImage(new Image(direccion + "borrar-usuario.png"));
                tblCargos.setDisable(false);
                operacion = Operaciones.NINGUNO;
                break;
            case NINGUNO:
                if (tblCargos.getSelectionModel().getSelectedItem() == null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("KINAL MALL");
                    alert.setHeaderText(null);
                    alert.setContentText("Seleccione un registro para continuar.");
                    alert.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Advertencia");
                    alert.setHeaderText(null);
                    alert.setContentText("¿Está seguro de eliminar?");

                    Optional<ButtonType> respuesta = alert.showAndWait();
                    if (respuesta.get() == ButtonType.OK) {
                        eliminarCargos();
                        limpiarControles();
                        cargarDatos();
                        break;
                    }

                }
                break;
        }
    }

    @FXML
    private void reporte(ActionEvent event) {
        Map parametro = new HashMap();
        GenerarReporte.getInstance().mostrarReporte("Reporte Cargos.jasper", parametro, "Reporte de Cargos.");

    }

    @FXML
    private void seleccionarElemento() {
        if (existeElementoSeleccionado()) {
            txtId.setText(String.valueOf(((Cargos) tblCargos.getSelectionModel().getSelectedItem()).getId()));
            txtNombre.setText(((Cargos) tblCargos.getSelectionModel().getSelectedItem()).getNombre());
        }
    }

}
