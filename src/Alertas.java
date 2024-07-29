import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;;

public class Alertas {

    private Alert alerta;

    public void errorMensagem(String Mensagem){

        alerta = new Alert (AlertType.ERROR);
        alerta.setTitle("Mensagem de erro");
        alerta.setHeaderText(null);
        alerta.setContentText(Mensagem);
        alerta.showAndWait();

    }

    public void sucessoMensagem(String Mensagem){

        alerta = new Alert (AlertType.INFORMATION);
        alerta.setTitle("Mensagem de sucesso");
        alerta.setHeaderText(null);
        alerta.setContentText(Mensagem);
        alerta.showAndWait();

    }

}
