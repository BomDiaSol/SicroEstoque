import javafx.beans.property.SimpleIntegerProperty;

public class ProdutoMySQL {
    public enum Status {
        ATIVO("ATIVO"),
        INATIVO("INATIVO");

        private String status;

        Status(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return status;
        }
    }

    private final SimpleIntegerProperty id;
    private int estoque;
    private int id_fornecedor;
    private String descricao;
    private String status;

    public ProdutoMySQL(int id, int estoque, int id_fornecedor, String descricao, String status) {
        this.id = new SimpleIntegerProperty(id);
        this.estoque = estoque;
        this.id_fornecedor = id_fornecedor;
        this.descricao = descricao;
        this.status = status;
    }

    // Getters and setters
    public int getId_produtos() { return id.get(); }
    public SimpleIntegerProperty idProperty() { return id; }
    public void setId(int id) { this.id.set(id); }
    public int getEstoque() { return estoque; }
    public void setEstoque(int estoque) { this.estoque = estoque; }
    public int getId_fornecedor() { return id_fornecedor; }
    public void setId_fornecedor(int id_fornecedor) { this.id_fornecedor = id_fornecedor; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

