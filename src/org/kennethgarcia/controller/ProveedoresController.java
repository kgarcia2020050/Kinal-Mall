/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kennethgarcia.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.util.ArrayList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.sql.ResultSet;
import javafx.scene.control.TextField;
import java.sql.SQLException;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.cell.PropertyValueFactory;
import org.kennethgarcia.bean.Proveedores;
import org.kennethgarcia.db.Conexion;
import org.kennethgarcia.report.GenerarReporte;
import org.kennethgarcia.system.Principal;

/**
 *
 * @author Kenneth Gerardo Garcia Lemus
 * @date 7/07/2021
 * @time 08:07:26
 */
public class ProveedoresController implements Initializable {

    @FXML
    private TextField txtSaldoLiquido;
    @FXML
    private ImageView imgCuentasPagar;
    @FXML
    private ImageView imgRegresar;

    private enum Operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO;
    }

    private Operaciones operacion = Operaciones.NINGUNO;

    private final String direccion = "/org/kennethgarcia/resources/image/";

    private Principal escenarioPrincipal;
    public ObservableList<Proveedores> listaProveedor;

    @FXML
    private Button btnCuentasPagar;

    @FXML
    private TableView tblProveedores;

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    public boolean validarNit(String nit) {
        String patron = "^[0-9]{6,}[-]?[0-9]{1}$";
        Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher(nit);
        return matcher.matches();
    }

    public boolean existeElementoSeleccionado() {
        return tblProveedores.getSelectionModel().getSelectedItem() != null;
    }

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNit;
    @FXML
    private TextField txtServicio;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colNit;
    @FXML
    private TableColumn colServicio;
    @FXML
    private TableColumn colTelefono;
    @FXML
    private TableColumn colDireccion;
    @FXML
    private TableColumn colSaldoFavor;
    @FXML
    private TableColumn colSaldoContra;
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
    private TextField txtTelefono;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtSaldoFavor;
    @FXML
    private TextField txtSaldoContra;

    @FXML
    private void regresar(MouseEvent event) {
        if (escenarioPrincipal.getUsuario().getRol() == 1) {
            escenarioPrincipal.menuPrincipal();
        } else {
            escenarioPrincipal.menuUsuario();
        }
    }

    public ObservableList<Proveedores> getProveedores() {
        ArrayList<Proveedores> lista = new ArrayList<>();
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarProveedores()}");
            ResultSet resultado = stmt.executeQuery();

            while (resultado.next()) {
                lista.add(new Proveedores(resultado.getInt("id"),
                        resultado.getString("nit"),
                        resultado.getString("servicioPrestado"),
                        resultado.getString("telefono"),
                        resultado.getString("direccion"),
                        resultado.getBigDecimal("saldoFavor"),
                        resultado.getBigDecimal("saldoContra")));
            }
            resultado.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        listaProveedor = FXCollections.observableArrayList(lista);
        return listaProveedor;
    }

    public void cargarDatos() {
        tblProveedores.setItems(getProveedores());
        colId.setCellValueFactory(new PropertyValueFactory<Proveedores, Integer>("id"));
        colNit.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("nit"));
        colServicio.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("servicioPrestado"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("telefono"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Proveedores, String>("direccion"));
        colSaldoFavor.setCellValueFactory(new PropertyValueFactory<Proveedores, BigDecimal>("saldoFavor"));
        colSaldoContra.setCellValueFactory(new PropertyValueFactory<Proveedores, BigDecimal>("saldoContra"));
    }

    public void agregarProveedor() {
        Proveedores proveedor = new Proveedores();
        proveedor.setNit(txtNit.getText());
        proveedor.setServicioPrestado(txtServicio.getText());
        proveedor.setTelefono(txtTelefono.getText());
        proveedor.setDireccion(txtDireccion.getText());
        proveedor.setSaldoFavor(new BigDecimal(txtSaldoFavor.getText()));
        proveedor.setSaldoContra(new BigDecimal(txtSaldoContra.getText()));
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarProveedores(?,?,?,?,?,?)}");
            stmt.setString(1, proveedor.getNit());
            stmt.setString(2, proveedor.getServicioPrestado());
            stmt.setString(3, proveedor.getTelefono());
            stmt.setString(4, proveedor.getDireccion());
            stmt.setBigDecimal(5, proveedor.getSaldoFavor());
            stmt.setBigDecimal(6, proveedor.getSaldoContra());
            stmt.execute();
            cargarDatos();
            limpiarTexto();
            deshabilitarTexto();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editarProveedor() {
        Proveedores proveedores = (Proveedores) tblProveedores.getSelectionModel().getSelectedItem();
        Proveedores proveedor = new Proveedores();
        proveedor.setId(Integer.parseInt(txtId.getText()));
        proveedor.setNit(txtNit.getText());
        proveedor.setServicioPrestado(txtServicio.getText());
        proveedor.setTelefono(txtTelefono.getText());
        proveedor.setDireccion(txtDireccion.getText());
        proveedor.setSaldoFavor(new BigDecimal(txtSaldoFavor.getText()));
        proveedor.setSaldoContra(new BigDecimal(txtSaldoContra.getText()));
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarProveedores(?,?,?,?,?,?,?)}");
            stmt.setInt(1, proveedor.getId());
            stmt.setString(2, proveedor.getNit());
            stmt.setString(3, proveedor.getServicioPrestado());
            stmt.setString(4, proveedor.getTelefono());
            stmt.setString(5, proveedor.getDireccion());
            stmt.setBigDecimal(6, proveedor.getSaldoFavor());
            stmt.setBigDecimal(7, proveedor.getSaldoContra());
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void eliminarProveedor() {

        Proveedores proveedor = (Proveedores) tblProveedores.getSelectionModel().getSelectedItem();
        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarProveedores(?)}");
            pstmt.setInt(1, proveedor.getId());
            pstmt.execute();
            cargarDatos();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1451) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("KINAL MALL");
                alert.setHeaderText(null);
                alert.setContentText("Antes de eliminar este proveedor debe de eliminar sus cuentas por pagar relacionadas.");
                alert.show();
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                btnCuentasPagar.setDisable(true);
                imgCuentasPagar.setOpacity(0.15);
                tblProveedores.setDisable(true);
                imgRegresar.setDisable(true);
                imgRegresar.setOpacity(0.15);
                operacion = Operaciones.GUARDAR;
                break;
            case GUARDAR:
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtNit);
                listaCampos.add(txtServicio);
                listaCampos.add(txtTelefono);
                listaCampos.add(txtDireccion);
                listaCampos.add(txtSaldoFavor);
                listaCampos.add(txtSaldoContra);
                ArrayList<ComboBox> listaCombo = new ArrayList<>();
                if (escenarioPrincipal.validar(listaCampos, listaCombo)) {
                    if (escenarioPrincipal.validarTelefono(txtTelefono.getText())) {
                        if (escenarioPrincipal.validarDecimal(txtSaldoFavor.getText())) {
                            if (escenarioPrincipal.validarDecimal(txtSaldoContra.getText())) {
                                if (validarNit(txtNit.getText())) {
                                    if (escenarioPrincipal.validarString(txtServicio.getText())) {
                                        agregarProveedor();
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
                                        btnCuentasPagar.setDisable(false);
                                        imgCuentasPagar.setOpacity(1);
                                        tblProveedores.setDisable(false);
                                        imgRegresar.setDisable(false);
                                        imgRegresar.setOpacity(1);
                                        operacion = Operaciones.NINGUNO;
                                    } else {
                                        Alert alert = new Alert(Alert.AlertType.ERROR);
                                        alert.setTitle("KINAL MALL");
                                        alert.setHeaderText(null);
                                        alert.setContentText("Debe ingresar solo letras en el campo Servicio.");
                                        alert.show();
                                    }

                                } else {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("KINAL MALL");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Formato de NIT invalido."
                                            + "\n (Ejemplo: 123456-7 o 1234567-8)");
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
                            alert.setContentText("Ingresar solo numeros en campo Saldo a favor.");
                            alert.show();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("KINAL MALL");
                        alert.setHeaderText(null);
                        alert.setContentText("Numero de telefono invalido."
                                + "\n Solo se deben ingresar numeros en un formato de 12345678.");
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
                if (tblProveedores.getSelectionModel().getSelectedItem() == null) {
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
                    btnCuentasPagar.setDisable(true);
                    imgCuentasPagar.setOpacity(0.15);
                    tblProveedores.setDisable(true);
                    imgRegresar.setDisable(true);
                    imgRegresar.setOpacity(0.15);
                    operacion = Operaciones.ACTUALIZAR;
                }
                break;
            case ACTUALIZAR:
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtId);
                listaCampos.add(txtNit);
                listaCampos.add(txtServicio);
                listaCampos.add(txtTelefono);
                listaCampos.add(txtDireccion);
                listaCampos.add(txtSaldoFavor);
                listaCampos.add(txtSaldoContra);
                ArrayList<ComboBox> listaCombo = new ArrayList<>();
                if (escenarioPrincipal.validar(listaCampos, listaCombo)) {
                    if (escenarioPrincipal.validarTelefono(txtTelefono.getText())) {
                        if (escenarioPrincipal.validarDecimal(txtSaldoFavor.getText())) {
                            if (escenarioPrincipal.validarDecimal(txtSaldoContra.getText())) {
                                if (validarNit(txtNit.getText())) {
                                    if (escenarioPrincipal.validarString(txtServicio.getText())) {
                                        editarProveedor();
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
                                        btnCuentasPagar.setDisable(false);
                                        imgCuentasPagar.setOpacity(1);
                                        tblProveedores.setDisable(false);
                                        imgRegresar.setDisable(false);
                                        imgRegresar.setOpacity(1);
                                        operacion = Operaciones.NINGUNO;
                                    } else {
                                        Alert alert = new Alert(Alert.AlertType.ERROR);
                                        alert.setTitle("KINAL MALL");
                                        alert.setHeaderText(null);
                                        alert.setContentText("Ingresar solo letras en el campo Servicio.");
                                        alert.show();
                                    }
                                } else {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("KINAL MALL");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Formato de NIT invalido."
                                            + "\n (Ejemplo: 123456-7 o 1234567-8)");
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
                            alert.setContentText("Solo ingresar numeros en campo Saldo a favor.");
                            alert.show();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("KINAL MALL");
                        alert.setHeaderText(null);
                        alert.setContentText("Numero de telefono invalido."
                                + "\n Solo se deben ingresar numeros en un formato de 12345678..");
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
                btnCuentasPagar.setDisable(false);
                imgCuentasPagar.setOpacity(1);
                tblProveedores.setDisable(false);
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
                btnCuentasPagar.setDisable(false);
                imgCuentasPagar.setOpacity(1);
                btnModificar.setText("Modificar");
                imgEditar.setImage(new Image(direccion + "editar.png"));
                btnEliminar.setText("Eliminar");
                imgEliminar.setImage(new Image(direccion + "borrar-usuario.png"));
                tblProveedores.setDisable(false);
                imgRegresar.setDisable(false);
                imgRegresar.setOpacity(1);
                operacion = Operaciones.NINGUNO;
                break;
            case NINGUNO:
                if (tblProveedores.getSelectionModel().getSelectedItem() == null) {
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
                        eliminarProveedor();
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
        GenerarReporte.getInstance().mostrarReporte("ReporteProveedores.jasper", parametro, "Reporte de Proveedores.");

    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        cargarDatos();
        /*String nit=txtNit.getText();
        if(validarNit(nit)){
        nit=nit.replace("-"," ");
        }*/
    }

    @FXML
    private void mostrarVistaCuentasPagar(ActionEvent event) {
        escenarioPrincipal.escenaCuentasPorPagar();
    }

    @FXML
    private void seleccionarElemento() {
        if (existeElementoSeleccionado()) {
            BigDecimal saldoContra;
            BigDecimal saldoFavor;
            txtId.setText(String.valueOf(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getId()));
            txtNit.setText(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getNit());
            txtServicio.setText(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getServicioPrestado());
            txtTelefono.setText(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getTelefono());
            txtDireccion.setText(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getDireccion());
            txtSaldoFavor.setText(String.valueOf(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getSaldoFavor()));
            saldoFavor = ((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getSaldoFavor();
            txtSaldoContra.setText(String.valueOf(((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getSaldoContra()));
            saldoContra = ((Proveedores) tblProveedores.getSelectionModel().getSelectedItem()).getSaldoContra();
            txtSaldoLiquido.setText(String.valueOf(saldoFavor.subtract(saldoContra)));
        }
    }

    public void habilitarDatos() {
        txtId.setEditable(false);
        txtId.setDisable(false);
        txtNit.setEditable(true);
        txtServicio.setEditable(true);
        txtTelefono.setEditable(true);
        txtDireccion.setEditable(true);
        txtSaldoFavor.setEditable(true);
        txtSaldoContra.setEditable(true);
    }

    public void deshabilitarTexto() {
        txtId.setEditable(false);
        txtId.setDisable(false);
        txtNit.setEditable(false);
        txtServicio.setEditable(false);
        txtTelefono.setEditable(false);
        txtDireccion.setEditable(false);
        txtSaldoFavor.setEditable(false);
        txtSaldoContra.setEditable(false);
    }

    public void limpiarTexto() {
        txtId.clear();
        txtNit.clear();
        txtServicio.clear();
        txtTelefono.clear();
        txtDireccion.clear();
        txtSaldoFavor.clear();
        txtSaldoContra.clear();
        txtSaldoLiquido.clear();
    }

}
