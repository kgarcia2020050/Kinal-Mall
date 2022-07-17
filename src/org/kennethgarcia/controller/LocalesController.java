/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kennethgarcia.controller;

import com.jfoenix.controls.JFXTimePicker;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.kennethgarcia.bean.Locales;
import org.kennethgarcia.db.Conexion;
import org.kennethgarcia.report.GenerarReporte;
import org.kennethgarcia.system.Principal;

/**
 *
 * @author Kenneth Gerardo Garcia Lemus
 * @date 15/06/2021
 * @time 17:56:33
 */
public class LocalesController implements Initializable {

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtSaldoFavor;
    @FXML
    private TextField txtMesesPendientes;
    @FXML
    private TextField txtValorLocal;
    @FXML
    private TextField txtValorAdministracion;
    @FXML
    private TextField txtSaldoContra;
    @FXML
    private CheckBox cbxDisponibilidad;
    @FXML
    private TextField txtSaldoLiquido;
    @FXML
    private ImageView imgNuevo;
    @FXML
    private ImageView imgEditar;
    @FXML
    private ImageView imgEliminar;
    @FXML
    private ImageView imgReporte;

    private final String direccion = "/org/kennethgarcia/resources/image/";
    @FXML
    private TextField txtLocalesDisponibles;
    @FXML
    private ImageView imgRegresar;

    private enum Operaciones {
        NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO

    }
    private Operaciones operacion = Operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<Locales> listaLocales;

    public ObservableList<Locales> getLocales() {
        ArrayList<Locales> lista = new ArrayList<>();

        try {

            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarLocales}");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                lista.add(new Locales(rs.getInt("id"), rs.getBigDecimal("saldoFavor"), rs.getBigDecimal("saldoContra"),
                        rs.getInt("mesesPendientes"), rs.getBoolean("disponibilidad"), rs.getBigDecimal("valorLocal"),
                        rs.getBigDecimal("valorAdministracion")));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        listaLocales = FXCollections.observableArrayList(lista);
        return listaLocales;

    }

    private boolean disp = false;

    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnReporte;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colSaldoFavor;
    @FXML
    private TableColumn colSaldoContra;
    @FXML
    private TableColumn colValorLocal;
    @FXML
    private TableColumn colValorAdministracion;
    @FXML
    private TableColumn colMesesPendientes;
    @FXML
    private TableColumn colDisponibilidad;
    @FXML
    private TableView tblLocales;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        cargarDatos();
    }

    @FXML
    public void revisarDisponibilidad(ActionEvent event) {
        if (cbxDisponibilidad.isSelected()) {
            disp = true;
        } else {
            disp = false;
        }
    }

    public void cargarDatos() {
        tblLocales.setItems(getLocales());
        colId.setCellValueFactory(new PropertyValueFactory<Locales, Integer>("id"));
        colSaldoFavor.setCellValueFactory(new PropertyValueFactory<Locales, BigDecimal>("saldoFavor"));
        colSaldoContra.setCellValueFactory(new PropertyValueFactory<Locales, BigDecimal>("saldoContra"));
        colMesesPendientes.setCellValueFactory(new PropertyValueFactory<Locales, Integer>("mesesPendientes"));
        colDisponibilidad.setCellValueFactory(new PropertyValueFactory<Locales, Boolean>("disponibilidad"));
        colValorLocal.setCellValueFactory(new PropertyValueFactory<Locales, BigDecimal>("valorLocal"));
        colValorAdministracion.setCellValueFactory(new PropertyValueFactory<Locales, BigDecimal>("valorAdministracion"));
        getLocalesDisponibles();
    }

    public boolean validar() {
        return tblLocales.getSelectionModel().getSelectedItem() != null;

    }

    @FXML
    public void seleccionarElemento() {
        if (validar()) {
            BigDecimal saldoContra;
            BigDecimal saldoFavor;
            txtId.setText(String.valueOf(((Locales) tblLocales.getSelectionModel().getSelectedItem()).getId()));
            txtSaldoFavor.setText(String.valueOf(((Locales) tblLocales.getSelectionModel().getSelectedItem()).getSaldoFavor()));
            saldoFavor = ((Locales) tblLocales.getSelectionModel().getSelectedItem()).getSaldoFavor();
            txtSaldoContra.setText(String.valueOf(((Locales) tblLocales.getSelectionModel().getSelectedItem()).getSaldoContra()));
            saldoContra = ((Locales) tblLocales.getSelectionModel().getSelectedItem()).getSaldoContra();
            txtMesesPendientes.setText(String.valueOf(((Locales) tblLocales.getSelectionModel().getSelectedItem()).getMesesPendientes()));
            cbxDisponibilidad.setText(String.valueOf(((Locales) tblLocales.getSelectionModel().getSelectedItem()).isDisponibilidad()));
            cbxDisponibilidad.setSelected(((Locales) tblLocales.getSelectionModel().getSelectedItem()).isDisponibilidad());
            txtValorLocal.setText(String.valueOf(((Locales) tblLocales.getSelectionModel().getSelectedItem()).getValorLocal()));
            txtValorAdministracion.setText(String.valueOf(((Locales) tblLocales.getSelectionModel().getSelectedItem()).getValorAdministracion()));
            txtSaldoLiquido.setText(String.valueOf(saldoFavor.subtract(saldoContra)));
        }
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
                tblLocales.setDisable(true);
                imgRegresar.setDisable(true);
                imgRegresar.setOpacity(0.15);
                operacion = Operaciones.GUARDAR;
                break;
            case GUARDAR:
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtSaldoFavor);
                listaCampos.add(txtSaldoContra);
                //listaCampos.add(txtMesesPendientes);
                listaCampos.add(txtValorLocal);
                listaCampos.add(txtValorAdministracion);
                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                if (escenarioPrincipal.validar(listaCampos, listaComboBox)) {
                    if (escenarioPrincipal.validarDecimal(txtSaldoContra.getText())) {
                        if (escenarioPrincipal.validarDecimal(txtSaldoFavor.getText())) {
                            if (escenarioPrincipal.validarDecimal(txtValorAdministracion.getText())) {
                                if (escenarioPrincipal.validarDecimal(txtValorLocal.getText())) {
                                    //if (escenarioPrincipal.validarEntero(txtMesesPendientes.getText())) {
                                    agregarLocales();
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
                                    tblLocales.setDisable(false);
                                    imgRegresar.setDisable(false);
                                    imgRegresar.setOpacity(1);
                                    operacion = Operaciones.NINGUNO;
                                    /* } else {
                                        Alert alert = new Alert(Alert.AlertType.ERROR);
                                        alert.setTitle("KINAL MALL");
                                        alert.setHeaderText(null);
                                        alert.setContentText("Cantidad de meses invalida."
                                                + "\n Solo se debe agregar una cantidad entera."
                                                + "\n Ejemplo: 1, 2, 3, etc...");
                                        alert.show();
                                    }*/
                                } else {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("KINAL MALL");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Ingresar solo numeros en campo Valor local.");
                                    alert.show();
                                }
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("KINAL MALL");
                                alert.setHeaderText(null);
                                alert.setContentText("Ingresar solo numeros en campo Valor de administracion.");
                                alert.show();
                            }
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("KINAL MALL");
                            alert.setHeaderText(null);
                            alert.setContentText("Ingresar solo numeros en campo Saldo a favor.");
                            alert.show();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("KINAL MALL");
                        alert.setHeaderText(null);
                        alert.setContentText("Ingresar solo numeros en campo Saldo en contra.");
                        alert.show();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("KINAL MALL");
                    alert.setHeaderText(null);
                    alert.setContentText("Uno o varios campos se encuentran vacios.");
                    alert.show();
                }
                break;
        }
    }

    @FXML
    private void modificar(ActionEvent event) {
        switch (operacion) {
            case NINGUNO:
                if (tblLocales.getSelectionModel().getSelectedItem() == null) {
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
                    tblLocales.setDisable(true);
                    imgRegresar.setDisable(true);
                    imgRegresar.setOpacity(0.15);
                    operacion = Operaciones.ACTUALIZAR;
                }
                break;
            case ACTUALIZAR:
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtId);
                listaCampos.add(txtSaldoFavor);
                listaCampos.add(txtSaldoContra);
                //listaCampos.add(txtMesesPendientes);
                listaCampos.add(txtValorLocal);
                listaCampos.add(txtValorAdministracion);
                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                if (escenarioPrincipal.validar(listaCampos, listaComboBox)) {
                    if (escenarioPrincipal.validarDecimal(txtSaldoContra.getText())) {
                        if (escenarioPrincipal.validarDecimal(txtSaldoFavor.getText())) {
                            if (escenarioPrincipal.validarDecimal(txtValorAdministracion.getText())) {
                                if (escenarioPrincipal.validarDecimal(txtValorLocal.getText())) {
                                    //       if (escenarioPrincipal.validarEntero(txtMesesPendientes.getText())) {
                                    editarLocales();
                                    cargarDatos();
                                    limpiarTexto();
                                    deshabilitarTexto();
                                    btnModificar.setText("Modificar");
                                    imgEditar.setImage(new Image(direccion + "editar.png"));
                                    btnEliminar.setText("Eliminar");
                                    imgEliminar.setImage(new Image(direccion + "borrar-usuario.png"));
                                    btnNuevo.setDisable(false);
                                    imgNuevo.setOpacity(1);
                                    btnReporte.setDisable(false);
                                    imgReporte.setOpacity(1);
                                    tblLocales.setDisable(false);
                                    imgRegresar.setDisable(false);
                                    imgRegresar.setOpacity(1);
                                    operacion = Operaciones.NINGUNO;
                                    /*   } else {
                                        Alert alert = new Alert(Alert.AlertType.ERROR);
                                        alert.setTitle("KINAL MALL");
                                        alert.setHeaderText(null);
                                        alert.setContentText("Cantidad de meses invalida."
                                                + "\n Solo se debe agregar una cantidad entera."
                                                + "\n Ejemplo: 1, 2, 3, etc...");
                                        alert.show();
                                    }*/
                                } else {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("KINAL MALL");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Ingresar solo numeros en campo Valor local.");
                                    alert.show();
                                }
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("KINAL MALL");
                                alert.setHeaderText(null);
                                alert.setContentText("Ingresar solo numeros en campo Valor de administracion.");
                                alert.show();
                            }
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("KINAL MALL");
                            alert.setHeaderText(null);
                            alert.setContentText("Ingresar solo numeros en campo Saldo a favor.");
                            alert.show();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("KINAL MALL");
                        alert.setHeaderText(null);
                        alert.setContentText("Ingresar solo numeros en campo Saldo en contra.");
                        alert.show();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("KINAL MALL");
                    alert.setHeaderText(null);
                    alert.setContentText("Uno o varios campos se encuentran vacios.");
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
                tblLocales.setDisable(false);
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
                tblLocales.setDisable(false);
                imgRegresar.setDisable(false);
                imgRegresar.setOpacity(1);
                operacion = Operaciones.NINGUNO;
                break;
            case NINGUNO:
                if (tblLocales.getSelectionModel().getSelectedItem() == null) {
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
                        eliminarLocales();
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
        GenerarReporte.getInstance().mostrarReporte("ReportaLocales.jasper", parametro, "Reporte de Locales.");

    }

    private void agregarLocales() {
        Locales registro = new Locales();
        registro.setSaldoFavor(new BigDecimal(txtSaldoFavor.getText()));
        registro.setSaldoContra(new BigDecimal(txtSaldoContra.getText()));
        //  registro.setMesesPendientes(Integer.parseInt(txtMesesPendientes.getText()));
        registro.setDisponibilidad(cbxDisponibilidad.isSelected());
        registro.setValorLocal(new BigDecimal(txtValorLocal.getText()));
        registro.setValorAdministracion(new BigDecimal(txtValorAdministracion.getText()));
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarLocales(?,?,?,?,?,?)}");
            stmt.setBigDecimal(1, registro.getSaldoFavor());
            stmt.setBigDecimal(2, registro.getSaldoContra());
            stmt.setNull(3, registro.getMesesPendientes());
            stmt.setBoolean(4, registro.isDisponibilidad());
            stmt.setBigDecimal(5, registro.getValorLocal());
            stmt.setBigDecimal(6, registro.getValorAdministracion());
            stmt.execute();
            cargarDatos();
            limpiarTexto();
            deshabilitarTexto();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void eliminarLocales() {
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarLocales(?)}");
            stmt.setInt(1, Integer.parseInt(txtId.getText()));
            stmt.execute();
            cargarDatos();

        } catch (SQLException e) {
            if (e.getErrorCode() == 1451) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("KINAL MALL");
                alert.setHeaderText(null);
                alert.setContentText("Antes de eliminar este local debe de eliminar sus cuentas por cobrar relacionadas.");
                alert.show();
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editarLocales() {
        Locales registro = (Locales) tblLocales.getSelectionModel().getSelectedItem();
        registro.setId(Integer.parseInt(txtId.getText()));
        registro.setSaldoFavor(new BigDecimal(txtSaldoFavor.getText()));
        registro.setSaldoContra(new BigDecimal(txtSaldoContra.getText()));
        registro.setMesesPendientes(Integer.parseInt(txtMesesPendientes.getText()));
        registro.setDisponibilidad(cbxDisponibilidad.isSelected());
        registro.setValorLocal(new BigDecimal(txtValorLocal.getText()));
        registro.setValorAdministracion(new BigDecimal(txtValorAdministracion.getText()));
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarLocales(?,?,?,?,?,?,?)}");
            stmt.setInt(1, registro.getId());
            stmt.setBigDecimal(2, registro.getSaldoFavor());
            stmt.setBigDecimal(3, registro.getSaldoContra());
            stmt.setInt(4, registro.getMesesPendientes());
            stmt.setBoolean(5, registro.isDisponibilidad());
            stmt.setBigDecimal(6, registro.getValorLocal());
            stmt.setBigDecimal(7, registro.getValorAdministracion());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void limpiarTexto() {
        txtId.clear();
        txtSaldoFavor.clear();
        txtSaldoContra.clear();
        txtMesesPendientes.clear();
        cbxDisponibilidad.setSelected(false);
        txtValorLocal.clear();
        txtValorAdministracion.clear();
        txtSaldoLiquido.clear();
    }

    private void habilitarDatos() {
        txtSaldoFavor.setEditable(true);
        txtSaldoContra.setEditable(true);
        txtMesesPendientes.setEditable(false);
        cbxDisponibilidad.setSelected(false);
        txtValorLocal.setEditable(true);
        txtValorAdministracion.setEditable(true);
        cbxDisponibilidad.setDisable(false);
    }

    public void deshabilitarTexto() {
        txtSaldoFavor.setEditable(false);
        txtSaldoContra.setEditable(false);
        txtMesesPendientes.setEditable(false);
        cbxDisponibilidad.setSelected(false);
        cbxDisponibilidad.setDisable(true);
        txtValorLocal.setEditable(false);
        txtValorAdministracion.setEditable(false);
    }

    public ObservableList<Locales> getLocalesDisponibles() {
        ArrayList<Locales> lista = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ContarLocales()}");
            rs = stmt.executeQuery();
            while (rs.next()) {
                int disponibles = rs.getInt("disponibles");
                txtLocalesDisponibles.setText(String.valueOf("Hay " + disponibles + " locales disponibles."));
            }
            listaLocales = FXCollections.observableArrayList(lista);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                stmt.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return listaLocales;
    }

}
