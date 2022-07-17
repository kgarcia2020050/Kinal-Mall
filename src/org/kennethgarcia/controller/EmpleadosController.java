/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kennethgarcia.controller;

import com.jfoenix.controls.JFXDatePicker;
import java.math.BigDecimal;
import java.net.URL;
import javafx.fxml.FXML;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
import org.kennethgarcia.bean.Administracion;
import org.kennethgarcia.bean.Cargos;
import org.kennethgarcia.bean.Departamentos;
import org.kennethgarcia.bean.Empleados;
import org.kennethgarcia.bean.Horarios;
import org.kennethgarcia.db.Conexion;
import org.kennethgarcia.report.GenerarReporte;
import org.kennethgarcia.system.Principal;

/**
 *
 * @author Kenneth Gerardo Garcia Lemus
 * @date 7/07/2021
 * @time 15:33:44
 */
public class EmpleadosController implements Initializable {

    @FXML
    private ImageView imgHorarios;
    @FXML
    private ImageView imgRegresar;

    private enum Operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO;
    }

    private Operaciones operacion = Operaciones.NINGUNO;

    private final String direccion = "/org/kennethgarcia/resources/image/";

    private ObservableList<Administracion> listaAdministracion;
    private ObservableList<Cargos> listaCargos;
    private ObservableList<Departamentos> listaDepartamentos;
    private ObservableList<Horarios> listaHorarios;
    private ObservableList<Empleados> listaEmpleados;
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
    private Button btnHorarios;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNombre;
    @FXML
    private TableView tblEmpleados;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colApellido;
    @FXML
    private TableColumn colTelefono;
    @FXML
    private TableColumn colEmail;
    @FXML
    private TableColumn colSueldo;
    @FXML
    private TableColumn colContratacion;
    @FXML
    private TableColumn colDepartamento;
    @FXML
    private TableColumn colCargo;
    @FXML
    private TableColumn colHorario;
    @FXML
    private TableColumn colAdministracion;
    @FXML
    private JFXDatePicker dtContratacion;
    @FXML
    private TextField txtSueldo;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtApellido;
    @FXML
    private TextField txtTelefono;
    @FXML
    private ComboBox cmbDepartamento;
    @FXML
    private ComboBox cmbCargo;
    @FXML
    private ComboBox cmbHorario;
    @FXML
    private ComboBox cmbAdministracion;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        dtContratacion.setEditable(false);
        cargarDatos();
    }

    public void cargarDatos() {
        tblEmpleados.setItems(getEmpleados());
        colId.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Empleados, String>("nombres"));
        colApellido.setCellValueFactory(new PropertyValueFactory<Empleados, String>("apellidos"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Empleados, String>("telefono"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Empleados, String>("email"));
        colSueldo.setCellValueFactory(new PropertyValueFactory<Empleados, BigDecimal>("sueldo"));
        colContratacion.setCellValueFactory(new PropertyValueFactory<Empleados, String>("fechaContratacion"));
        colDepartamento.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("codigoDepartamento"));
        colCargo.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("codigoCargo"));
        colHorario.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("codigoHorario"));
        colAdministracion.setCellValueFactory(new PropertyValueFactory<Empleados, Integer>("codigoAdministracion"));
        cmbDepartamento.setItems(getDepartamentos());
        cmbCargo.setItems(getCargos());
        cmbHorario.setItems(getHorarios());
        cmbAdministracion.setItems(getAdministracion());

    }

    public ObservableList<Empleados> getEmpleados() {
        ArrayList<Empleados> empleado = new ArrayList<>();

        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarEmpleados()}");
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                empleado.add(new Empleados(
                        resultado.getInt("id"),
                        resultado.getString("nombres"),
                        resultado.getString("apellidos"),
                        resultado.getString("email"),
                        resultado.getString("telefono"),
                        resultado.getDate("fechaContratacion"),
                        resultado.getBigDecimal("sueldo"),
                        resultado.getInt("codigoDepartamento"),
                        resultado.getInt("codigoCargo"),
                        resultado.getInt("codigoHorario"),
                        resultado.getInt("codigoAdministracion")));
            }

            resultado.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        listaEmpleados = FXCollections.observableArrayList(empleado);
        return listaEmpleados;
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
        if (escenarioPrincipal.getUsuario().getRol() == 1) {
            escenarioPrincipal.menuPrincipal();
        } else {
            escenarioPrincipal.menuUsuario();
        }
    }

    public boolean existeElementoSeleccionado() {
        return tblEmpleados.getSelectionModel().getSelectedItem() != null;
    }

    @FXML
    private void nuevo(ActionEvent event) {
        switch (operacion) {
            case NINGUNO:
                activarControles();
                limpiarTexto();
                btnNuevo.setText("Guardar");
                imgNuevo.setImage(new Image(direccion + "salvar.png"));
                btnModificar.setText("Cancelar");
                imgEditar.setImage(new Image(direccion + "boton-x.png"));
                btnEliminar.setDisable(true);
                imgEliminar.setOpacity(0.15);
                btnReporte.setDisable(true);
                imgReporte.setOpacity(0.15);
                tblEmpleados.setDisable(true);
                imgHorarios.setDisable(true);
                imgHorarios.setOpacity(0.15);
                btnHorarios.setDisable(true);
                imgRegresar.setDisable(true);
                imgRegresar.setOpacity(0.15);
                operacion = Operaciones.GUARDAR;
                break;
            case GUARDAR:
                ArrayList<JFXDatePicker> fecha = new ArrayList<>();
                fecha.add(dtContratacion);
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtNombre);
                listaCampos.add(txtApellido);
                listaCampos.add(txtEmail);
                listaCampos.add(txtTelefono);
                listaCampos.add(txtSueldo);
                ArrayList<ComboBox> listaCombo = new ArrayList<>();
                listaCombo.add(cmbDepartamento);
                listaCombo.add(cmbCargo);
                listaCombo.add(cmbHorario);
                listaCombo.add(cmbAdministracion);
                if (escenarioPrincipal.validar(listaCampos, listaCombo)) {
                    if (dtContratacion.getValue() != null) {
                        if (escenarioPrincipal.validarCorreo(txtEmail.getText())) {
                            if (escenarioPrincipal.validarDecimal(txtSueldo.getText())) {
                                if (escenarioPrincipal.validarTelefono(txtTelefono.getText())) {
                                    if (escenarioPrincipal.validarString(txtNombre.getText())) {
                                        if (escenarioPrincipal.validarString(txtApellido.getText())) {
                                            agregarEmpleados();
                                            cargarDatos();
                                            limpiarTexto();
                                            desactivarControles();
                                            btnNuevo.setText("Nuevo");
                                            imgNuevo.setImage(new Image(direccion + "agregar-usuario (1).png"));
                                            btnModificar.setText("Modificar");
                                            imgEditar.setImage(new Image(direccion + "editar.png"));
                                            btnEliminar.setDisable(false);
                                            imgEliminar.setOpacity(1);
                                            btnReporte.setDisable(false);
                                            imgReporte.setOpacity(1);
                                            tblEmpleados.setDisable(false);
                                            imgHorarios.setDisable(false);
                                            imgHorarios.setOpacity(1);
                                            btnHorarios.setDisable(false);
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
                                    alert.setContentText("Numero de telefono invalido."
                                            + "\n Solo se deben ingresar 8 numeros en un formato de 12345678.");
                                    alert.show();
                                }
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("KINAL MALL");
                                alert.setHeaderText(null);
                                alert.setContentText("Ingresar solo numeros en el campo Sueldo.");
                                alert.show();
                            }
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("KINAL MALL");
                            alert.setHeaderText(null);
                            alert.setContentText("Correo invalido."
                                    + "\n (Ejemplo: abcde@gmail.com)");
                            alert.show();
                        }

                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("KINAL MALL");
                        alert.setHeaderText(null);
                        alert.setContentText("Ingresar la fecha de contratacion.");
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
                if (tblEmpleados.getSelectionModel().getSelectedItem() == null) {
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
                    tblEmpleados.setDisable(true);
                    imgHorarios.setDisable(true);
                    imgHorarios.setOpacity(0.15);
                    btnHorarios.setDisable(true);
                    imgRegresar.setDisable(true);
                    imgRegresar.setOpacity(0.15);
                    operacion = Operaciones.ACTUALIZAR;
                }
                break;
            case ACTUALIZAR:
                ArrayList<JFXDatePicker> fecha = new ArrayList<>();
                fecha.add(dtContratacion);
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtId);
                listaCampos.add(txtNombre);
                listaCampos.add(txtApellido);
                listaCampos.add(txtEmail);
                listaCampos.add(txtTelefono);
                listaCampos.add(txtSueldo);
                ArrayList<ComboBox> listaCombo = new ArrayList<>();
                listaCombo.add(cmbDepartamento);
                listaCombo.add(cmbCargo);
                listaCombo.add(cmbHorario);
                listaCombo.add(cmbAdministracion);
                if (escenarioPrincipal.validar(listaCampos, listaCombo)) {
                    if ((dtContratacion.getValue() != null)) {
                        if (escenarioPrincipal.validarCorreo(txtEmail.getText())) {
                            if (escenarioPrincipal.validarDecimal(txtSueldo.getText())) {
                                if (escenarioPrincipal.validarTelefono(txtTelefono.getText())) {
                                    if (escenarioPrincipal.validarString(txtNombre.getText())) {
                                        if (escenarioPrincipal.validarString(txtApellido.getText())) {
                                            modificarEmpleados();
                                            limpiarTexto();
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
                                            tblEmpleados.setDisable(false);
                                            imgHorarios.setDisable(false);
                                            imgHorarios.setOpacity(1);
                                            btnHorarios.setDisable(false);
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
                                    alert.setContentText("Numero de telefono invalido."
                                            + "\n Solo se deben ingresar 8 numeros en un formato de 12345678.");
                                    alert.show();
                                }

                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("KINAL MALL");
                                alert.setHeaderText(null);
                                alert.setContentText("Ingresar solo numeros en el campo Sueldo.");
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
                        alert.setContentText("Ingresar la fecha limite de contratacion.");
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
                desactivarControles();
                btnNuevo.setText("Nuevo");
                imgNuevo.setImage(new Image(direccion + "agregar-usuario (1).png"));
                btnModificar.setText("Modificar");
                imgEditar.setImage(new Image(direccion + "editar.png"));
                btnEliminar.setDisable(false);
                imgEliminar.setOpacity(1);
                btnReporte.setDisable(false);
                imgReporte.setOpacity(1);
                tblEmpleados.setDisable(false);
                imgHorarios.setDisable(false);
                imgHorarios.setOpacity(1);
                btnHorarios.setDisable(false);
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
                desactivarControles();
                btnNuevo.setDisable(false);
                imgNuevo.setOpacity(1);
                btnReporte.setDisable(false);
                imgReporte.setOpacity(1);
                imgHorarios.setDisable(false);
                imgHorarios.setOpacity(1);
                btnHorarios.setDisable(false);
                imgRegresar.setDisable(false);
                imgRegresar.setOpacity(1);
                btnModificar.setText("Modificar");
                imgEditar.setImage(new Image(direccion + "editar.png"));
                btnEliminar.setText("Eliminar");
                imgEliminar.setImage(new Image(direccion + "borrar-usuario.png"));
                operacion = Operaciones.NINGUNO;
                break;
            case NINGUNO:
                if (tblEmpleados.getSelectionModel().getSelectedItem() == null) {
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
        GenerarReporte.getInstance().mostrarReporte("ReporteEmpleados.jasper", parametro, "Reporte de Empleados.");
    }

    @FXML
    private void mostrarVistaHorarios(ActionEvent event) {
        escenarioPrincipal.escenaHorarios();
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

    public ObservableList<Departamentos> getDepartamentos() {
        ArrayList<Departamentos> lista = new ArrayList<>();

        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarDepartamentos()}");
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                lista.add(new Departamentos(
                        resultado.getInt("id"),
                        resultado.getString("nombre")));
            }

            resultado.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        listaDepartamentos = FXCollections.observableArrayList(lista);
        return listaDepartamentos;
    }

    public Departamentos buscarDepartamento(int id) {
        Departamentos departamento = null;
        PreparedStatement stmt = null;
        ResultSet resultado = null;
        try {
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarDepartamentos(?)}");
            stmt.setInt(1, id);
            resultado = stmt.executeQuery();
            while (resultado.next()) {
                departamento = new Departamentos(
                        resultado.getInt("id"),
                        resultado.getString("nombre")
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
        return departamento;
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

    public Cargos buscarCargos(int id) {
        Cargos cargo = null;
        PreparedStatement stmt = null;
        ResultSet resultado = null;
        try {
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarCargos(?)}");
            stmt.setInt(1, id);
            resultado = stmt.executeQuery();
            while (resultado.next()) {
                cargo = new Cargos(
                        resultado.getInt("id"),
                        resultado.getString("nombre")
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
        return cargo;
    }

    public ObservableList<Horarios> getHorarios() {
        ArrayList<Horarios> lista = new ArrayList<>();
        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_ListarHorarios()}");
            ResultSet resultado = stmt.executeQuery();

            while (resultado.next()) {
                lista.add(new Horarios(resultado.getInt("id"),
                        resultado.getTime("horarioEntrada"),
                        resultado.getTime("horarioSalida"),
                        resultado.getBoolean("lunes"),
                        resultado.getBoolean("martes"),
                        resultado.getBoolean("miercoles"),
                        resultado.getBoolean("jueves"),
                        resultado.getBoolean("viernes")));
            }
            resultado.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        listaHorarios = FXCollections.observableArrayList(lista);
        return listaHorarios;
    }

    public Horarios buscarHorarios(int id) {
        Horarios horario = null;
        PreparedStatement stmt = null;
        ResultSet resultado = null;
        try {
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_BuscarHorarios(?)}");
            stmt.setInt(1, id);
            resultado = stmt.executeQuery();
            while (resultado.next()) {
                horario = new Horarios(
                        resultado.getInt("id"),
                        resultado.getTime("horarioEntrada"),
                        resultado.getTime("horarioSalida"),
                        resultado.getBoolean("lunes"),
                        resultado.getBoolean("martes"),
                        resultado.getBoolean("miercoles"),
                        resultado.getBoolean("jueves"),
                        resultado.getBoolean("viernes")
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
        return horario;
    }

    public void agregarEmpleados() {
        Empleados empleado = new Empleados();
        empleado.setNombres(txtNombre.getText());
        empleado.setApellidos(txtApellido.getText());
        empleado.setEmail(txtEmail.getText());
        empleado.setTelefono(txtTelefono.getText());
        empleado.setFechaContratacion(Date.valueOf(dtContratacion.getValue()));
        empleado.setSueldo(new BigDecimal(txtSueldo.getText()));
        empleado.setCodigoDepartamento(((Departamentos) cmbDepartamento.getSelectionModel().getSelectedItem()).getId());
        empleado.setCodigoCargo(((Cargos) cmbCargo.getSelectionModel().getSelectedItem()).getId());
        empleado.setCodigoHorario(((Horarios) cmbHorario.getSelectionModel().getSelectedItem()).getId());
        empleado.setCodigoAdministracion(((Administracion) cmbAdministracion.getSelectionModel().getSelectedItem()).getId());

        PreparedStatement stmt = null;
        try {
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarEmpleados(?,?,?,?,?,?,?,?,?,?)}");
            stmt.setString(1, empleado.getNombres());
            stmt.setString(2, empleado.getApellidos());
            stmt.setString(3, empleado.getEmail());
            stmt.setString(4, empleado.getTelefono());
            stmt.setDate(5, empleado.getFechaContratacion());
            stmt.setBigDecimal(6, empleado.getSueldo());
            stmt.setInt(7, empleado.getCodigoDepartamento());
            stmt.setInt(8, empleado.getCodigoCargo());
            stmt.setInt(9, empleado.getCodigoHorario());
            stmt.setInt(10, empleado.getCodigoAdministracion());
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

    public void modificarEmpleados() {
        Empleados empleado = new Empleados();
        empleado.setId(Integer.parseInt(txtId.getText()));
        empleado.setNombres(txtNombre.getText());
        empleado.setApellidos(txtApellido.getText());
        empleado.setEmail(txtEmail.getText());
        empleado.setTelefono(txtTelefono.getText());
        empleado.setFechaContratacion(Date.valueOf(dtContratacion.getValue()));
        empleado.setSueldo(new BigDecimal(txtSueldo.getText()));
        empleado.setCodigoDepartamento(((Departamentos) cmbDepartamento.getSelectionModel().getSelectedItem()).getId());
        empleado.setCodigoCargo(((Cargos) cmbCargo.getSelectionModel().getSelectedItem()).getId());
        empleado.setCodigoHorario(((Horarios) cmbHorario.getSelectionModel().getSelectedItem()).getId());
        empleado.setCodigoAdministracion(((Administracion) cmbAdministracion.getSelectionModel().getSelectedItem()).getId());

        PreparedStatement stmt = null;
        try {
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EditarEmpleados(?,?,?,?,?,?,?,?,?,?,?)}");
            stmt.setInt(1, empleado.getId());
            stmt.setString(2, empleado.getNombres());
            stmt.setString(3, empleado.getApellidos());
            stmt.setString(4, empleado.getEmail());
            stmt.setString(5, empleado.getTelefono());
            stmt.setDate(6, empleado.getFechaContratacion());
            stmt.setBigDecimal(7, empleado.getSueldo());
            stmt.setInt(8, empleado.getCodigoDepartamento());
            stmt.setInt(9, empleado.getCodigoCargo());
            stmt.setInt(10, empleado.getCodigoHorario());
            stmt.setInt(11, empleado.getCodigoAdministracion());
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
        try {
            PreparedStatement stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarEmpleados(?)}");
            stmt.setInt(1, Integer.parseInt(txtId.getText()));
            stmt.execute();
            cargarDatos();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void seleccionarElemento() {
        if (existeElementoSeleccionado()) {
            txtId.setText(String.valueOf(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getId()));
            txtNombre.setText(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getNombres());
            txtApellido.setText(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getApellidos());
            txtEmail.setText(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getEmail());
            txtTelefono.setText(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getTelefono());
            dtContratacion.setValue(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getFechaContratacion().toLocalDate());
            txtSueldo.setText(String.valueOf(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getSueldo()));
            cmbDepartamento.getSelectionModel().select(buscarDepartamento(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoDepartamento()));
            cmbCargo.getSelectionModel().select(buscarCargos(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoCargo()));
            cmbHorario.getSelectionModel().select(buscarHorarios(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoHorario()));
            cmbAdministracion.getSelectionModel().select(buscarAdministracion(((Empleados) tblEmpleados.getSelectionModel().getSelectedItem()).getCodigoAdministracion()));
        }
    }

    public void activarControles() {
        txtId.setEditable(false);
        txtId.setDisable(false);
        txtNombre.setEditable(true);
        txtApellido.setEditable(true);
        txtTelefono.setEditable(true);
        txtEmail.setEditable(true);
        txtSueldo.setEditable(true);

        cmbAdministracion.setDisable(false);
        cmbCargo.setDisable(false);
        cmbDepartamento.setDisable(false);
        cmbHorario.setDisable(false);

        dtContratacion.setDisable(false);
    }

    public void desactivarControles() {
        txtId.setEditable(false);
        txtId.setDisable(false);
        txtNombre.setEditable(false);
        txtApellido.setEditable(false);
        txtTelefono.setEditable(false);
        txtEmail.setEditable(false);
        txtSueldo.setEditable(false);

        cmbAdministracion.setDisable(true);
        cmbCargo.setDisable(true);
        cmbDepartamento.setDisable(true);
        cmbHorario.setDisable(true);

        dtContratacion.setDisable(true);
    }

    public void limpiarTexto() {
        txtId.clear();
        txtNombre.clear();
        txtApellido.clear();
        txtTelefono.clear();
        txtEmail.clear();
        txtSueldo.clear();

        cmbAdministracion.valueProperty().set(null);
        cmbCargo.valueProperty().set(null);
        cmbDepartamento.valueProperty().set(null);
        cmbHorario.valueProperty().set(null);

        dtContratacion.getEditor().clear();
    }

}
