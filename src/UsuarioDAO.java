import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public boolean validar(String nome_usuario, String senha) throws SQLException{
        String query = "SELECT * FROM usuarios WHERE nome_usuario = ? AND senha = ?";
        try (Connection connection = conexao.ConnectDB(); PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, nome_usuario);
            statement.setString(2, senha);

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();

        } catch (SQLException e){
            e.printStackTrace();

        }

        return false;

    }

    public boolean registrar(String nome_completo, String nome_usuario, String senha, String email) throws SQLException {
        String query = "INSERT INTO usuarios (nome_completo, nome_usuario, senha, email) VALUES (?, ?, ?, ?)";
        try (Connection connection = conexao.ConnectDB(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nome_completo);
            statement.setString(2, nome_usuario);
            statement.setString(3, senha);
            statement.setString(4, email);

            int rowInserted = statement.executeUpdate();
            return rowInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}
