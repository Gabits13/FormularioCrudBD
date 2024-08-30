// Esta classe será responsável pela conexão com o MySQL e terá 3 métodos:
// 1 - método 1: abertura da conexão
// 2 - método 2: fechamento da conexão
// 3 - método 3: execução de comandos SQL

package conexao;

import javax.swing.JOptionPane;
import java.sql.*; // para execução de comandos SQL no ambiente Java

public class Conexao {
    final private String driver = "com.mysql.cj.jdbc.Driver"; // definição do driver MySQL para acesso aos dados
    final private String url = "jdbc:mysql://localhost/clientes"; // acesso ao bd "clientes" no servidor (myAdmin)
    final private String usuario = "root"; // usuário do MySQL
    final private String senha = ""; // senha do MySQL
    private Connection conexao; // variável que armazenará a conexão aberta
    public Statement statement; // variável para execução dos comandos SQL dentro do ambiente Java
    public ResultSet resultset; // variável que armazenará o resultado da execução de um comando SQL

    public boolean conecta() {
        boolean result = true;
        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, usuario, senha);
            JOptionPane.showMessageDialog(null, "Conexão estabelecida", "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
        } catch (ClassNotFoundException Driver) {
            JOptionPane.showMessageDialog(null, "Driver não localizado: " + Driver, "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE); 
            result = false;
        } catch (SQLException Fonte) {
            JOptionPane.showMessageDialog(null, "Fonte de dados não localizada: " + Fonte, "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
            result = false;  
        }
        return result;
    }

    public void desconecta() {
        try {
            conexao.close();
            JOptionPane.showMessageDialog(null, "Conexão com banco fechada", "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException fecha) {
            JOptionPane.showMessageDialog(null, "Erro ao fechar o banco: " + fecha, "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);   
        }
    }

    public void executaSQL(String sql) {
        try {
            statement = conexao.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultset = statement.executeQuery(sql);
        } catch (SQLException excecao) {
            JOptionPane.showMessageDialog(null, "Erro na execução do comando SQL:\n " + excecao, "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
