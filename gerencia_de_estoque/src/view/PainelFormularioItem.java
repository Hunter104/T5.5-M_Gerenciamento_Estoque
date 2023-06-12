package view;


import controle.ControleEstoqueFilial;
import controle.IdRepetidoException;
import modelo.Filial;
import modelo.Item;
import modelo.NivelRestricaoInadequadoException;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

public class PainelFormularioItem extends PainelFormulario {
    private final JTextField valorNome = new JTextField();
    private final JTextField valorCategoria = new JTextField();
    private final JTextField valorValor = new JTextField();
    private final JTextField valorQuantidade = new JTextField();
    private final JTextField valorID = new JTextField();
    private final JCheckBox isRestrito = new JCheckBox("Restrito");
    private JComboBox<Filial> opcoesFiliais;
    private Filial filialdoItem;


    // Editar item de uma filial
    public PainelFormularioItem(Filial filialdoItem) {
        this.filialdoItem = filialdoItem;
        ArrayList<JComponent> direitos = inicializarComponentesDireitos();
        direitos.add(isRestrito);
        String titulo = "Informações básicas - Filial do item escolhido: " + filialdoItem.getNome();
        criarFormulario(inicializarComponentesEsquerdos(), direitos, titulo);
    }

    // ADICIONAR ITEM A UMA FILIAL
    public PainelFormularioItem() {
        String titulo = "Informações básicas - Filial do item escolhido: " + filialdoItem.getNome();
        criarFormulario(inicializarComponentesEsquerdos(), inicializarComponentesDireitos(), titulo);
    }

    // ADICIONAR ITEM GERAL
    public PainelFormularioItem(ArrayList<Filial> filiaisDisponiveis) {

        opcoesFiliais = new JComboBox<>(filiaisDisponiveis.toArray(new Filial[0]));

        ArrayList<JComponent> esquerdos = inicializarComponentesEsquerdos();
        ArrayList<JComponent> direitos = inicializarComponentesDireitos();
        esquerdos.add(new JLabel("Filial: "));
        direitos.add(opcoesFiliais);

        criarFormulario(esquerdos, direitos, "Adicionar informações básicas");

    }

    // Formulários só podem fazer duas coisas, receber dados, ou colocar dados
    public void atualizarCaracteristicasBasicas(ControleEstoqueFilial controleEstoque, Item itemEscolhido) throws IdRepetidoException {
        try {
            if (isRestrito.isSelected())
                itemEscolhido.restringir();
            else
                itemEscolhido.liberar();
            controleEstoque.atualizarCaracteristicasBasicas(
                    valorNome.getText(),
                    valorCategoria.getText(),
                    Double.parseDouble(valorValor.getText()),
                    Integer.parseInt(valorQuantidade.getText()),
                    Integer.parseInt(valorID.getText()),
                    itemEscolhido
            );
        } catch (NivelRestricaoInadequadoException e1){
            // Reiniciar o checkbox
            isRestrito.setSelected(itemEscolhido.isRestrito());
            mensagemErroRestricao(e1);
            e1.printStackTrace();
        }
    }

    public void popularFormularios(Item itemEscolhido) {
        valorNome.setText(itemEscolhido.getNome());
        valorCategoria.setText(itemEscolhido.getCategoria());
        valorValor.setText(String.valueOf(itemEscolhido.getValor()));
        valorQuantidade.setText(String.valueOf(itemEscolhido.getQuantidade()));
        valorID.setText(String.valueOf(itemEscolhido.getId()));
        isRestrito.setSelected(itemEscolhido.isRestrito());
    }

    private ArrayList<JComponent> inicializarComponentesEsquerdos() {
        return new ArrayList<>(Arrays.asList(
                new JLabel("Nome: "),
                new JLabel("ID: "),
                new JLabel("Categoria: "),
                new JLabel("Quantidade: "),
                new JLabel("Valor (R$): ")));
    }

    private ArrayList<JComponent> inicializarComponentesDireitos() {
        return new ArrayList<>(Arrays.asList(
                valorNome,
                valorID,
                valorCategoria,
                valorQuantidade,
                valorValor
            )
        );
    }

    private void mensagemErroRestricao(NivelRestricaoInadequadoException e) {
        JOptionPane.showMessageDialog(
                null, e.getMessage(), "Erro de restrição:", JOptionPane.ERROR_MESSAGE
        );
    }

    public Filial getSelectedFilial() {
        return (Filial) opcoesFiliais.getSelectedItem();
    }

    public JTextField getValorNome() {
        return valorNome;
    }

    public JTextField getValorCategoria() {
        return valorCategoria;
    }

    public JTextField getValorValor() {
        return valorValor;
    }

    public JTextField getValorQuantidade() {
        return valorQuantidade;
    }

    public JTextField getValorID() {
        return valorID;
    }
}