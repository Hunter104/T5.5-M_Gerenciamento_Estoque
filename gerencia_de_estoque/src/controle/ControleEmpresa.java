package controle;

import modelo.Empresa;
import modelo.Filial;
import modelo.Item;

import java.util.ArrayList;
import java.util.List;

public class ControleEmpresa {
    private final Empresa empresa;
    private final ArrayList<Filial> filiais;

    public ControleEmpresa(String nome) {
        empresa = new Empresa(nome);
        filiais = empresa.getFiliais();
    }

    public ArrayList<Filial> getFiliais() {
        return filiais;
    }

    public ArrayList<Item> getEstoque() {
        return empresa.lerTodoEstoque();
    }

    public ArrayList<Item> getEstoqueVazio() {
        List<Item> estoqueLista = empresa
                .lerTodoEstoque()
                .stream()
                .filter(item -> item.getQuantidade() == 0).toList();
        return new ArrayList<>(estoqueLista);
    }

    //TODO: essa função é de teste
    public void adicionarFilial(Filial f) throws IdRepetidoException {
        boolean idRepetido = filiais.stream().anyMatch(filial -> filial.getId() == f.getId());
        if (idRepetido) {
            throw new IdRepetidoException("Id repetido: a empresa já contém uma filial com esse Id");
        } else {
            empresa.adicionarFilial(f);
        }
    }

    public void adicionarFilial(String nome, String local, int id) throws IdRepetidoException {
        boolean idRepetido = filiais.stream().anyMatch(filial -> filial.getId() == id);
        if (idRepetido) {
            throw new IdRepetidoException("Id repetido: a empresa já contém uma filial com esse Id");
        } else {
            empresa.adicionarFilial(nome, local, id);
        }
    }

    public void atualizarFilial(String newNome, String newLocal, int newId, Filial f) throws IdRepetidoException {
        boolean idRepetido = filiais
                .stream()
                // Atualizar se não houver uma filial na lista com o mesmo id, *Exceto ela mesma*
                .anyMatch(filial -> (filial.getId() == newId) && (!filial.equals(f)));
        if (idRepetido) {
            throw new IdRepetidoException("Id repetido: a empresa já contém uma filial com esse Id");
        } else {
            /* Se eu fosse utilizar o método atualizarFilial pela empresa
            o código ficaria redundante, pois o método iria buscar a filial pelo ID sendo que já
            temos uma referência a filial escolhida
             */
            f.setId(newId);
            f.setNome(newNome);
            f.setLocal(newLocal);
        }

    }

    // Essa função assume que todos os itens têm identificação única
    // TODO: atualizar o método equals de cada item para ter uma função mais flexível
    public Filial buscarFilialaPartirdeItem(Item item) {
        return filiais.stream()
                .filter(filial -> filial.getEstoque().contains(item))
                .findFirst()
                .orElseThrow();
    }

    public String getNome() {
        return empresa.getNome();
    }

    public Filial buscarFilial(int id) {
        return empresa.lerFilial(id);
    }

    public void excluirFilial(Filial f) {
        empresa.removerFilial(f);
    }

    public void excluirFilial(int id) {
        empresa.removerFilial(id);
    }


}
