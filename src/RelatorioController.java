

import java.io.IOException;
import java.time.LocalTime;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class RelatorioController {

    private Stage stage;
    @FXML
    private Label saudacao;
    @FXML
    private Label usuarioNome;
    @FXML
    private Button botaoEstoqueGeral;
    @FXML
    private Button botaoItensZerados;
    @FXML
    private Button botaoPrincipaisItens;
    @FXML
    public void initialize() {
        usuarioNome.setText(LoginController.getUsuarioAtual());
        saudacao.setText(getSaudacao());
    }

    private String getSaudacao() {
        LocalTime now = LocalTime.now();
        if (now.isBefore(LocalTime.NOON)) {
            return "Bom dia";
        } else if (now.isBefore(LocalTime.of(18, 0))) {
            return "Boa tarde";
        } else {
            return "Boa noite";
        }
    }

    @FXML
    void handleBotaoPrincipaisItens(ActionEvent event) throws IOException {
        changeScene("/fxml/PrincipaisItens.fxml", "SincroEstoque - Principais Itens");
    }

    @FXML
    private void handleBotaoEstoqueGeral(ActionEvent event) throws IOException {
        changeScene("/fxml/EstoqueGeral.fxml", "SincroEstoque - Estoque Geral");
    }

    @FXML
    private void handleBotaoItensZerados(ActionEvent event) throws IOException {
        changeScene("/fxml/ItensZerados.fxml", "SincroEstoque - Itens Zerados");
    }

    private void changeScene(String fxml, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = loader.load();
        stage = (Stage) saudacao.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.show();
    }
}
