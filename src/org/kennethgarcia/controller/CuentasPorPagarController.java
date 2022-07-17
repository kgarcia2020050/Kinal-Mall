/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kennethgarcia.controller;

import com.jfoenix.controls.JFXDatePicker;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.SQLException;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.kennethgarcia.bean.Administracion;
import org.kennethgarcia.bean.CuentasPorPagar;
import org.kennethgarcia.bean.Proveedores;
import org.kennethgarcia.db.Conexion;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import org.kennethgarcia.report.GenerarReporte;
import org.kennethgarcia.system.Principal;

/**
 *
 * @author Kenneth Gerardo Garcia Lemus
 * @date 7/07/2021
 * @time 08:06:59
 */
public class CuentasPorPagarController implements Initializable {

    private Principal escenarioPrincipal;
    @FXML
    private JFXDatePicker dtFechaLimite;
    @FXML
    private TableView tblCuentasPagar;
    @FXML
    private ComboBox cmbEstadoPago;
    @FXML
    private ImageView imgRegresar;

    /*
    public void validarReal(String numero){
    String patron="^[0-9]+([.][0-9]{2}?)";
    Pattern pattern=Pattern.compile(patron);
    Matcher matcher=pattern.matcher(numero);
    return matcher.matches();    }
     */
    public boolean existeElementoSeleccionado() {
        return tblCuentasPagar.getSelectionModel().getSelectedItem() != null;
    }

    @FXML
    private void seleccionarElemento() {
        if (existeElementoSeleccionado()) {
            txtId.setText(String.valueOf(((CuentasPorPagar) tblCuentasPagar.getSelectionModel().getSelectedItem()).getId()));
            txtFactura.setText(((CuentasPorPagar) tblCuentasPagar.getSelectionModel().getSelectedItem()).getNumeroFactura());
            dtFechaLimite.setValue(((CuentasPorPagar) tblCuentasPagar.getSelectionModel().getSelectedItem()).getFechaLimitePago().toLocalDate());
            txtValorNeto.setText(String.valueOf(((CuentasPorPagar) tblCuentasPagar.getSelectionModel().getSelectedItem()).getValorNetoPago()));
            cmbAdministracion.getSelectionModel().select(buscarAdministracion(((CuentasPorPagar) tblCuentasPagar.getSelectionModel().getSelectedItem()).getCodigoAdministracion()));
            cmbProveedores.getSelectionModel().select(buscarProveedor(((CuentasPorPagar) tblCuentasPagar.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
            cmbEstadoPago.setValue(((CuentasPorPagar) tblCuentasPagar.getSelectionModel().getSelectedItem()).getEstadoPago());
        }

    }

    public void agregarCuentasPagar() {
        CuentasPorPagar cuentaPagar = new CuentasPorPagar();
        cuentaPagar.setNumeroFactura(txtFactura.getText());
        cuentaPagar.setFechaLimitePago(Date.valueOf(dtFechaLimite.getValue()));
        cuentaPagar.setValorNetoPago(new BigDecimal(txtValorNeto.getText()));
        cuentaPagar.setEstadoPago(cmbEstadoPago.getValue().toString());
        cuentaPagar.setCodigoAdministracion(((Administracion) cmbAdministracion.getSelectionModel().getSelectedItem()).getId());
        cuentaPagar.setCodigoProveedor(((Proveedores) cmbProveedores.getSelectionModel().getSelectedItem()).getId());

        PreparedStatement stmt = null;
        try {
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarCuentasPorPagar(?,?,?,?,?,?)}");
            stmt.setString(1, cuentaPagar.getNumeroFactura());
            stmt.setDate(2, cuentaPagar.getFechaLimitePago());
            stmt.setString(3, cuentaPagar.getEstadoPago());
            stmt.setBigDecimal(4, cuentaPagar.getValorNetoPago());
            stmt.setInt(5, cuentaPagar.getCodigoAdministracion());
            stmt.setInt(6, cuentaPagar.getCodigoProveedor());
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

    public void editarCuentasPagar() {
        CuentasPorPagar cuentaPagar = new CuentasPorPagar();
        cuentaPagar.setId(Integer.parseInt(txtId.getText()));
        cuentaPagar.setNumeroFactura(txtFactura.getText());
        cuentaPagar.setFechaLimitePago(Date.valueOf(dtFechaLimite.getValue()));
        cuentaPagar.setValorNetoPago(new BigDecimal(txtValorNeto.getText()));
        cuentaPagar.setEstadoPago(cmbEstadoPago.getValue().toString());
        cuentaPagar.setCodigoAdministracion(((Administracion) cmbAdministracion.getSelectionModel().getSelectedItem()).getId());
        cuentaPagar.setCodigoProveedor(((Proveedores) cmbProveedores.getSelectionModel().getSelectedItem()).getId());

        PreparedStatement stmt = null;
        try {
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarCuentasPorPagar(?,?,?,?,?,?,?)}");
            stmt.setInt(1, cuentaPagar.getId());
            stmt.setString(2, cuentaPagar.getNumeroFactura());
            stmt.setDate(3, cuentaPagar.getFechaLimitePago());
            stmt.setString(4, cuentaPagar.getEstadoPago());
            stmt.setBigDecimal(5, cuentaPagar.getValorNetoPago());
            stmt.setInt(6, cuentaPagar.getCodigoAdministracion());
            stmt.setInt(7, cuentaPagar.getCodigoProveedor());
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

    public void eliminarCuentasPorPagar() {
        CuentasPorPagar cuentasPagar = (CuentasPorPagar) tblCuentasPagar.getSelectionModel().getSelectedItem();
        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarCuentasPorPagar(?)}");
            pstmt.setInt(1, cuentasPagar.getId());
            pstmt.execute();
            cargarDatos();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private enum Operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO;
    }

    private Operaciones operacion = Operaciones.NINGUNO;

    private final String direccion = "/org/kennethgarcia/resources/image/";

    public Administracion buscarAdministracion(int id) {
        Administracion administracion = null;
        PreparedStatement stmt = null;
        ResultSet resultado = null;
        try {
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarAdministracion(?)}");
            stmt.setInt(1, id);
            resultado = stmt.executeQuery();

            while (resultado.next()) {
                administracion = new Administracion(
                        resultado.getInt("id"),
                        resultado.getString("direccion"),
                        resultado.getString("telefono")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                resultado.close();
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return administracion;
    }

    public Proveedores buscarProveedor(int id) {
        Proveedores proveedor = null;
        PreparedStatement stmt = null;
        ResultSet resultado = null;
        try {
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarProveedores(?)}");
            stmt.setInt(1, id);
            resultado = stmt.executeQuery();

            while (resultado.next()) {
                proveedor = new Proveedores(resultado.getInt("id"),
                        resultado.getString("nit"),
                        resultado.getString("servicioPrestado"),
                        resultado.getString("telefono"),
                        resultado.getString("direccion"),
                        resultado.getBigDecimal("saldoFavor"),
                        resultado.getBigDecimal("saldoContra"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                resultado.close();
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return proveedor;
    }

    @FXML
    private TextField txtId;
    @FXML
    private TextField txtFactura;
    @FXML
    private TextField txtValorNeto;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colFactura;
    @FXML
    private TableColumn colFecha;
    @FXML
    private TableColumn colEstadoPago;
    @FXML
    private TableColumn colValorNeto;
    @FXML
    private TableColumn colAdministracion;
    @FXML
    private TableColumn colProveedores;
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
    private ComboBox cmbAdministracion;
    @FXML
    private ComboBox cmbProveedores;

    private ObservableList<CuentasPorPagar> listaCuentasPagar;
    private ObservableList<Proveedores> listaProveedor;
    private ObservableList<Administracion> listaAdministracion;

    public void activarControles() {
        txtId.setEditable(false);
        txtId.setDisable(false);
        txtValorNeto.setEditable(true);
        txtFactura.setEditable(true);

        cmbAdministracion.setDisable(false);
        cmbProveedores.setDisable(false);
        cmbEstadoPago.setDisable(false);

        dtFechaLimite.setDisable(false);
    }

    public void desactivarControles() {
        txtId.setEditable(false);
        txtId.setDisable(false);
        txtValorNeto.setEditable(false);
        txtFactura.setEditable(false);

        cmbAdministracion.setDisable(true);
        cmbProveedores.setDisable(true);
        cmbEstadoPago.setDisable(true);

        dtFechaLimite.setDisable(true);
    }

    public void limpiarControles() {
        txtId.clear();
        txtValorNeto.clear();
        txtFactura.clear();

        cmbAdministracion.valueProperty().set(null);
        cmbProveedores.valueProperty().set(null);
        cmbEstadoPago.valueProperty().set(null);

        dtFechaLimite.getEditor().clear();
    }

    public void cargarDatos() {
        tblCuentasPagar.setItems(getCuentasPorPagar());
        colId.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, Integer>("id"));
        colFactura.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, String>("numeroFactura"));
        colFecha.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, Date>("fechaLimitePago"));
        colEstadoPago.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, String>("estadoPago"));
        colValorNeto.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, BigDecimal>("valorNetoPago"));
        colAdministracion.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, Integer>("codigoAdministracion"));
        colProveedores.setCellValueFactory(new PropertyValueFactory<CuentasPorPagar, Integer>("codigoProveedor"));
        cmbAdministracion.setItems(getAdministracion());
        cmbProveedores.setItems(getProveedores());

    }

    public ObservableList<Administracion> getAdministracion() {
        ArrayList<Administracion> listado = new ArrayList<>();
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarAdministracion()}");
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                listado.add(new Administracion(
                        resultado.getInt("id"),
                        resultado.getString("direccion"),
                        resultado.getString("telefono")));
            }
            resultado.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        listaAdministracion = FXCollections.observableArrayList(listado);
        return listaAdministracion;
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

    public ObservableList<CuentasPorPagar> getCuentasPorPagar() {
        ArrayList<CuentasPorPagar> cuenta = new ArrayList<>();
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarCuentasPorPagar()}");
            ResultSet resultado = stmt.executeQuery();

            while (resultado.next()) {
                cuenta.add(new CuentasPorPagar(resultado.getInt("id"),
                        resultado.getString("numeroFactura"),
                        resultado.getDate("fechaLimitePago"),
                        resultado.getString("estadoPago"),
                        resultado.getBigDecimal("valorNetoPago"),
                        resultado.getInt("codigoAdministracion"),
                        resultado.getInt("codigoProveedor")));
            }
            resultado.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        listaCuentasPagar = FXCollections.observableArrayList(cuenta);
        return listaCuentasPagar;
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        ObservableList<String> opciones = FXCollections.observableArrayList("PAGADO", "PENDIENTE");
        cmbEstadoPago.getItems().addAll(opciones);

        cargarDatos();
        dtFechaLimite.setEditable(false);
    }

    @FXML
    private void regresar(MouseEvent event) {
        escenarioPrincipal.escenaProveedores();
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
                tblCuentasPagar.setDisable(true);
                imgRegresar.setDisable(true);
                imgRegresar.setOpacity(0.15);
                operacion = Operaciones.GUARDAR;
                break;
            case GUARDAR:
                ArrayList<JFXDatePicker> fecha = new ArrayList<>();
                fecha.add(dtFechaLimite);
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtValorNeto);
                listaCampos.add(txtFactura);
                ArrayList<ComboBox> listaCombo = new ArrayList<>();
                listaCombo.add(cmbAdministracion);
                listaCombo.add(cmbProveedores);
                listaCombo.add(cmbEstadoPago);
                if (escenarioPrincipal.validar(listaCampos, listaCombo)) {
                    if (dtFechaLimite.getValue() != null) {
                        if (escenarioPrincipal.validarEntero(txtFactura.getText())) {
                            if (escenarioPrincipal.validarDecimal(txtValorNeto.getText())) {
                                agregarCuentasPagar();
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
                                tblCuentasPagar.setDisable(false);
                                imgRegresar.setDisable(false);
                                imgRegresar.setOpacity(1);
                                operacion = Operaciones.NINGUNO;
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("KINAL MALL");
                                alert.setHeaderText(null);
                                alert.setContentText("Ingresar solo numeros en campo Valor Neto.");
                                alert.show();
                            }
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("KINAL MALL");
                            alert.setHeaderText(null);
                            alert.setContentText("Numero de factura invalido.");
                            alert.show();
                        }

                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("KINAL MALL");
                        alert.setHeaderText(null);
                        alert.setContentText("Ingresar la fecha de limite de pago.");
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
                if (tblCuentasPagar.getSelectionModel().getSelectedItem() == null) {
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
                    tblCuentasPagar.setDisable(true);
                    imgRegresar.setDisable(true);
                    imgRegresar.setOpacity(0.15);
                    operacion = Operaciones.ACTUALIZAR;
                }
                break;
            case ACTUALIZAR:
                ArrayList<JFXDatePicker> fecha = new ArrayList<>();
                fecha.add(dtFechaLimite);
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtId);
                listaCampos.add(txtValorNeto);
                listaCampos.add(txtFactura);
                ArrayList<ComboBox> listaCombo = new ArrayList<>();
                listaCombo.add(cmbAdministracion);
                listaCombo.add(cmbProveedores);
                listaCombo.add(cmbEstadoPago);
                if (escenarioPrincipal.validar(listaCampos, listaCombo)) {
                    if ((dtFechaLimite.getValue() != null)) {
                        if (escenarioPrincipal.validarEntero(txtFactura.getText())) {
                            if (escenarioPrincipal.validarDecimal(txtValorNeto.getText())) {
                                editarCuentasPagar();
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
                                tblCuentasPagar.setDisable(false);
                                imgRegresar.setDisable(false);
                                imgRegresar.setOpacity(1);
                                operacion = Operaciones.NINGUNO;
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("KINAL MALL");
                                alert.setHeaderText(null);
                                alert.setContentText("Ingresar solo numeros en campo Valor Neto.");
                                alert.show();
                            }
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("KINAL MALL");
                            alert.setHeaderText(null);
                            alert.setContentText("Numero de factura invalido.");
                            alert.show();
                        }
                        
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("KINAL MALL");
                        alert.setHeaderText(null);
                        alert.setContentText("Ingresar la fecha limite de pago.");
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
                tblCuentasPagar.setDisable(false);

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
                btnModificar.setText("Modificar");
                imgEditar.setImage(new Image(direccion + "editar.png"));
                btnEliminar.setText("Eliminar");
                imgEliminar.setImage(new Image(direccion + "borrar-usuario.png"));
                tblCuentasPagar.setDisable(false);
                imgRegresar.setDisable(false);
                imgRegresar.setOpacity(1);
                operacion = Operaciones.NINGUNO;
                break;
            case NINGUNO:
                if (tblCuentasPagar.getSelectionModel().getSelectedItem() == null) {
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
                        eliminarCuentasPorPagar();
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
        GenerarReporte.getInstance().mostrarReporte("ReporteCuentasPorPagar.jasper", parametro, "Reporte de Cuentas por pagar.");

    }

}
