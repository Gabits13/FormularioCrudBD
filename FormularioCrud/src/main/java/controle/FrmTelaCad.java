package controle;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.text.ParseException;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import conexao.Conexao;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Gabriel Santos
 */
public class FrmTelaCad extends JFrame {

    Conexao con_cliente;

    JLabel rCodigo, rNome, rEmail, rTel, rData, rPesquisa;
    JTextField tcodigo, tnome, temail, tPesquisa;
    JFormattedTextField tel, data;
    MaskFormatter mTel, mData;

    JTable tblClientes; // datagrid
    JScrollPane scp_tabela; // container para o datagrid

    JButton bPrim, bAnt, bProx, bUlt; // Botões de navegação
    JButton bNovo, bGravar, bAlterar, bExcluir, bPesquisar; // Botões de ação
     JButton botaosair; //botao sair
    
    public FrmTelaCad() {
        con_cliente = new Conexao(); // inicialização do objeto
        con_cliente.conecta(); // chama o método que conecta

        Container tela = getContentPane();
        tela.setLayout(null);
        setTitle("Banco de Dados dos Clientes");
        setResizable(false);
        
         // Definir o ícone da janela
        ImageIcon img = new ImageIcon("src/img/favicon_database.png");
        setIconImage(img.getImage());

        // Labels
        rCodigo = new JLabel("Código:");
        rNome = new JLabel("Nome:");
        rData = new JLabel("Data:");
        rTel = new JLabel("Telefone:");
        rEmail = new JLabel("Email:");
        rPesquisa = new JLabel("Pesquisar pelo nome do Cliente:");

        rCodigo.setBounds(50, 20, 80, 25);
        rNome.setBounds(50, 60, 80, 25);
        rData.setBounds(50, 100, 80, 25);
        rTel.setBounds(50, 140, 80, 25);
        rEmail.setBounds(50, 180, 80, 25);
        rPesquisa.setBounds(50, 450, 200, 25);

        tela.add(rCodigo);
        tela.add(rNome);
        tela.add(rData);
        tela.add(rTel);
        tela.add(rEmail);
        tela.add(rPesquisa);

        // Text Fields
        tcodigo = new JTextField();
        tnome = new JTextField();
        temail = new JTextField();
        tPesquisa = new JTextField();

        tcodigo.setBounds(150, 20, 200, 25);
        tnome.setBounds(150, 60, 200, 25);
        temail.setBounds(150, 180, 200, 25);
        tPesquisa.setBounds(250, 450, 560, 25);

        tela.add(tcodigo);
        tela.add(tnome);
        tela.add(temail);
        tela.add(tPesquisa);

        // Formatted Text Fields
        try {
            mTel = new MaskFormatter("(##)####-####");
            mData = new MaskFormatter("##/##/####");
            mTel.setPlaceholderCharacter('_');
            mData.setPlaceholderCharacter('_');
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tel = new JFormattedTextField(mTel);
        data = new JFormattedTextField(mData);

        tel.setBounds(150, 140, 200, 25);
        data.setBounds(150, 100, 200, 25);

        tela.add(tel);
        tela.add(data);
        
        
         // Carregar ícones
        ImageIcon iconePesquisar = new ImageIcon("src/img/pesquisar.png");
        ImageIcon iconeAnterior = new ImageIcon("src/img/anterior.png");
        ImageIcon iconeProximo = new ImageIcon("src/img/proximo.png");
        ImageIcon iconePrimeiro = new ImageIcon("src/img/primeiro.png");
        ImageIcon iconeUltimo = new ImageIcon("src/img/ultimo.png");
        ImageIcon iconeNovo = new ImageIcon("src/img/novo.png");
        ImageIcon iconeGravar = new ImageIcon("src/img/gravar.png");
        ImageIcon iconeAlterar = new ImageIcon("src/img/alterar.png");
        ImageIcon iconeExcluir = new ImageIcon("src/img/excluir.png");
        ImageIcon iconeSair = new ImageIcon("src/img/sair.png");

        // Botões de navegação
        bPrim = new JButton("Primeiro" , iconePrimeiro); // Sem ícone
        bAnt = new JButton("Anterior", iconeAnterior);
        bProx = new JButton("Próximo", iconeProximo);
        bUlt = new JButton("Último", iconeUltimo); // Sem ícone

        bPrim.setBounds(200, 500, 120, 25);
        bAnt.setBounds(350, 500, 120, 25);
        bProx.setBounds(500, 500, 120, 25);
        bUlt.setBounds(650, 500, 120, 25);

        tela.add(bPrim);
        tela.add(bAnt);
        tela.add(bProx);
        tela.add(bUlt);


         // Botões de ação
        bNovo = new JButton("Novo Registro", iconeNovo);
        bNovo.setBounds(450, 20, 180, 50);
        tela.add(bNovo);

        bGravar = new JButton("Salvar", iconeGravar);
        bGravar.setBounds(650, 20, 180, 50);
        tela.add(bGravar);

        bAlterar = new JButton("Alterar", iconeAlterar);
        bAlterar.setBounds(450, 120, 180, 50);
        tela.add(bAlterar);

        bExcluir = new JButton("Excluir", iconeExcluir);
        bExcluir.setBounds(650, 120, 180, 50);
        tela.add(bExcluir);

        bPesquisar = new JButton("Pesquisar", iconePesquisar);
        bPesquisar.setBounds(825, 450, 120, 25);
        tela.add(bPesquisar);

        botaosair = new JButton("Sair", iconeSair);
        botaosair.setBounds(845, 670, 100, 25);
        tela.add(botaosair);
        
        // JTable
        tblClientes = new JTable();
        scp_tabela = new JScrollPane(tblClientes);

        tblClientes.setModel(new DefaultTableModel(
                new Object[][]{
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null}
                },
                new String[]{"Código", "Nome", "Data de Nascimento", "Telefone", "Email"}) {
            boolean[] canEdit = new boolean[]{false, false, false, false, false};

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

        tblClientes.setBorder(BorderFactory.createLineBorder(Color.black));
        tblClientes.setFont(new Font("Arial", 1, 12));
        tblClientes.setAutoCreateRowSorter(true); // ativa a classificação ordenada da tabela

        scp_tabela.setBounds(50, 220, 900, 200);
        tela.add(scp_tabela);

        // Janela
        setSize(1000, 800);
        setLocationRelativeTo(null); // Centraliza a janela na tela
        setVisible(true);

        con_cliente.executaSQL("select * from tbclientes order by cod");
        preencherTabela();
        posicionarRegistro();

        // Adiciona ação aos botões
        bPrim.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    con_cliente.resultset.first();
                    mostrar_Dados();
                } catch (SQLException erro) {
                    JOptionPane.showMessageDialog(null, "Não foi possível acessar o primeiro registro: " + erro, "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        bAnt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    con_cliente.resultset.previous();
                    mostrar_Dados();
                } catch (SQLException erro) {
                    JOptionPane.showMessageDialog(null, "Não foi possível acessar o registro anterior: " + erro, "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        bProx.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    con_cliente.resultset.next();
                    mostrar_Dados();
                } catch (SQLException erro) {
                    JOptionPane.showMessageDialog(null, "Não foi possível acessar o próximo registro: " + erro, "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        bUlt.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    con_cliente.resultset.last();
                    mostrar_Dados();
                } catch (SQLException erro) {
                    JOptionPane.showMessageDialog(null, "Não foi possível acessar o último registro: " + erro, "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        bNovo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tcodigo.setText("");
                tnome.setText("");
                tel.setText("");
                temail.setText("");
                data.setText("");
                tcodigo.requestFocus();
            }
        });

        bGravar.addActionListener(new ActionListener() {
             
            
            public void actionPerformed(ActionEvent e) {
                try {
                     String codigo = tcodigo.getText();
                     String nome = tnome.getText();
                     String telefone = tel.getText();
                     String email = temail.getText();
                     String dt_nasc = data.getText();
                     
                    String insert_sql="insert into tbclientes (nome,telefone, email, dt_nasc) values ('" + nome + "','" + telefone + "','" + email + "','" + dt_nasc + "')";
                    con_cliente.statement.executeUpdate(insert_sql);
                     JOptionPane.showMessageDialog(null, "Gravação realizada com sucesso!", "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
                    con_cliente.executaSQL("select * from tbclientes order by cod");
                    preencherTabela();
                
                } catch (SQLException erro) {
                    JOptionPane.showMessageDialog(null, "Erro ao tentar gravar o registro: " + erro, "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        bAlterar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String codigo = tcodigo.getText();
                    String nome = tnome.getText();
                    String telefone = tel.getText();
                    String email = temail.getText();
                    String dt_nasc = data.getText();

                    String sql = "UPDATE tbclientes SET nome='" + nome + "', telefone='" + telefone + "', email='" + email + "', dt_nasc='" + dt_nasc + "' WHERE cod=" + codigo;
                    con_cliente.statement.executeUpdate(sql);

                    JOptionPane.showMessageDialog(null, "Alteração realizada com sucesso!", "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);

                    con_cliente.executaSQL("select * from tbclientes order by cod");
                    preencherTabela();
                    posicionarRegistro();

                } catch (SQLException erro) {
                    JOptionPane.showMessageDialog(null, "Erro na alteração: " + erro, "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        bExcluir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String codigo = tcodigo.getText();
                    int resposta = JOptionPane.showConfirmDialog(null, "Deseja excluir o registro?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION, 3);

                    if (resposta == JOptionPane.YES_OPTION) {
                        String delete_sql = "DELETE FROM tbclientes WHERE cod = " + codigo;
                        con_cliente.statement.executeUpdate(delete_sql);
                        JOptionPane.showMessageDialog(null, "Exclusão realizada com sucesso!", "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);

                        con_cliente.executaSQL("select * from tbclientes order by cod");
                        preencherTabela();
                        posicionarRegistro();
                    } else {
                        JOptionPane.showMessageDialog(null, "Operação cancelada pelo usuário!", "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (SQLException erro) {
                    JOptionPane.showMessageDialog(null, "Erro na exclusão: " + erro, "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

      bPesquisar.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        try {
            // Atualiza a query para buscar apenas nomes que comecem com a letra digitada
            String pesquisa = "select * from tbclientes where nome like '" + tPesquisa.getText() + "%'";
           //ta dando erro ainda e nao ta buscando, temo que o erro nao esta aqui e sim
            con_cliente.executaSQL(pesquisa); //na parte que preenche a tabela quando for pesquisar
            if (con_cliente.resultset.first()) {
                preencherTabela();
            } else {
                JOptionPane.showMessageDialog(null, "Não existem dados com este parâmetro!", "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException errosql) {
            JOptionPane.showMessageDialog(null, "Erro na pesquisa: " + errosql, "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
        }
    }
});
     
         botaosair.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int opcao;

                Object[] botoes = {"Sim", "Não"};
                opcao = JOptionPane.showOptionDialog(null, "Deseja mesmo fechar a janela?", "Fechar",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, botoes, botoes[0]);
                
                if (opcao == JOptionPane.YES_OPTION)
                    System.exit(0);
            }
        });
        
         //tool tips
        
        bPrim.setToolTipText("Vai pro Primeiro registro");
        bAnt.setToolTipText("Vai pro registro anterior");
        bProx.setToolTipText("Vai pro proximo registro");
        bUlt.setToolTipText("Vai pro ultimo registro");
        bNovo.setToolTipText("Limpa as caixas de texto para um novo registro");
        bGravar.setToolTipText("Salva o novo registro");
        bAlterar.setToolTipText("Altera o registro selecionado");
        bExcluir.setToolTipText("Exclui o registro selecionado"); 
        bPesquisar.setToolTipText("Pesquisa no banco de dados pelo o que foi digitado");
        botaosair.setToolTipText("Botão que será usado para sair");
        
    }

    private void mostrar_Dados() {
        try {
            tcodigo.setText(con_cliente.resultset.getString("cod"));
            tnome.setText(con_cliente.resultset.getString("nome"));
            tel.setText(con_cliente.resultset.getString("telefone"));
            temail.setText(con_cliente.resultset.getString("email"));
            data.setText(con_cliente.resultset.getString("dt_nasc"));
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao tentar mostrar os dados: " + erro, "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void preencherTabela() {
        
        tblClientes.getColumnModel().getColumn(0).setPreferredWidth(4);
        tblClientes.getColumnModel().getColumn(1).setPreferredWidth(150);
        tblClientes.getColumnModel().getColumn(2).setPreferredWidth(11);
        tblClientes.getColumnModel().getColumn(3).setPreferredWidth(14);
        tblClientes.getColumnModel().getColumn(4).setPreferredWidth(100);
        
         DefaultTableModel modelo = (DefaultTableModel) tblClientes.getModel();
         modelo.setNumRows(0);
        try {
           

            con_cliente.resultset.beforeFirst();
            while (con_cliente.resultset.next()) {
                modelo.addRow(new Object[]{
                        con_cliente.resultset.getString("cod"),
                        con_cliente.resultset.getString("nome"),
                        con_cliente.resultset.getString("dt_nasc"),
                        con_cliente.resultset.getString("telefone"),
                        con_cliente.resultset.getString("email")
                });
            }
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro ao listar na tabela: " + erro, "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void posicionarRegistro() {
        try {
            con_cliente.resultset.first();
            mostrar_Dados();
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Não foi possível posicionar o primeiro registro: " + erro, "Mensagem do Programa", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new FrmTelaCad();
    }
}
