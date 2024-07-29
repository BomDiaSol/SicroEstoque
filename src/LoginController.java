import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    public Stage stage;

    @FXML
    private TextField usuario;
    @FXML
    private PasswordField senha;
    @FXML
    private Hyperlink forgotPasswordLink;
    @FXML
    private Button botaoLogin;
    @FXML
    private Label usuarioNome;
    @FXML
    private Label usuarioNome1;
    @FXML
    private Button botaoCadastro;

    private static String usuarioAtual;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void userLogin(ActionEvent event) throws IOException {
        try {
            checkLogin();
        } catch (SQLException e) {
            e.printStackTrace();
            usuarioNome.setText("Erro ao conectar com o banco de dados.");
        }
    }

    private void checkLogin() throws IOException, SQLException {
        String usuarioEscrito = usuario.getText();
        String senhaEscrita = senha.getText();

        if (usuarioEscrito.isEmpty() && senhaEscrita.isEmpty()) {
            usuarioNome.setText("Informe ID e senha para efetuar login");
        } else if (usuarioDAO.validar(usuarioEscrito, senhaEscrita)) {
            usuarioNome.setText("Login bem sucedido.");
            usuarioNome1.setText("Usuário: " + usuarioEscrito);
            usuarioAtual = usuarioEscrito;
            changeScene("/fxml/relatorios.fxml", "SincroEstoque - Relatórios");
        } else {
            usuarioNome.setText("Informações incorretas. Tente novamente");
        }
    }

    @FXML
    private void handleBotaoCadastro(ActionEvent event) throws IOException {
        changeScene("/fxml/Cadastro.fxml", "SincroEstoque - Cadastro");
    }

    public static String getUsuarioAtual() {
        return usuarioAtual;
    }

    private void changeScene(String fxml, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = loader.load();
        stage = (Stage) botaoCadastro.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.show();
    }
}
