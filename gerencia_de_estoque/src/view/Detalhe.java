package view;

import controle.ControleEmpresa;
import controle.IdRepetidoException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;

abstract class Detalhe implements ActionListener {
    protected final ControleEmpresa controleEmpresa;
    protected final JFrame janela = new JFrame();
    protected final PesquisaView pesquisaView;
    protected final ModosDetalhe modo;
    protected final JButton botaoAtualizar = new JButton("Atualizar");
    protected final JButton botaoExcluir = new JButton("Excluir");
    protected final JButton botaoAdicionar = new JButton("Adicionar");
    protected final JButton botaoCancelar = new JButton("Cancelar");

    public Detalhe(ModosDetalhe modo, PesquisaView pesquisaView, ControleEmpresa controleEmpresa) {
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
        janela.add(
                new PainelBotoes(botaoAdicionar, botaoCancelar, botaoAtualizar, botaoExcluir, modo, this)
                ,c);

        // HABILITAR JANELA
        janela.setSize(width, height);
        janela.setResizable(false);
        janela.setVisible(true);
    }

    abstract protected ArrayList<JComponent> agruparTodosFormularios();

    abstract protected void excluirElemento();

    abstract protected void adicionarElemento() throws IdRepetidoException;

    abstract protected void atualizarElemento() throws IdRepetidoException;

    abstract protected void popularFormularios();

    protected void enviarFormularios() throws IdRepetidoException, NumberFormatException, NullPointerException {
        switch (modo) {
            case ADICIONAR -> adicionarElemento();
            case EDITAR -> atualizarElemento();
        }
        pesquisaView.refresh();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == botaoAtualizar || src == botaoAdicionar) {
            try {
                enviarFormularios();
            } catch (NumberFormatException e1) {
                mensagemErrodeFormatacao();
            } catch (NullPointerException e2) {
                mensagemErroFormularioVazio();
            } catch (IdRepetidoException e3) {
                mensagemErroIdrepetido(e3);
            }
        } else {
            if (src == botaoExcluir) excluirElemento();
            // tanto o botão de excluir quanto o de cancelar fecham a janela no final da operação
            janela.dispatchEvent(new WindowEvent(janela, WindowEvent.WINDOW_CLOSING));
        }
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
    protected void mensagemErroIdrepetido(IdRepetidoException e3) {
        JOptionPane.showMessageDialog(
                null, e3.getMessage(), "Erro de indentificação", JOptionPane.ERROR_MESSAGE
        );
    }
}
