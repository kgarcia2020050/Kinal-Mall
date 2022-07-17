/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kennethgarcia.controller;

import com.jfoenix.controls.JFXTimePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.kennethgarcia.bean.TipoCliente;
import org.kennethgarcia.db.Conexion;
import org.kennethgarcia.system.Principal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.kennethgarcia.report.GenerarReporte;

/**
 *
 * @author Kenneth Gerardo Garcia Lemus
 * @date 14/06/2021
 * @time 10:44:32
 */
public class TipoClienteController implements Initializable {

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

    private enum Operaciones {
        NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO

    }
    private final String direccion = "/org/kennethgarcia/resources/image/";

    public boolean existeElementoSeleccionado() {
        return tblTipoCliente.getSelectionModel().getSelectedItem() != null;
    }
    private Operaciones operacion = Operaciones.NINGUNO;

    private Principal escenarioPrincipal;
    private ObservableList<TipoCliente> listaTipoCliente;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnReporte;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private TableView tblTipoCliente;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colDescripcion;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        cargarDatos();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @FXML
    private void regresar(MouseEvent event) {
        escenarioPrincipal.escenaAdministracion();
    }

    @FXML
    private void nuevo(ActionEvent event) {

        switch (operacion) {
            case NINGUNO:
                habilitarDatos();
                limpiarTexto();
                btnNuevo.setText("Guardar");
                imgNuevo.setImage(new Image(direccion + "salvar.png"));
                btnModificar.setText("Cancelar");
                imgEditar.setImage(new Image(direccion + "boton-x.png"));
                btnEliminar.setDisable(true);
                imgEliminar.setOpacity(0.15);
                btnReporte.setDisable(true);
                imgReporte.setOpacity(0.15);
                tblTipoCliente.setDisable(true);
                imgRegresar.setDisable(true);
                imgRegresar.setOpacity(0.15);
                operacion = Operaciones.GUARDAR;
                break;
            case GUARDAR:
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtDescripcion);
                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                if (escenarioPrincipal.validar(listaCampos, listaComboBox)) {
                    if (escenarioPrincipal.validarString(txtDescripcion.getText())) {
                        agregarTipoCliente();
                        cargarDatos();
                        limpiarTexto();
                        deshabilitarTexto();
                        btnNuevo.setText("Nuevo");
                        imgNuevo.setImage(new Image(direccion + "agregar-usuario (1).png"));
                        btnModificar.setText("Modificar");
                        imgEditar.setImage(new Image(direccion + "editar.png"));
                        btnEliminar.setDisable(false);
                        imgEliminar.setOpacity(1);
                        btnReporte.setDisable(false);
                        imgReporte.setOpacity(1);
                        tblTipoCliente.setDisable(false);
                        imgRegresar.setDisable(false);
                        imgRegresar.setOpacity(1);
                        operacion = Operaciones.NINGUNO;

                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("KINAL MALL");
                        alert.setHeaderText(null);
                        alert.setContentText("Ingresar solo letras en campo Descripcion.");
                        alert.show();
                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("KINAL MALL");
                    alert.setHeaderText(null);
                    alert.setContentText("El campo descripcion se encuentra vacio.");
                    alert.show();
                }
                break;
        }

    }

    @FXML
    private void modificar(ActionEvent event) {

        switch (operacion) {
            case NINGUNO:
                if (tblTipoCliente.getSelectionModel().getSelectedItem() == null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("KINAL MALL");
                    alert.setHeaderText(null);
                    alert.setContentText("Seleccione un registro para continuar.");
                    alert.show();

                } else {
                    habilitarDatos();
                    btnModificar.setText("Actualizar");
                    imgEditar.setImage(new Image(direccion + "salvar.png"));
                    btnEliminar.setText("Cancelar");
                    imgEliminar.setImage(new Image(direccion + "boton-x.png"));
                    imgNuevo.setOpacity(0.15);
                    imgReporte.setOpacity(0.15);
                    btnNuevo.setDisable(true);
                    btnReporte.setDisable(true);
                    tblTipoCliente.setDisable(true);
                    imgRegresar.setDisable(true);
                    imgRegresar.setOpacity(0.15);
                    operacion = Operaciones.ACTUALIZAR;
                }
                break;
            case ACTUALIZAR:
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtId);
                listaCampos.add(txtDescripcion);
                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                if (escenarioPrincipal.validar(listaCampos, listaComboBox)) {
                    if(escenarioPrincipal.validarString(txtDescripcion.getText())){
                    editarTipoCliente();
                    limpiarTexto();
                    cargarDatos();
                    deshabilitarTexto();
                    btnModificar.setText("Modificar");
                    imgEditar.setImage(new Image(direccion + "editar.png"));
                    btnEliminar.setText("Eliminar");
                    imgEliminar.setImage(new Image(direccion + "borrar-usuario.png"));
                    btnNuevo.setDisable(false);
                    imgNuevo.setOpacity(1);
                    btnReporte.setDisable(false);
                    imgReporte.setOpacity(1);
                    tblTipoCliente.setDisable(false);
                    imgRegresar.setDisable(false);
                    imgRegresar.setOpacity(1);
                    operacion = Operaciones.NINGUNO;
                    }else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("KINAL MALL");
                    alert.setHeaderText(null);
                    alert.setContentText("Ingresar solo letras en campo Descripcion.");
                    alert.show();
                }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("KINAL MALL");
                    alert.setHeaderText(null);
                    alert.setContentText("El campo descripcion se encuentra vacio.");
                    alert.show();
                }
                break;
            case GUARDAR:
                limpiarTexto();
                deshabilitarTexto();
                btnNuevo.setText("Nuevo");
                imgNuevo.setImage(new Image(direccion + "agregar-usuario (1).png"));
                btnModificar.setText("Modificar");
                imgEditar.setImage(new Image(direccion + "editar.png"));
                btnEliminar.setDisable(false);
                imgEliminar.setOpacity(1);
                btnReporte.setDisable(false);
                imgReporte.setOpacity(1);
                tblTipoCliente.setDisable(false);
                imgRegresar.setDisable(false);
                imgRegresar.setOpacity(1);
                operacion = Operaciones.NINGUNO;
        }

    }

    @FXML
    private void eliminar(ActionEvent event) {

        switch (operacion) {
            case ACTUALIZAR:
                limpiarTexto();
                deshabilitarTexto();
                btnNuevo.setDisable(false);
                imgNuevo.setOpacity(1);
                btnReporte.setDisable(false);
                imgReporte.setOpacity(1);
                btnModificar.setText("Modificar");
                imgEditar.setImage(new Image(direccion + "editar.png"));
                btnEliminar.setText("Eliminar");
                imgEliminar.setImage(new Image(direccion + "borrar-usuario.png"));
                tblTipoCliente.setDisable(false);
                imgRegresar.setDisable(false);
                imgRegresar.setOpacity(1);
                operacion = Operaciones.NINGUNO;
                break;
            case NINGUNO:
                if (tblTipoCliente.getSelectionModel().getSelectedItem() == null) {
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
                        eliminarTipoCliente();
                        limpiarTexto();
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
        GenerarReporte.getInstance().mostrarReporte("Reporte Tipo Cliente.jasper", parametro, "Reporte de Tipos de Cliente.");

    }

    private void agregarTipoCliente() {

        TipoCliente registro = new TipoCliente();
        registro.setDescripcion(txtDescripcion.getText());

        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarTipoCliente(?)}");
            stmt.setString(1, registro.getDescripcion());
            stmt.execute();
            cargarDatos();
            limpiarTexto();
            deshabilitarTexto();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void editarTipoCliente() {
        TipoCliente registro = (TipoCliente) tblTipoCliente.getSelectionModel().getSelectedItem();
        registro.setId(Integer.parseInt(txtId.getText()));
        registro.setDescripcion(txtDescripcion.getText());
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarTipoCliente(?,?)}");
            stmt.setInt(1, registro.getId());
            stmt.setString(2, registro.getDescripcion());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminarTipoCliente() {
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarTipoCliente(?)}");
            stmt.setInt(1, Integer.parseInt(txtId.getText()));
            stmt.execute();
            cargarDatos();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1451) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("KINAL MALL");
                alert.setHeaderText(null);
                alert.setContentText("Antes de eliminar este tipo de cliente debe de eliminar todos sus clientes relacionados.");
                alert.show();
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean validar() {
        return tblTipoCliente.getSelectionModel().getSelectedItem() != null;
    }

    @FXML
    public void seleccionarElemento() {
        if (validar()) {
            txtId.setText(String.valueOf(((TipoCliente) tblTipoCliente.getSelectionModel().getSelectedItem()).getId()));
            txtDescripcion.setText(((TipoCliente) tblTipoCliente.getSelectionModel().getSelectedItem()).getDescripcion());
        }
    }

    public ObservableList<TipoCliente> getTipoCliente() {
        ArrayList<TipoCliente> lista = new ArrayList<>();

        try {

            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarTipoCliente}");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                lista.add(new TipoCliente(rs.getInt("id"), rs.getString("descripcion")));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        listaTipoCliente = FXCollections.observableArrayList(lista);
        return listaTipoCliente;

    }

    public void cargarDatos() {
        tblTipoCliente.setItems(getTipoCliente());
        colId.setCellValueFactory(new PropertyValueFactory<TipoCliente, Integer>("id"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TipoCliente, String>("descripcion"));
    }

    public void limpiarTexto() {
        txtId.clear();
        txtDescripcion.clear();

    }

    public void deshabilitarTexto() {
        txtDescripcion.setEditable(false);
    }

    private void habilitarDatos() {
        txtDescripcion.setEditable(true);
    }

}
