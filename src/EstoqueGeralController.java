import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class EstoqueGeralController {

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
    private Button botaoPrincipaisItens;
    @FXML
    private ImageView btnGerarPDF;

    private String[] representadas = {"Todos", "331 - Bignard", "409 - BRW", "431 - Tekbond", "555 - Canson", };

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
    private void handleBotaoPrincipaisItens(ActionEvent event) throws IOException {
        changeScene("/fxml/PrincipaisItens.fxml", "SincroEstoque - Principais Itens");
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

    @FXML
    private void handleBotaoGerarPDF(MouseEvent event) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        InputStream fontStream = getClass().getResourceAsStream("/font/RobotoSlab.ttf");
        PDType0Font minhaFonte = PDType0Font.load(document, fontStream);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.setFont(minhaFonte, 12);

        ObservableList<ProdutoMySQL> produtos = tabela_itens.getItems();
        float margin = 50;
        float yStart = page.getMediaBox().getHeight() - margin;
        float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
        float yPosition = yStart;
        float rowHeight = 20f;
        float cellMargin = 5f;

        // Calculate column widths as a proportion of the tableWidth
        float[] colWidths = {
            tableWidth * 0.1f,  // 10% for ID
            tableWidth * 0.4f,  // 40% for Descrição
            tableWidth * 0.1f,  // 10% for Estoque
            tableWidth * 0.15f, // 15% for ID Fornecedor
            tableWidth * 0.25f  // 25% for Status
        };

        // Draw table headers
        contentStream.beginText();
        contentStream.newLineAtOffset(margin + cellMargin, yPosition);
        contentStream.showText("ID");
        contentStream.newLineAtOffset(colWidths[0], 0);
        contentStream.showText("Descrição");
        contentStream.newLineAtOffset(colWidths[1], 0);
        contentStream.showText("Estoque");
        contentStream.newLineAtOffset(colWidths[2], 0);
        contentStream.showText("ID Fornecedor");
        contentStream.newLineAtOffset(colWidths[3], 0);
        contentStream.showText("Status");
        contentStream.endText();

        yPosition -= rowHeight;

        // Draw table content
        for (ProdutoMySQL produto : produtos) {
            if (yPosition < margin) { // Check if a new page is needed
                contentStream.close();
                page = new PDPage();
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                contentStream.setFont(minhaFonte, 12);
                yPosition = yStart;
            }

            contentStream.beginText();
            contentStream.newLineAtOffset(margin + cellMargin, yPosition);
            contentStream.showText(String.valueOf(produto.getId_produtos()));
            contentStream.newLineAtOffset(colWidths[0], 0);
            contentStream.showText(produto.getDescricao());
            contentStream.newLineAtOffset(colWidths[1], 0);
            contentStream.showText(String.valueOf(produto.getEstoque()));
            contentStream.newLineAtOffset(colWidths[2], 0);
            contentStream.showText(String.valueOf(produto.getId_fornecedor()));
            contentStream.newLineAtOffset(colWidths[3], 0);
            contentStream.showText(produto.getStatus());
            contentStream.endText();

            yPosition -= rowHeight;
        }

        contentStream.close();

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(btnGerarPDF.getScene().getWindow());

        if (file != null) {
            document.save(file);
        }
        document.close(); // Certifique-se de fechar o documento fora do bloco if
    }
}
