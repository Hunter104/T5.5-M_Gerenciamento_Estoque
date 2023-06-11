package view;

import controle.ControleEmpresa;
import controle.ControleEstoque;
import controle.ControleEstoqueFilial;
import modelo.Filial;
import modelo.Item;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class PesquisaEstoqueView extends PesquisaView {
    private final JCheckBox filtroEstoqueVazio = new JCheckBox("Filtrar por estoque vazio");
    private final ModosPesquisa modo;
    private final JTextField valorPesquisaNomeDeItem = new JTextField();
    private final ControleEstoque controleEstoque;
    private JList<Item> listaEstoque;

    public PesquisaEstoqueView(ControleEmpresa controleEmpresa) {
        modo = ModosPesquisa.LISTAR_ESTOQUE_GERAL;
        this.controleEmpresa = controleEmpresa;
        this.controleEstoque = controleEmpresa;
        iniciarJanelaEstoque("Estoque", "Estoque geral");

    }

    public PesquisaEstoqueView(ControleEmpresa controleEmpresa, Filial filialEscolhida) {
        modo = ModosPesquisa.LISTAR_ESTOQUE_FILIAL;
        controleEstoque = new ControleEstoqueFilial(controleEmpresa, filialEscolhida);
        this.controleEmpresa = controleEmpresa;
        iniciarJanelaEstoque(
                "Visualizando Estoque de: " + filialEscolhida,
                "Estoque de " + filialEscolhida
        );
    }

    private void iniciarJanelaEstoque(String tituloJanela, String tituloPainel) {
        // Definição dos componentes
        valorPesquisaNomeDeItem.getDocument().addDocumentListener(new FiltrosListener());

        botaoAdicionar = new JButton("Adicionar Item");
        botaoVerDetalhes = new JButton("Ver Item");
        switch (modo) {
            case LISTAR_ESTOQUE_GERAL ->{
                botaoVerDetalhes.addActionListener(new ListarEstoqueGeralListener());
                botaoAdicionar.addActionListener(new ListarEstoqueGeralListener());
            }
            case LISTAR_ESTOQUE_FILIAL -> {
                botaoVerDetalhes.addActionListener(new ListarEstoqueFilialListener());
                botaoAdicionar.addActionListener(new ListarEstoqueFilialListener());
            }
        }

        listaEstoque = new JList<>(controleEstoque.getEstoque().toArray(new Item[0]));
        listaEstoque.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaEstoque.setVisibleRowCount(10);

        filtroEstoqueVazio.addItemListener(new FiltrosListener());

        // -- CRIAÇÃO DO PAINEL DE PESQUISA POR NOME --
        // Nome: [                  ]
        JPanel painelPesquisa = new JPanel();
        painelPesquisa.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.weightx = 0.1;
        c.anchor = GridBagConstraints.LINE_START;
        painelPesquisa.add(new JLabel("Nome: "), c);

        c.weightx = 0.9;
        c.anchor = GridBagConstraints.LINE_END;
        c.fill = GridBagConstraints.HORIZONTAL;
        painelPesquisa.add(valorPesquisaNomeDeItem, c);

        // Criar janela de pesquisa
        JFrame janela = new JFrame(tituloJanela);
        janela.add(
                new PainelPesquisa(
                        tituloPainel,
                        listaEstoque,
                        new JButton[]{botaoAdicionar, botaoVerDetalhes},
                        new JComponent[]{filtroEstoqueVazio, painelPesquisa}
                )
        );

        janela.setSize(400, 400);
        janela.setResizable(false);
        janela.setVisible(true);
    }

    @Override
    public void refresh() {
            ArrayList<Item> estoqueApenasVazios;
            String nomePesquisado = valorPesquisaNomeDeItem.getText();
            ArrayList<Item> estoquePrincipal = controleEstoque.buscarItensParcial(nomePesquisado, false);

            if (filtroEstoqueVazio.isSelected()) {
                estoqueApenasVazios = controleEstoque.getItensVazios(estoquePrincipal);
                listaEstoque.setListData(estoqueApenasVazios.toArray(new Item[0]));
            } else {
                listaEstoque.setListData(estoquePrincipal.toArray(new Item[0]));
            }
            listaEstoque.updateUI();
    }

    // --POP UPS--
    @Override
    protected void mensagemErroEscolhaVazia() {
        String mensagem = "Erro de escolha: um item não foi selecionado";
        JOptionPane.showMessageDialog(null, mensagem, "Erro de escolha", JOptionPane.ERROR_MESSAGE);
    }

    private class ListarEstoqueGeralListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object src = e.getSource();
            try {
                if (src == botaoAdicionar) {
                    new DetalheItem(controleEmpresa, PesquisaEstoqueView.this);
                } else if (src == botaoVerDetalhes) {
                    Item itemEscolhido = listaEstoque.getSelectedValue();
                    new DetalheItem(controleEmpresa, PesquisaEstoqueView.this, itemEscolhido);
                }
            } catch (NullPointerException | NoSuchElementException exc) {
                mensagemErroEscolhaVazia();
            }
        }
    }

    private class ListarEstoqueFilialListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object src = e.getSource();
            try {
                if (src == botaoVerDetalhes) {
                    Item itemEscolhido = listaEstoque.getSelectedValue();
                    new DetalheItem(controleEmpresa, PesquisaEstoqueView.this, itemEscolhido);
                } else if (src == botaoAdicionar) {
                    ControleEstoqueFilial filialGerenciada = (ControleEstoqueFilial) controleEstoque;
                    new DetalheItem(controleEmpresa, PesquisaEstoqueView.this, filialGerenciada);
                }
            } catch (NullPointerException | NoSuchElementException exc) {
                mensagemErroEscolhaVazia();
            }
        }
    }

    private class FiltrosListener implements DocumentListener, ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            refresh();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            refresh();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            refresh();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            refresh();
        }
    }

}