package controle;
import modelo.*;

public class ControleEmpresa {
    private Empresa empresaMain;

    public ControleEmpresa(String nome) {
        empresaMain = new Empresa(nome);
    }

    public void renomearEmpresa(String novoNome) {
        empresaMain.setNome(novoNome);
    }

    public String getNomeEmpresa() {
        return empresaMain.getNome();
    }
    public Empresa getEmpresaMain() {
        return empresaMain;
    }

    public void setEmpresaMain(Empresa empresaMain) {
        this.empresaMain = empresaMain;
    }

    public int getQtdFilial() {
        return empresaMain.getFiliais().size();
    }

    public int getQtdEmEstoque() {
        return empresaMain.lerTodoEstoque().size();
    }

}
