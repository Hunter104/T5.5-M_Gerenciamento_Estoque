package view;

import controle.ControleEstoqueFilial;
import controle.ElementoInexistenteException;
import controle.IdRepetidoException;
import modelo.Farmaceutico;
import modelo.NivelRestricaoInadequadoException;

import javax.swing.*;
import java.util.Arrays;

/**
 * Painel formulário com os campos de Farmaceutico, herda da classe PainelFormulario
 *
 * @author André Emanuel Bipo da Silva
 * @version 1.0
 * @see PainelFormulario
 * @since 2023
 */
public class PainelFormularioFarmaceutico extends PainelFormulario {
    private final JTextField valorTarja = new JTextField();
    private final JTextField valorComposicao = new JTextField();
    private final JCheckBox isRetencaoDeReceita = new JCheckBox("Retenção de receita");
    private final JCheckBox isGenerico = new JCheckBox("Medicamento genérico");
    private final JCheckBox isReceita = new JCheckBox("Necessita de receita");

    /**
     * Cria um novo Painel com os atributos de farmacêutico
     */
    public PainelFormularioFarmaceutico() {
        criarFormulario(
                Arrays.asList(
                        new JLabel("Tarja: "),
                        new JLabel("Composição: ")),
                Arrays.asList(
                        valorTarja,
                        valorComposicao,
                        isReceita,
                        isRetencaoDeReceita,
                        isGenerico)
                , "Detalhes - Farmacêutico");
    }

    /**
     * Atualiza as informações de um farmacêutico no estoque
     *
     * @param controleEstoque controle do estoque
     * @param itemEscolhido   farmacêutico a ser atualizado
     * @throws ElementoInexistenteException gera uma exceção caso farmacêutico escolhido não exista
     */
    @Deprecated
    public void atualizarFarmaceutico(ControleEstoqueFilial controleEstoque, Farmaceutico itemEscolhido) throws ElementoInexistenteException {
        controleEstoque.atualizarFarmaceutico(
                valorTarja.getText(),
                valorComposicao.getText(),
                isReceita.isSelected(),
                isRetencaoDeReceita.isSelected(),
                isGenerico.isSelected(),
                itemEscolhido
        );
    }

    public void atualizarFarmaceutico(PainelFormularioItem painelFormularioItem, ControleEstoqueFilial controleEstoque, Farmaceutico itemEscolhido) throws ElementoInexistenteException, NivelRestricaoInadequadoException, IdRepetidoException {
        if (painelFormularioItem.getIsRestrito().isSelected()) {
            itemEscolhido.restringir();
        } else {
            itemEscolhido.liberar();
        }
        controleEstoque.atualizarCaracteristicasBasicas(
                painelFormularioItem.getValorNome().getText(),
                painelFormularioItem.getValorCategoria().getText(),
                Double.parseDouble(painelFormularioItem.getValorValor().getText()),
                Integer.parseInt(painelFormularioItem.getValorQuantidade().getText()),
                Integer.parseInt(painelFormularioItem.getValorId().getText()),
                itemEscolhido
        );
        controleEstoque.atualizarFarmaceutico(
                valorTarja.getText(),
                valorComposicao.getText(),
                isReceita.isSelected(),
                isRetencaoDeReceita.isSelected(),
                isGenerico.isSelected(),
                itemEscolhido
        );
    }

    /**
     * Adiciona um novo produto químico ao estoque
     *
     * @param painelFormularioItem painel do item
     * @param controleEstoque      controle do estoque
     * @throws IdRepetidoException          gera uma exceção caso id do novo item seja igual a um existente
     * @throws ElementoInexistenteException gera uma exceção caso farmacêutico escolhido não exista
     */
    public void adicionarFarmaceutico(PainelFormularioItem painelFormularioItem, ControleEstoqueFilial controleEstoque) throws IdRepetidoException, ElementoInexistenteException {
        controleEstoque.adicionarFarmaceutico(
                painelFormularioItem.getValorNome().getText(),
                painelFormularioItem.getValorCategoria().getText(),
                Double.parseDouble(painelFormularioItem.getValorValor().getText()),
                Integer.parseInt(painelFormularioItem.getValorQuantidade().getText()),
                Integer.parseInt(painelFormularioItem.getValorId().getText()),
                valorTarja.getText(),
                valorComposicao.getText(),
                isReceita.isSelected(),
                isRetencaoDeReceita.isSelected(),
                isGenerico.isSelected()
        );
    }

    /**
     * Preenche os campos do formulário com as informações de um farmacêutico específico
     *
     * @param itemEscolhido farmacêutico escolhido
     */
    public void popularFormularios(Farmaceutico itemEscolhido) {
        valorComposicao.setText(itemEscolhido.getComposicao());
        valorTarja.setText(itemEscolhido.getTarja());
        isGenerico.setSelected(itemEscolhido.isGenerico());
        isReceita.setSelected(itemEscolhido.isReceita());
        isRetencaoDeReceita.setSelected(itemEscolhido.isRetencaoDeReceita());
    }
}
