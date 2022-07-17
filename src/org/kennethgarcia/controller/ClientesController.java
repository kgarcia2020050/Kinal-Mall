/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kennethgarcia.controller;

import com.jfoenix.controls.JFXTimePicker;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.kennethgarcia.db.Conexion;
import org.kennethgarcia.bean.Clientes;
import org.kennethgarcia.bean.TipoCliente;
import org.kennethgarcia.report.GenerarReporte;
import org.kennethgarcia.system.Principal;

/**
 *
 * @author Kenneth Gerardo Garcia Lemus
 * @date 12/06/2021
 * @time 11:59:47
 */
public class ClientesController implements Initializable {

    private final String direccion = "/org/kennethgarcia/resources/image/";
    private Principal escenarioPrincipal;
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
    @FXML
    private Button btnCuentasCobrar;
    @FXML
    private ImageView imgCuentasPorCobrar;

    @FXML
    private void vistaCuentasCobrar(ActionEvent event) {
        escenarioPrincipal.escenaCuentasPorCobrar();
    }

    private enum Operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO;
    }

    private Operaciones operacion = Operaciones.NINGUNO;

    private ObservableList<Clientes> listaClientes;
    private ObservableList<TipoCliente> listaTipoCliente;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarDatos();
    }

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
    private TextField txtNombres;
    @FXML
    private TextField txtApellidos;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtEmail;
    @FXML
    private TableView tblClientes;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colNombres;
    @FXML
    private TableColumn colApellidos;
    @FXML
    private TableColumn colTelefono;
    @FXML
    private TableColumn colDireccion;
    @FXML
    private TableColumn colEmail;
    @FXML
    private TableColumn colTipoCliente;
    @FXML
    private ComboBox cmbTipoCliente;

    public void activarControles() {
        txtNombres.setEditable(true);
        txtApellidos.setEditable(true);
        txtTelefono.setEditable(true);
        txtDireccion.setEditable(true);
        txtEmail.setEditable(true);
        cmbTipoCliente.setDisable(false);
        /*btnTipoCliente.setDisable(false);
        btnLocales.setDisable(false);
        imgRegresar.setDisable(false);*/
    }

    public void desactivarControles() {
        txtNombres.setEditable(false);
        txtApellidos.setEditable(false);
        txtTelefono.setEditable(false);
        txtDireccion.setEditable(false);
        txtEmail.setEditable(false);
        cmbTipoCliente.setDisable(true);

        /*btnTipoCliente.setDisable(true);
        btnLocales.setDisable(true);
        imgRegresar.setDisable(true);*/
    }

    public void limpiarControles() {
        txtId.clear();
        txtNombres.clear();
        txtApellidos.clear();
        txtTelefono.clear();
        txtDireccion.clear();
        txtEmail.clear();
        //cmbTipoCliente.getSelectionModel().clearSelection();
        cmbTipoCliente.valueProperty().set(null);
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
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
                btnCuentasCobrar.setDisable(true);
                imgCuentasPorCobrar.setOpacity(0.15);
                tblClientes.setDisable(true);
                imgRegresar.setDisable(true);
                imgRegresar.setOpacity(0.15);
                operacion = Operaciones.GUARDAR;
                break;
            case GUARDAR:
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtNombres);
                listaCampos.add(txtApellidos);
                listaCampos.add(txtTelefono);
                listaCampos.add(txtDireccion);
                listaCampos.add(txtEmail);
                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                listaComboBox.add(cmbTipoCliente);
                if (escenarioPrincipal.validar(listaCampos, listaComboBox)) {
                    if ((escenarioPrincipal.validarTelefono(txtTelefono.getText()))) {
                        if (escenarioPrincipal.validarCorreo(txtEmail.getText())) {
                            if (escenarioPrincipal.validarString(txtNombres.getText())) {
                                if (escenarioPrincipal.validarString(txtApellidos.getText())) {
                                    agregarClientes();
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
                                    btnCuentasCobrar.setDisable(false);
                                    imgCuentasPorCobrar.setOpacity(1);
                                    tblClientes.setDisable(false);
                                    imgRegresar.setDisable(false);
                                    imgRegresar.setOpacity(1);
                                    operacion = Operaciones.NINGUNO;
                                } else {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("KINAL MALL");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Ingresar solo letras en el campo Apellido.");
                                    alert.show();
                                }
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("KINAL MALL");
                                alert.setHeaderText(null);
                                alert.setContentText("Ingresar solo letras en el campo Nombre.");
                                alert.show();
                            }

                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("KINAL MALL");
                            alert.setHeaderText(null);
                            alert.setContentText("Formato de Email invalido."
                                    + "\n (Ejemplo: abcde@gmail.com)");
                            alert.show();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("KINAL MALL");
                        alert.setHeaderText(null);
                        alert.setContentText("Numero de telefono invalido."
                                + "\n Solo se deben ingresar 8 numeros en un formato de 12345678.");
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
                if (tblClientes.getSelectionModel().getSelectedItem() == null) {
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
                    btnCuentasCobrar.setDisable(true);
                    imgCuentasPorCobrar.setOpacity(0.15);
                    tblClientes.setDisable(true);
                    imgRegresar.setDisable(true);
                    imgRegresar.setOpacity(0.15);
                    operacion = Operaciones.ACTUALIZAR;
                }
                break;
            case ACTUALIZAR:
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtId);
                listaCampos.add(txtNombres);
                listaCampos.add(txtApellidos);
                listaCampos.add(txtTelefono);
                listaCampos.add(txtDireccion);
                listaCampos.add(txtEmail);
                ArrayList<ComboBox> listaComboBox = new ArrayList<>();
                listaComboBox.add(cmbTipoCliente);
                if (escenarioPrincipal.validar(listaCampos, listaComboBox)) {
                    if (escenarioPrincipal.validarTelefono(txtTelefono.getText())) {
                        if (escenarioPrincipal.validarCorreo(txtEmail.getText())) {
                            if (escenarioPrincipal.validarString(txtNombres.getText())) {
                                if (escenarioPrincipal.validarString(txtApellidos.getText())) {
                                    editarClientes();
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
                                    btnCuentasCobrar.setDisable(false);
                                    imgCuentasPorCobrar.setOpacity(1);
                                    tblClientes.setDisable(false);
                                    imgRegresar.setDisable(false);
                                    imgRegresar.setOpacity(1);
                                    operacion = Operaciones.NINGUNO;
                                } else {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setTitle("KINAL MALL");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Ingresar solo letras en el campo Apellido.");
                                    alert.show();
                                }
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("KINAL MALL");
                                alert.setHeaderText(null);
                                alert.setContentText("Ingresar solo letras en el campo Nombre.");
                                alert.show();
                            }

                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("KINAL MALL");
                            alert.setHeaderText(null);
                            alert.setContentText("Formato de Email invalido."
                                    + "\n (Ejemplo: abcde@gmail.com)");
                            alert.show();
                        }

                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("KINAL MALL");
                        alert.setHeaderText(null);
                        alert.setContentText("Numero de telefono invalido."
                                + "\n Solo se deben ingresar 8 numeros en un formato de 12345678.");
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
                btnCuentasCobrar.setDisable(false);
                imgCuentasPorCobrar.setOpacity(1);
                tblClientes.setDisable(false);
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
                btnCuentasCobrar.setDisable(false);
                imgCuentasPorCobrar.setOpacity(1);
                btnModificar.setText("Modificar");
                imgEditar.setImage(new Image(direccion + "editar.png"));
                btnEliminar.setText("Eliminar");
                imgEliminar.setImage(new Image(direccion + "borrar-usuario.png"));
                tblClientes.setDisable(false);
                imgRegresar.setDisable(false);
                imgRegresar.setOpacity(1);
                operacion = Operaciones.NINGUNO;
                break;
            case NINGUNO:
                if (tblClientes.getSelectionModel().getSelectedItem() == null) {
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
                        eliminarCliente();
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
        GenerarReporte.getInstance().mostrarReporte("ReporteClientes.jasper", parametro, "Reporte de Clientes.");
    }

    @FXML
    private void regresar(MouseEvent event) {
        if (escenarioPrincipal.getUsuario().getRol() == 1) {
            escenarioPrincipal.menuPrincipal();
        } else {
            escenarioPrincipal.menuUsuario();
        }
    }

    public ObservableList<TipoCliente> getTipoClientes() {
        ArrayList<TipoCliente> lista = new ArrayList<>();
        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarTipoCliente}");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                lista.add(new TipoCliente(rs.getInt("id"), rs.getString("descripcion")));
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        listaTipoCliente = FXCollections.observableArrayList(lista);
        return listaTipoCliente;
    }

    public ObservableList<Clientes> getClientes() {
        ArrayList<Clientes> lista = new ArrayList<>();
        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarClientes()}");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                lista.add(new Clientes(
                        rs.getInt("id"), rs.getString("nombres"), rs.getString("apellidos"),
                        rs.getString("telefono"), rs.getString("direccion"),
                        rs.getString("email"), rs.getInt("codigoTipoCliente")
                )
                );
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        listaClientes = FXCollections.observableArrayList(lista);
        return listaClientes;
    }

    public TipoCliente buscarTipoCliente(int id) {
        TipoCliente registro = null;
        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarTipoCliente(?)}");
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                registro = new TipoCliente(rs.getInt("id"), rs.getString("descripcion"));
            }
            rs.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return registro;
    }

    public boolean existeElementoSeleccionado() {
        return tblClientes.getSelectionModel().getSelectedItem() != null;

    }

    @FXML
    public void seleccionarElemento() {
        if (existeElementoSeleccionado()) {
            txtId.setText(String.valueOf(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getId()));
            txtNombres.setText(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getNombres());
            txtApellidos.setText(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getApellidos());
            txtTelefono.setText(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getTelefono());
            txtDireccion.setText(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getDireccion());
            txtEmail.setText(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getEmail());
            cmbTipoCliente.getSelectionModel().select(buscarTipoCliente(((Clientes) tblClientes.getSelectionModel().getSelectedItem()).getCodigoTipoCliente()));
        }
    }

    public void agregarClientes() {
        Clientes cliente = new Clientes();
        cliente.setNombres(txtNombres.getText());
        cliente.setApellidos(txtApellidos.getText());
        cliente.setTelefono(txtTelefono.getText());
        cliente.setDireccion(txtDireccion.getText());
        cliente.setEmail(txtEmail.getText());

        cliente.setCodigoTipoCliente(((TipoCliente) cmbTipoCliente.getSelectionModel().getSelectedItem()).getId());

        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarClientes(?,?,?,?,?,?)}");
            pstmt.setString(1, cliente.getNombres());
            pstmt.setString(2, cliente.getApellidos());
            pstmt.setString(3, cliente.getTelefono());
            pstmt.setString(4, cliente.getDireccion());
            pstmt.setString(5, cliente.getEmail());
            pstmt.setInt(6, cliente.getCodigoTipoCliente());
            pstmt.execute();
            listaClientes.add(cliente);
            cargarDatos();
            limpiarControles();
            desactivarControles();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1452) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("KINAL MALL");
                alert.setHeaderText(null);
                alert.setContentText("Seleccione un tipo de cliente para continuar.");
                alert.show();
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void editarClientes() {
        Clientes cliente = new Clientes();
        cliente.setId(Integer.parseInt(txtId.getText()));
        cliente.setNombres(txtNombres.getText());
        cliente.setApellidos(txtApellidos.getText());
        cliente.setTelefono(txtTelefono.getText());
        cliente.setDireccion(txtDireccion.getText());
        cliente.setEmail(txtEmail.getText());
        cliente.setCodigoTipoCliente(((TipoCliente) cmbTipoCliente.getSelectionModel().getSelectedItem()).getId());

        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarClientes(?, ?, ?, ?, ?, ?, ?)}");
            pstmt.setInt(1, cliente.getId());
            pstmt.setString(2, cliente.getNombres());
            pstmt.setString(3, cliente.getApellidos());
            pstmt.setString(4, cliente.getTelefono());
            pstmt.setString(5, cliente.getDireccion());
            pstmt.setString(6, cliente.getEmail());
            pstmt.setInt(7, cliente.getCodigoTipoCliente());
            pstmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarCliente() {
        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarClientes(?)}");
            pstmt.setInt(1, Integer.parseInt(txtId.getText()));
            pstmt.execute();
            cargarDatos();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1451) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("KINAL MALL");
                alert.setHeaderText(null);
                alert.setContentText("Antes de eliminar este cliente debe de eliminar sus cuentas por cobrar relacionadas.");
                alert.show();
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cargarDatos() {
        tblClientes.setItems(getClientes());
        colId.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("id"));
        colNombres.setCellValueFactory(new PropertyValueFactory<Clientes, String>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<Clientes, String>("apellidos"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Clientes, String>("telefono"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Clientes, String>("direccion"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Clientes, String>("email"));
        colTipoCliente.setCellValueFactory(new PropertyValueFactory<Clientes, Integer>("codigoTipoCliente"));

        cmbTipoCliente.setItems(getTipoClientes());
    }
}
