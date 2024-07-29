import java.io.IOException;

import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CadastroController {

    @FXML
    private TextField nome_completo;

    @FXML
    private TextField nome_usuario;

    @FXML
    private PasswordField senha;

    @FXML
    private TextField email;

    @FXML
    private PasswordField confirmarSenha;

    @FXML
    private Button botaoContinuar;

    @FXML
    private Button botaoVoltar;

    @FXML
    private Label mensagemCadastro;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    void handleBotaoContinuar(ActionEvent event) throws IOException {

        Alertas alerta = new Alertas();

        if(nome_completo.getText().isEmpty() || nome_usuario.getText().isEmpty() ||
        email.getText().isEmpty() || senha.getText().isEmpty() || confirmarSenha.getText().isEmpty()){

            alerta.errorMensagem("Preencha todos os campos");

        } else if (!senha.getText().equals(confirmarSenha.getText())){

            alerta.errorMensagem("As senha não coincidem");

        } else {
            try {
                // Verifica se o usuário já existe
                if (usuarioDAO.validar(nome_usuario.getText(), senha.getText())) {
                    alerta.errorMensagem("Nome de usuário já existe");
                } else {
                    // Registra o novo usuário
                    if (usuarioDAO.registrar(nome_completo.getText() ,nome_usuario.getText(), senha.getText(), email.getText())) {
                        mensagemCadastro.setText("Usuário cadastrado com sucesso!");
                    } else {
                        alerta.errorMensagem("Erro ao cadastrar usuário");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                alerta.errorMensagem("Erro de SQL ao cadastrar usuário");
            }
        }
    }

    @FXML
    void handleBotaoVoltar(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));
        Stage stage = (Stage) botaoVoltar.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

}