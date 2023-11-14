package Util;


import javafx.scene.control.Alert;

public class AlertUtil {
    public static void mostrarError(String msj){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(msj);
        alert.show();
    }

    public static void mostrarCorrecto(String msj){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msj);
        alert.show();
    }

}
