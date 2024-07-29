

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class PrincipaisItensController {

    @FXML
    private TableView<ProdutoMySQL> tabela_itens;
    @FXML
    private TableColumn<ProdutoMySQL, Integer> col_ID;
    @FXML
    private TableColumn<ProdutoMySQL, String> col_Descricao;
    @FXML
    private TableColumn<ProdutoMySQL, Integer> col_Estoque;
    @FXML
    private TableColumn<ProdutoMySQL, Integer> col_IDFornecedor;
    @FXML
    private TableColumn<ProdutoMySQL, String> col_Status;
    @FXML
    private ChoiceBox<String> escolha;
    @FXML
    private Label usuarioNome;
    @FXML
    private Stage stage;
    @FXML
    private Button botaoItensZerados;
    @FXML
    private Button botaoEstoqueGeral;

    private String[] representadas = {"Todos", "331 - Bignard", "409 - BRW", "431 - Tekbond", "555 - Canson"};

    @FXML
    public void initialize() {

        col_ID.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_Descricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        col_Estoque.setCellValueFactory(new PropertyValueFactory<>("estoque"));
        col_IDFornecedor.setCellValueFactory(new PropertyValueFactory<>("id_fornecedor"));
        col_Status.setCellValueFactory(new PropertyValueFactory<>("status"));

        tabela_itens.setItems(getProdutos());
        escolha.getItems().addAll(representadas);
        escolha.getSelectionModel().selectFirst();

        tabela_itens.setItems(getProdutos());

        escolha.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

        tabela_itens.setItems(getProdutosFiltrados(newValue));
        });
    }

    public ObservableList<ProdutoMySQL> getProdutos() {
        ObservableList<ProdutoMySQL> produtos = FXCollections.observableArrayList();

        try {
            Connection conn = conexao.ConnectDB();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM produtos");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProdutoMySQL produto = new ProdutoMySQL(
                    rs.getInt("id"),
                    rs.getInt("estoque"),
                    rs.getInt("id_fornecedor"),
                    rs.getString("descricao"),
                    rs.getString("status").toUpperCase());
                produtos.add(produto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return produtos;
    }

    public ObservableList<ProdutoMySQL> getProdutosFiltrados(String filtro) {
        ObservableList<ProdutoMySQL> produtosFiltrados = FXCollections.observableArrayList();

        try {
            Connection conn = conexao.ConnectDB();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM produtos WHERE id_fornecedor LIKE ?");
            if (filtro.equals("Todos")) {
                ps.setString(1, "%");
            } else {

                ps.setString(1, filtro.split(" ")[0] + "%");
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProdutoMySQL produto = new ProdutoMySQL(
                    rs.getInt("id"),
                    rs.getInt("estoque"),
                    rs.getInt("id_fornecedor"),
                    rs.getString("descricao"),
                    rs.getString("status").toUpperCase());
                produtosFiltrados.add(produto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return produtosFiltrados;
    }

    @FXML
    private void handleBotaoEstoqueGeral(ActionEvent event) throws IOException {
        changeScene("/fxml/EstoqueGeral.fxml", "SincroEstoque - Principais Itens");
    }

    @FXML
    private void handleBotaoItensZerados(ActionEvent event) throws IOException {
        changeScene("/fxml/ItensZerados.fxml", "SincroEstoque - Estoque Geral");
    }

    private void changeScene(String fxml, String title) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Stage stage = (Stage) tabela_itens.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.show();
    }
}
