package view;

import controle.ControleEmpresa;
import controle.ElementoInexistenteException;
import controle.IdRepetidoException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Collection;

abstract class DetalheView {
    protected final ControleEmpresa controleEmpresa;
    protected final JFrame janela = new JFrame();
    protected final PesquisaView pesquisaView;
    protected final ModosDetalhe modo;
    protected final JButton botaoAtualizar = new JButton("Atualizar");
    protected final JButton botaoExcluir = new JButton("Excluir");
    protected final JButton botaoAdicionar = new JButton("Adicionar");
    protected final JButton botaoCancelar = new JButton("Cancelar");

    public DetalheView(ModosDetalhe modo, PesquisaView pesquisaView, ControleEmpresa controleEmpresa) {
        janela.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        janela.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (mensagemConfirmarSaida()) {
                    janela.dispose();
                }
            }
        });
        this.modo = modo;
        this.pesquisaView = pesquisaView;
        this.controleEmpresa = controleEmpresa;
    }

    protected void criarJanela(Collection<? extends JComponent> formularios, int width, int height, String tituloDaJanela) {
        janela.setTitle(tituloDaJanela);
        janela.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // FORMULARIOS PRINCIPAIS
        c.weightx = 0.5;
        c.weighty = 0.2;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.NORTH;
        for (JComponent formulario : formularios) {
            janela.add(formulario, c);
            c.gridy++;
        }

        // PAINEL DE BOTÕES
        c.weighty = 0.6;
        c.anchor = GridBagConstraints.FIRST_LINE_END;
        c.insets = new Insets(10,0, 0, 5);
        janela.add(
                new PainelDetalheBotoes(
                        botaoAdicionar,
                        botaoCancelar, botaoAtualizar,
                        botaoExcluir,
                        modo,
                        new GerenciarElementoListener()
                )
                ,c);

        // HABILITAR JANELA
        janela.setSize(width, height);
        janela.setResizable(false);
        janela.setVisible(true);
        janela.setLocationRelativeTo(null);

    }

    abstract protected List<JComponent> agruparTodosFormularios();

    abstract protected void excluirElemento() throws ElementoInexistenteException;

    abstract protected void adicionarElemento() throws IdRepetidoException;

    abstract protected void atualizarElemento() throws IdRepetidoException, ElementoInexistenteException;

    abstract protected void popularFormularios();

    protected void enviarFormularios() throws IdRepetidoException, NumberFormatException, NullPointerException {
        switch (modo) {
            case ADICIONAR -> adicionarElemento();
                case EDITAR -> {
                    try {
                        atualizarElemento();
                    } catch (ElementoInexistenteException e) {
                        mensagemElementoInexistente(e);
                        pesquisaView.refresh();
                        janela.dispose();
                    }
                }

        }
        pesquisaView.refresh();
    }

    class GerenciarElementoListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Object src = e.getSource();

            if (src == botaoAtualizar || src == botaoAdicionar) {
                try {
                    enviarFormularios();
                } catch (NumberFormatException e1) {
                    mensagemErrodeFormatacao();
                    e1.printStackTrace();
                } catch (NullPointerException e2) {
                    mensagemErroFormularioVazio();
                    e2.printStackTrace();
                } catch (IdRepetidoException e3) {
                    mensagemErroIdRepetido(e3);
                    e3.printStackTrace();
                }
            } else {
                if (src == botaoExcluir) {
                    if (mensagemConfirmarExclusao()) {
                        try {
                            excluirElemento();
                            janela.dispose();
                        } catch (ElementoInexistenteException e1) {
                            mensagemElementoInexistente(e1);
                        }
                    }
                } else {
                    if (mensagemConfirmarSaida()) {
                        janela.dispose();
                    }
                }
            }
        }
    }

    protected boolean mensagemConfirmarSaida() {
        String[] botoes = {"Sim", "Não"};
        int escolhaPrompt = JOptionPane.showOptionDialog(null,
                "Confirmar saída? Dados não salvos serão descartados",
                "Confirmar saída",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                botoes,
                botoes[1]);
        return escolhaPrompt == JOptionPane.YES_OPTION;
    }

    protected boolean mensagemConfirmarExclusao() {
        String[] botoes = {"Sim", "Não"};
        int escolhaPrompt = JOptionPane.showOptionDialog(null,
                "Confirmar exclusão?",
                "Confirmar saída",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                botoes,
                botoes[1]);
        return escolhaPrompt == JOptionPane.YES_OPTION;
    }

    protected void mensagemElementoInexistente(ElementoInexistenteException e) {
        JOptionPane.showMessageDialog(null,
                "Elemento inexistente: "+e.getMessage(),
                "Elemento inexistente", JOptionPane.ERROR_MESSAGE);
    }

    protected void mensagemErrodeFormatacao() {
        JOptionPane.showMessageDialog(null,
                "Erro de formatação: assegure-se que valores numéricos foram inseridos corretamente.",
                "Erro de formatação", JOptionPane.ERROR_MESSAGE);
    }

    protected void mensagemErroFormularioVazio() {
        JOptionPane.showMessageDialog(null,
                "Erro de entrada: assegure-se que todos os formulários foram preenchidos.",
                "Erro de entrada", JOptionPane.ERROR_MESSAGE);
    }

    // --POP UPS--
    protected void mensagemErroIdRepetido(IdRepetidoException e3) {
        JOptionPane.showMessageDialog(
                null, e3.getMessage(), "Erro de indentificação", JOptionPane.ERROR_MESSAGE
        );
    }
}
