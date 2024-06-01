package Controllers;

import Model.Users;
import com.db4o.ObjectSet;
import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import javax.swing.JOptionPane;

public class Controller {
    static ObjectContainer db = Db4o.openFile("banco.dbo");
    public void menu() {
        String opcao = JOptionPane.showInputDialog(
                "Escolha uma opção:\n" +
                        "1 - Adicionar Usuário\n" +
                        "2 - Atualizar Usuário\n" +
                        "3 - Consultar Usuários\n" +
                        "4 - Excluir Usuário\n" +
                        "0 - Sair"
        );

        switch (opcao) {
            case "1":
                AddUser();
                break;

            case "2":
                UpdateUser();
                break;

            case "3":
                QueryUsers();
                break;

            case "4":
                DeleteUser();
                break;

            case "0":
                JOptionPane.showMessageDialog(null, "Saindo da aplicação.");
                db.close();
                return;

            default:
                JOptionPane.showMessageDialog(null, "Opção inválida. Tente novamente.");
                menu();

        }
    }

    private void DeleteUser() {
        String NomeAntigo = JOptionPane.showInputDialog("Digite o nome do usuário que você deseja deletar:");

        Users usuario_deletar = new Users(NomeAntigo, null, null, 0);
        ObjectSet<Users> resultado_busca = db.queryByExample(usuario_deletar);

        if (resultado_busca.hasNext()) {
            Users usuario_deletado = resultado_busca.next();
            db.delete(usuario_deletado);
            JOptionPane.showMessageDialog(null, "O usuário " + NomeAntigo + " foi deletado(a) com sucesso!");
            menu();
        } else {
            JOptionPane.showMessageDialog(null, "Usuário não encontrado, por favor digite um nome válido.");
            DeleteUser();
        }
    }

    private void QueryUsers() {
        ObjectSet<Users> resultadoConsulta = db.queryByExample(Users.class);
        StringBuilder usuarios = new StringBuilder("Usuários no banco de dados:\n");

        while (resultadoConsulta.hasNext()) {
            usuarios.append(resultadoConsulta.next()).append("\n");
        }

        JOptionPane.showMessageDialog(null, usuarios.toString());
        menu();
    }

    private void UpdateUser() {
        String NomeAntigo = JOptionPane.showInputDialog("Digite o nome do usuário que você deseja atualizar:");
        String EmailNovo = JOptionPane.showInputDialog("Digite seu novo email:");
        String SenhaNova = JOptionPane.showInputDialog("Digite sua nova senha:");
        int idadeNova = Integer.parseInt(JOptionPane.showInputDialog("Digite sua nova idade:"));

        Users usuario_atualizar = new Users(NomeAntigo, null, null, 0);
        ObjectSet<Users> resultadoAtualizacao = db.queryByExample(usuario_atualizar);

        if (resultadoAtualizacao.hasNext()) {
            Users usuario_atualizado = resultadoAtualizacao.next();
            usuario_atualizado.setEmail(EmailNovo);
            usuario_atualizado.setPassword(SenhaNova);
            usuario_atualizado.setAge(idadeNova);
            db.store(usuario_atualizado);
            JOptionPane.showMessageDialog(null, "Usuário atualizado: " + usuario_atualizado);
            menu();
        } else {
            JOptionPane.showMessageDialog(null, "Usuário não encontrado para atualização.");
            menu();
        }
    }

    private void AddUser() {
        String name = JOptionPane.showInputDialog("Digite o nome do usuário:");
        String email = JOptionPane.showInputDialog("Digite seu e-mail aqui:");
        String password = JOptionPane.showInputDialog("Digite sua senha aqui:");
        int age = Integer.parseInt(JOptionPane.showInputDialog("Digite a idade do usuário:"));

        Users novoUsuario = new Users(name, email, password, age);
        db.store(novoUsuario);
        JOptionPane.showMessageDialog(null, "Usuário adicionado: " + novoUsuario.toString());
        menu();
    }
}
