/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kennethgarcia.controller;

import com.jfoenix.controls.JFXTimePicker;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.kennethgarcia.bean.Administracion;
import org.kennethgarcia.bean.Clientes;
import org.kennethgarcia.bean.CuentasPorCobrar;
import org.kennethgarcia.bean.Locales;
import org.kennethgarcia.db.Conexion;
import org.kennethgarcia.report.GenerarReporte;
import org.kennethgarcia.system.Principal;

/**
 *
 * @author Kenneth Gerardo Garcia Lemus
 * @date 30/06/2021
 * @time 15:51:06
 */
public class CuentasPorCobrarController implements Initializable {

    private final String direccion = "/org/kennethgarcia/resources/image/";
    @FXML
    private ComboBox cmbEstadoPago;
    @FXML
    private ImageView imgRegresar;

    private enum Operaciones {
        NUEVO, GUARDAR, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO

    }
    private Operaciones operacion = Operaciones.NINGUNO;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtValorNeto;
    @FXML
    private TextField txtNumeroFactura;
    @FXML
    private TableView tblCuentasCobrar;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colAnio;
    @FXML
    private TableColumn colMes;
    @FXML
    private TableColumn colValorNeto;
    @FXML
    private TableColumn colEstadoPago;
    @FXML
    private TableColumn colNoFactura;
    @FXML
    private TableColumn colAdministracion;
    @FXML
    private TableColumn colCliente;
    @FXML
    private TableColumn colLocal;
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

    private SpinnerValueFactory<Integer> valueFactoryAnio;
    @FXML
    private Spinner<Integer> spnAnio;
    @FXML
    private ComboBox cmbAdministracion;
    @FXML
    private ComboBox cmbCliente;
    @FXML
    private ComboBox cmbLocal;
    @FXML
    private Spinner<Integer> spnMes;
    private SpinnerValueFactory<Integer> valueFactoryMes;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        valueFactoryAnio = new SpinnerValueFactory.IntegerSpinnerValueFactory(2020, 2050, 2021);
        valueFactoryMes = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, 6);
        spnAnio.setValueFactory(valueFactoryAnio);
        spnMes.setValueFactory(valueFactoryMes);

        ObservableList<String> listaOpciones = FXCollections.observableArrayList("PAGADO", "PENDIENTE");
        cmbEstadoPago.getItems().addAll(listaOpciones);
        cargarDatos();

    }

    private ObservableList<CuentasPorCobrar> listaCuentasPorCobrar;

    public ObservableList<CuentasPorCobrar> getCuentasPorCobrar() {
        ArrayList<CuentasPorCobrar> listado = new ArrayList<>();
        try {
            PreparedStatement stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarCuentasPorCobrar()}");
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                listado.add(new CuentasPorCobrar(resultado.getInt("id"),
                        resultado.getString("numeroFactura"),
                        resultado.getInt("anio"),
                        resultado.getInt("mes"),
                        resultado.getBigDecimal("valorNetoPago"),
                        resultado.getString("estadoPago"),
                        resultado.getInt("codigoAdministracion"),
                        resultado.getInt("codigoCliente"),
                        resultado.getInt("codigoLocal")));
            }
            resultado.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        listaCuentasPorCobrar = FXCollections.observableArrayList(listado);
        return listaCuentasPorCobrar;
    }

    private ObservableList<Administracion> listaAdministracion;

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

    private ObservableList<Locales> listaLocales;

    public ObservableList<Locales> getLocales() {
        ArrayList<Locales> lista = new ArrayList<>();

        try {

            PreparedStatement stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarLocales}");
            ResultSet resultado = stmt.executeQuery();

            while (resultado.next()) {

                lista.add(new Locales(resultado.getInt("id"), resultado.getBigDecimal("saldoFavor"), resultado.getBigDecimal("saldoContra"),
                        resultado.getInt("mesesPendientes"), resultado.getBoolean("disponibilidad"), resultado.getBigDecimal("valorLocal"),
                        resultado.getBigDecimal("valorAdministracion")));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        listaLocales = FXCollections.observableArrayList(lista);
        return listaLocales;

    }
    private ObservableList<Clientes> listaClientes;

    public ObservableList<Clientes> getClientes() {
        ArrayList<Clientes> lista = new ArrayList<>();
        try {
            PreparedStatement stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarClientes()}");
            ResultSet resultado = stmt.executeQuery();

            while (resultado.next()) {
                lista.add(new Clientes(
                        resultado.getInt("id"), resultado.getString("nombres"), resultado.getString("apellidos"),
                        resultado.getString("telefono"), resultado.getString("direccion"),
                        resultado.getString("email"), resultado.getInt("codigoTipoCliente")
                )
                );
            }
            resultado.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        listaClientes = FXCollections.observableArrayList(lista);
        return listaClientes;
    }

    private Principal escenarioPrincipal;

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }

    @FXML
    private void regresar(MouseEvent event) {
        escenarioPrincipal.escenaClientes();
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
                tblCuentasCobrar.setDisable(true);
                imgRegresar.setDisable(true);
                imgRegresar.setOpacity(0.15);
                operacion = Operaciones.GUARDAR;
                break;
            case GUARDAR:
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtNumeroFactura);
                listaCampos.add(txtValorNeto);
                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                listaComboBox.add(cmbAdministracion);
                listaComboBox.add(cmbCliente);
                listaComboBox.add(cmbLocal);
                listaComboBox.add(cmbEstadoPago);
                if (escenarioPrincipal.validar(listaCampos, listaComboBox)) {
                    if (escenarioPrincipal.validarEntero(txtNumeroFactura.getText())) {
                        if (escenarioPrincipal.validarDecimal(txtValorNeto.getText())) {
                            agregarCuentasPorCobrar();
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
                            tblCuentasCobrar.setDisable(false);
                            imgRegresar.setDisable(false);
                            imgRegresar.setOpacity(1);
                            operacion = Operaciones.NINGUNO;
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("KINAL MALL");
                            alert.setHeaderText(null);
                            alert.setContentText("Ingresar solo numeros en campo Valor neto.");
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
                if (tblCuentasCobrar.getSelectionModel().getSelectedItem() == null) {
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
                    tblCuentasCobrar.setDisable(true);
                    imgRegresar.setDisable(true);
                    imgRegresar.setOpacity(0.15);
                    operacion = Operaciones.ACTUALIZAR;
                }
                break;
            case ACTUALIZAR:
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtId);
                listaCampos.add(txtNumeroFactura);
                listaCampos.add(txtValorNeto);

                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                listaComboBox.add(cmbAdministracion);
                listaComboBox.add(cmbCliente);
                listaComboBox.add(cmbLocal);
                listaComboBox.add(cmbEstadoPago);
                if (escenarioPrincipal.validar(listaCampos, listaComboBox)) {
                    if (escenarioPrincipal.validarEntero(txtNumeroFactura.getText())) {
                        if (escenarioPrincipal.validarDecimal(txtValorNeto.getText())) {
                            editarCuentasPorCobrar();
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
                            tblCuentasCobrar.setDisable(false);
                            imgRegresar.setDisable(false);
                            imgRegresar.setOpacity(1);
                            operacion = Operaciones.NINGUNO;
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("KINAL MALL");
                            alert.setHeaderText(null);
                            alert.setContentText("Ingresar solo numeros en campo Valor neto.");
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
                tblCuentasCobrar.setDisable(false);
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
                tblCuentasCobrar.setDisable(false);
                imgRegresar.setDisable(false);
                imgRegresar.setOpacity(1);
                operacion = Operaciones.NINGUNO;
                break;
            case NINGUNO:
                if (tblCuentasCobrar.getSelectionModel().getSelectedItem() == null) {

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
                        eliminarCuentasPorCobrar();
                        limpiarControles();
                        cargarDatos();
                        break;
                    }

                }
                break;
        }
    }

    public boolean existeElementoSeleccionado() {
        return tblCuentasCobrar.getSelectionModel().getSelectedItem() != null;

    }

    @FXML
    private void reporte(ActionEvent event) {
        Map parametro = new HashMap();
        GenerarReporte.getInstance().mostrarReporte("ReporteCuentasPorCobrar.jasper", parametro, "Reporte de Cuentas Por Cobrar.");

    }

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

    public Clientes buscarClientes(int id) {
        Clientes cliente = null;
        PreparedStatement stmt = null;
        ResultSet resultado = null;

        try {
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarClientes(?)}");
            stmt.setInt(1, id);
            resultado = stmt.executeQuery();

            while (resultado.next()) {
                cliente = new Clientes(
                        resultado.getInt("id"),
                        resultado.getString("nombres"),
                        resultado.getString("apellidos"),
                        resultado.getString("telefono"),
                        resultado.getString("direccion"),
                        resultado.getString("email"),
                        resultado.getInt("codigoTipoCliente")
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

        return cliente;
    }

    public Locales buscarLocales(int id) {
        Locales local = null;
        PreparedStatement stmt = null;
        ResultSet resultado = null;

        try {
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarLocales(?)}");
            stmt.setInt(1, id);
            resultado = stmt.executeQuery();
            while (resultado.next()) {
                local = new Locales();
                local.setId(resultado.getInt("id"));
                local.setSaldoFavor(resultado.getBigDecimal("saldoFavor"));
                local.setSaldoContra(resultado.getBigDecimal("saldoContra"));
                local.setMesesPendientes(resultado.getInt("mesesPendientes"));
                local.setDisponibilidad(resultado.getBoolean("disponibilidad"));
                local.setValorLocal(resultado.getBigDecimal("valorLocal"));
                local.setValorAdministracion(resultado.getBigDecimal("valorAdministracion"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                resultado.close();
                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return local;
    }

    @FXML
    private void seleccionarElemento() {
        if (existeElementoSeleccionado()) {
            txtId.setText(String.valueOf(((CuentasPorCobrar) tblCuentasCobrar.getSelectionModel().getSelectedItem()).getId()));
            txtNumeroFactura.setText(String.valueOf(((CuentasPorCobrar) tblCuentasCobrar.getSelectionModel().getSelectedItem()).getNumeroFactura()));
            txtValorNeto.setText(String.valueOf(((CuentasPorCobrar) tblCuentasCobrar.getSelectionModel().getSelectedItem()).getValorNetoPago()));
            spnAnio.getValueFactory().setValue(((CuentasPorCobrar) tblCuentasCobrar.getSelectionModel().getSelectedItem()).getAnio());
            spnMes.getValueFactory().setValue(((CuentasPorCobrar) tblCuentasCobrar.getSelectionModel().getSelectedItem()).getMes());

            cmbAdministracion.getSelectionModel().select(buscarAdministracion(((CuentasPorCobrar) tblCuentasCobrar.getSelectionModel().getSelectedItem()).getCodigoAdministracion()));

            cmbCliente.getSelectionModel().select(buscarClientes(((CuentasPorCobrar) tblCuentasCobrar.getSelectionModel().getSelectedItem()).getCodigoCliente()));
            cmbEstadoPago.setValue(((CuentasPorCobrar) tblCuentasCobrar.getSelectionModel().getSelectedItem()).getEstadoPago());

            cmbLocal.getSelectionModel().select(buscarLocales(((CuentasPorCobrar) tblCuentasCobrar.getSelectionModel().getSelectedItem()).getCodigoLocal()));
        }

    }

    public void activarControles() {
        txtId.setEditable(false);
        txtNumeroFactura.setEditable(true);
        txtValorNeto.setEditable(true);
        spnAnio.setDisable(false);
        spnMes.setDisable(false);
        cmbAdministracion.setDisable(false);
        cmbCliente.setDisable(false);
        cmbLocal.setDisable(false);
        cmbEstadoPago.setDisable(false);
    }

    public void desactivarControles() {
        txtId.setEditable(false);
        txtNumeroFactura.setEditable(false);
        txtValorNeto.setEditable(false);
        spnAnio.setDisable(true);
        spnMes.setDisable(true);
        cmbAdministracion.setDisable(true);
        cmbCliente.setDisable(true);
        cmbLocal.setDisable(true);
        cmbEstadoPago.setDisable(true);
    }

    public void limpiarControles() {
        txtId.clear();
        txtNumeroFactura.clear();
        txtValorNeto.clear();
        spnAnio.getValueFactory().setValue(2021);
        spnMes.getValueFactory().setValue(1);
        //spnAnio.setValueFactory(valueFactoryAnio);
        //spnMes.setValueFactory(valueFactoryMes);
        cmbAdministracion.valueProperty().set(null);
        cmbCliente.valueProperty().set(null);
        cmbLocal.valueProperty().set(null);
        cmbEstadoPago.valueProperty().set(null);
    }

    public void cargarDatos() {
        tblCuentasCobrar.setItems(getCuentasPorCobrar());
        colId.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("id"));
        colNoFactura.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, String>("numeroFactura"));
        colAnio.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("anio"));
        colMes.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("mes"));
        colValorNeto.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, BigDecimal>("valorNetoPago"));
        colEstadoPago.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, String>("estadoPago"));
        colAdministracion.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("codigoAdministracion"));
        colCliente.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("codigoCliente"));
        colLocal.setCellValueFactory(new PropertyValueFactory<CuentasPorCobrar, Integer>("codigoLocal"));
        cmbAdministracion.setItems(getAdministracion());
        cmbCliente.setItems(getClientes());
        cmbLocal.setItems(getLocales());
    }

    public void agregarCuentasPorCobrar() {
        CuentasPorCobrar cuentaCobrar = new CuentasPorCobrar();
        cuentaCobrar.setNumeroFactura(txtNumeroFactura.getText());
        cuentaCobrar.setAnio(spnAnio.getValue());
        cuentaCobrar.setMes(spnMes.getValue());
        cuentaCobrar.setValorNetoPago(new BigDecimal(txtValorNeto.getText()));
        cuentaCobrar.setEstadoPago(cmbEstadoPago.getValue().toString());
        cuentaCobrar.setCodigoAdministracion(((Administracion) cmbAdministracion.getSelectionModel().getSelectedItem()).getId());
        cuentaCobrar.setCodigoCliente(((Clientes) cmbCliente.getSelectionModel().getSelectedItem()).getId());
        cuentaCobrar.setCodigoLocal(((Locales) cmbLocal.getSelectionModel().getSelectedItem()).getId());

        PreparedStatement stmt = null;

        try {
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarCuentasPorCobrar(?,?,?,?,?,?,?,?)}");
            stmt.setString(1, cuentaCobrar.getNumeroFactura());
            stmt.setInt(2, cuentaCobrar.getAnio());
            stmt.setInt(3, cuentaCobrar.getMes());
            stmt.setBigDecimal(4, cuentaCobrar.getValorNetoPago());
            stmt.setString(5, cuentaCobrar.getEstadoPago());
            stmt.setInt(6, cuentaCobrar.getCodigoAdministracion());
            stmt.setInt(7, cuentaCobrar.getCodigoCliente());
            stmt.setInt(8, cuentaCobrar.getCodigoLocal());

            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void editarCuentasPorCobrar() {
        CuentasPorCobrar cuentaCobrar = new CuentasPorCobrar();
        cuentaCobrar.setId(Integer.parseInt(txtId.getText()));
        cuentaCobrar.setNumeroFactura(txtNumeroFactura.getText());
        cuentaCobrar.setAnio(spnAnio.getValue());
        cuentaCobrar.setMes(spnMes.getValue());
        cuentaCobrar.setValorNetoPago(new BigDecimal(txtValorNeto.getText()));
        cuentaCobrar.setEstadoPago(cmbEstadoPago.getValue().toString());
        cuentaCobrar.setCodigoAdministracion(((Administracion) cmbAdministracion.getSelectionModel().getSelectedItem()).getId());
        cuentaCobrar.setCodigoCliente(((Clientes) cmbCliente.getSelectionModel().getSelectedItem()).getId());
        cuentaCobrar.setCodigoLocal(((Locales) cmbLocal.getSelectionModel().getSelectedItem()).getId());

        PreparedStatement stmt = null;

        try {
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarCuentasPorCobrar(?,?,?,?,?,?,?,?,?)}");

            stmt.setInt(1, cuentaCobrar.getId());

            stmt.setString(2, cuentaCobrar.getNumeroFactura());
            stmt.setInt(3, cuentaCobrar.getAnio());
            stmt.setInt(4, cuentaCobrar.getMes());
            stmt.setBigDecimal(5, cuentaCobrar.getValorNetoPago());
            stmt.setString(6, cuentaCobrar.getEstadoPago());
            stmt.setInt(7, cuentaCobrar.getCodigoAdministracion());
            stmt.setInt(8, cuentaCobrar.getCodigoCliente());
            stmt.setInt(9, cuentaCobrar.getCodigoLocal());

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

    public void eliminarCuentasPorCobrar() {
        CuentasPorCobrar cuentasCobrar = (CuentasPorCobrar) tblCuentasCobrar.getSelectionModel().getSelectedItem();
        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarCuentasPorCobrar(?)}");
            pstmt.setInt(1, cuentasCobrar.getId());
            pstmt.execute();
            cargarDatos();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
