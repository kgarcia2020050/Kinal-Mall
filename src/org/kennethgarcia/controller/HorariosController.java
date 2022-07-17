/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kennethgarcia.controller;

import com.jfoenix.controls.JFXTimePicker;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import java.sql.Time;
import java.sql.PreparedStatement;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.input.MouseEvent;
import org.kennethgarcia.bean.Horarios;
import org.kennethgarcia.db.Conexion;
import org.kennethgarcia.report.GenerarReporte;
import org.kennethgarcia.system.Principal;

/**
 *
 * @author Kenneth Gerardo Garcia Lemus
 * @date 7/07/2021
 * @time 08:07:11
 */
public class HorariosController implements Initializable {

    @FXML
    private ImageView imgRegresar;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        cargarDatos();
        tpEntrada.setEditable(false);
        tpSalida.setEditable(false);
    }

    private enum Operaciones {
        NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO;
    }

    private Operaciones operacion = Operaciones.NINGUNO;

    private final String direccion = "/org/kennethgarcia/resources/image/";
    private Principal escenarioPrincipal;
    private ObservableList<Horarios> listaHorarios;
    @FXML
    private TextField txtId;
    @FXML
    private JFXTimePicker tpEntrada;
    @FXML
    private JFXTimePicker tpSalida;
    @FXML
    private CheckBox cbxLunes;
    @FXML
    private CheckBox cbxMartes;
    @FXML
    private CheckBox cbxMiercoles;
    @FXML
    private CheckBox cbxJueves;
    @FXML
    private CheckBox cbxViernes;
    @FXML
    private TableView tblHorarios;

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colEntrada;
    @FXML
    private TableColumn colSalida;
    @FXML
    private TableColumn colLunes;
    @FXML
    private TableColumn colMartes;
    @FXML
    private TableColumn colMiercoles;
    @FXML
    private TableColumn colJueves;
    @FXML
    private TableColumn colViernes;
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

    public boolean existeElementoSeleccionado() {
        return tblHorarios.getSelectionModel().getSelectedItem() != null;
    }

    @FXML
    private void regresar(MouseEvent event) {
        escenarioPrincipal.escenaEmpleados();
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

    public void cargarDatos() {
        tblHorarios.setItems(getHorarios());
        colId.setCellValueFactory(new PropertyValueFactory<Horarios, Integer>("id"));
        colEntrada.setCellValueFactory(new PropertyValueFactory<Horarios, Time>("horarioEntrada"));
        colSalida.setCellValueFactory(new PropertyValueFactory<Horarios, Time>("horarioSalida"));
        colLunes.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("lunes"));
        colMartes.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("martes"));
        colMiercoles.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("miercoles"));
        colJueves.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("jueves"));
        colViernes.setCellValueFactory(new PropertyValueFactory<Horarios, Boolean>("viernes"));
    }

    public void habilitarDatos() {
        txtId.setEditable(false);
        txtId.setDisable(false);
        tpEntrada.setDisable(false);
        tpSalida.setDisable(false);
        cbxLunes.setDisable(false);
        cbxMartes.setDisable(false);
        cbxMiercoles.setDisable(false);
        cbxJueves.setDisable(false);
        cbxViernes.setDisable(false);

    }

    public void deshabilitarTexto() {
        txtId.setEditable(false);
        txtId.setDisable(false);
        tpEntrada.setDisable(true);
        tpSalida.setDisable(true);
        cbxLunes.setDisable(true);
        cbxMartes.setDisable(true);
        cbxMiercoles.setDisable(true);
        cbxJueves.setDisable(true);
        cbxViernes.setDisable(true);
    }

    public void limpiarTexto() {
        txtId.clear();
        tpEntrada.getEditor().clear();
        tpSalida.getEditor().clear();
        cbxLunes.setSelected(false);
        cbxMartes.setSelected(false);
        cbxMiercoles.setSelected(false);
        cbxJueves.setSelected(false);
        cbxViernes.setSelected(false);
    }

    public void agregarHorarios() {
        Horarios horario = new Horarios();
        horario.setHorarioEntrada(Time.valueOf(tpEntrada.getValue()));
        horario.setHorarioSalida(Time.valueOf(tpSalida.getValue()));
        horario.setLunes(cbxLunes.isSelected());
        horario.setMartes(cbxMartes.isSelected());
        horario.setMiercoles(cbxMiercoles.isSelected());
        horario.setJueves(cbxJueves.isSelected());
        horario.setViernes(cbxViernes.isSelected());
        PreparedStatement stmt;
        try {
            stmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_AgregarHorarios(?,?,?,?,?,?,?)}");
            stmt.setTime(1, horario.getHorarioEntrada());
            stmt.setTime(2, horario.getHorarioSalida());
            stmt.setBoolean(3, horario.isLunes());
            stmt.setBoolean(4, horario.isMartes());
            stmt.setBoolean(5, horario.isMiercoles());
            stmt.setBoolean(6, horario.isJueves());
            stmt.setBoolean(7, horario.isViernes());
            stmt.execute();
            cargarDatos();
            limpiarTexto();
            deshabilitarTexto();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editarHorarios() {
        Horarios horarios = (Horarios) tblHorarios.getSelectionModel().getSelectedItem();
        Horarios horario = new Horarios();
        horario.setId(Integer.parseInt(txtId.getText()));
        horario.setHorarioEntrada(Time.valueOf(tpEntrada.getValue()));
        horario.setHorarioSalida(Time.valueOf(tpSalida.getValue()));
        horario.setLunes(cbxLunes.isSelected());
        horario.setMartes(cbxMartes.isSelected());
        horario.setMiercoles(cbxMiercoles.isSelected());
        horario.setJueves(cbxJueves.isSelected());
        horario.setViernes(cbxViernes.isSelected());

        try {
            PreparedStatement stmt;
            stmt = Conexion.getInstance().getConexion().prepareCall("CALL sp_EditarHorarios(?,?,?,?,?,?,?,?)");
            stmt.setInt(1, horario.getId());
            stmt.setTime(2, horario.getHorarioEntrada());
            stmt.setTime(3, horario.getHorarioSalida());
            stmt.setBoolean(4, horario.isLunes());
            stmt.setBoolean(5, horario.isMartes());
            stmt.setBoolean(6, horario.isMiercoles());
            stmt.setBoolean(7, horario.isJueves());
            stmt.setBoolean(8, horario.isViernes());
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarHorarios() {

        Horarios horario = (Horarios) tblHorarios.getSelectionModel().getSelectedItem();
        try {
            PreparedStatement pstmt = Conexion.getInstance().getConexion().prepareCall("{CALL sp_EliminarHorarios(?)}");
            pstmt.setInt(1, horario.getId());
            pstmt.execute();
            cargarDatos();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1451) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("KINAL MALL");
                alert.setHeaderText(null);
                alert.setContentText("Antes de eliminar este horario debe eliminar a su empleados relacionados.");
                alert.show();
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* public void validarTiempo(String time){
    String patron="[0-9]{4}-{1}(0[0-9]{2}|1[012])-{1}(1[0-9])";
    Pattern pattern=Pattern.compile(patron);
    Matcher matcher
    }*/
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
                tblHorarios.setDisable(true);
                imgRegresar.setDisable(true);
                imgRegresar.setOpacity(0.15);
                operacion = Operaciones.GUARDAR;
                break;
            case GUARDAR:
                ArrayList<JFXTimePicker> horas = new ArrayList<>();
                horas.add(tpEntrada);
                horas.add(tpSalida);
                ArrayList<TextField> listaCampos = new ArrayList<>();
                ArrayList<ComboBox> listaCombo = new ArrayList<>();
                if (escenarioPrincipal.validar(listaCampos, listaCombo)) {
                    if ((tpEntrada.getValue() != null)) {
                        if ((tpSalida.getValue() != null)) {
                            agregarHorarios();
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
                            tblHorarios.setDisable(false);
                            imgRegresar.setDisable(false);
                            imgRegresar.setOpacity(1);
                            operacion = Operaciones.NINGUNO;
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("KINAL MALL");
                            alert.setHeaderText(null);
                            alert.setContentText("Ingresar la hora de salida.");
                            alert.show();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("KINAL MALL");
                        alert.setHeaderText(null);
                        alert.setContentText("Ingresar la hora de entrada.");
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
                if (tblHorarios.getSelectionModel().getSelectedItem() == null) {
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
                    tblHorarios.setDisable(true);
                    imgRegresar.setDisable(true);
                    imgRegresar.setOpacity(0.15);
                    operacion = Operaciones.ACTUALIZAR;
                }
                break;
            case ACTUALIZAR:
                ArrayList<TextField> listaCampos = new ArrayList<>();
                listaCampos.add(txtId);
                ArrayList<JFXTimePicker> horas = new ArrayList<>();
                horas.add(tpEntrada);
                horas.add(tpSalida);
                ArrayList<ComboBox> listaCombo = new ArrayList<>();
                if (escenarioPrincipal.validar(listaCampos, listaCombo)) {
                    if ((tpEntrada.getValue() != null)) {
                        if ((tpSalida.getValue() != null)) {
                            editarHorarios();
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
                            tblHorarios.setDisable(false);
                            imgRegresar.setDisable(false);
                            imgRegresar.setOpacity(1);
                            operacion = Operaciones.NINGUNO;
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("KINAL MALL");
                            alert.setHeaderText(null);
                            alert.setContentText("Ingresar la hora de salida.");
                            alert.show();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("KINAL MALL");
                        alert.setHeaderText(null);
                        alert.setContentText("Ingresar la hora de entrada.");
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
                tblHorarios.setDisable(false);
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
                tblHorarios.setDisable(false);
                imgRegresar.setDisable(false);
                imgRegresar.setOpacity(1);
                operacion = Operaciones.NINGUNO;
                break;
            case NINGUNO:
                if (tblHorarios.getSelectionModel().getSelectedItem() == null) {
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
                        eliminarHorarios();
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
        GenerarReporte.getInstance().mostrarReporte("ReporteHorarios.jasper", parametro, "Reporte de Horarios.");
    }

    @FXML
    public void seleccionarElemento() {
        if (existeElementoSeleccionado()) {
            txtId.setText(String.valueOf(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).getId()));
            tpEntrada.setValue(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).getHorarioEntrada().toLocalTime());
            tpSalida.setValue(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).getHorarioSalida().toLocalTime());
            cbxLunes.setText(String.valueOf(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).isLunes()));
            cbxLunes.setSelected(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).isLunes());
            cbxMartes.setText(String.valueOf(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).isMartes()));
            cbxMartes.setSelected(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).isMartes());
            cbxMiercoles.setText(String.valueOf(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).isMiercoles()));
            cbxMiercoles.setSelected(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).isMiercoles());
            cbxJueves.setText(String.valueOf(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).isJueves()));
            cbxJueves.setSelected(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).isJueves());
            cbxViernes.setText(String.valueOf(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).isViernes()));
            cbxViernes.setSelected(((Horarios) tblHorarios.getSelectionModel().getSelectedItem()).isViernes());
        }

    }

}
