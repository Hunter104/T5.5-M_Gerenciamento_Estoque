package view;

import controle.ControleEmpresa;
import controle.ControleEstoqueFilial;
import controle.IdRepetidoException;
import modelo.Filial;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class JanelaEmpresa implements ActionListener {
    private static JFrame janela = new JFrame("Empresa");
    private static JButton verFil = new JButton("Ver Filiais");
    private static JButton verEst = new JButton("Ver Estoque");


    private static ControleEmpresa controleEmpresa = new ControleEmpresa("Lixo");

    public JanelaEmpresa() {
        JLabel titulo = new JLabel("Empresa");
        JLabel texto = new JLabel("<html>Um empreendimento de "
                + "vendas on-line necessita de um sistema de controle e "
                + "gerenciamento de seu estoque. Eles precisam gerenciar"
                + " os itens de estoque e as filiais responsáveis e para isso"
                + " precisam poder cadastrar, remover, alterar e ler dados sobre"
                + " as filiais e os itens. </html>");

        verFil.setBounds(40, 200, 120, 30);
        verEst.setBounds(200, 200, 120, 30);
        titulo.setBounds(140, 0, 90, 50);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        texto.setBounds(20, 70, 330, 90);

        janela.setLayout(null);

        janela.add(verFil);
        janela.add(verEst);
        janela.add(titulo);
        janela.add(texto);
        janela.setSize(400, 400);
        janela.setVisible(true);
        janela.setResizable(false);
        verFil.addActionListener(this);
        verEst.addActionListener(this);

        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {

        JanelaEmpresa empresa = new JanelaEmpresa();
        // TODO: remover essas filiais de exemplo
        try {
            controleEmpresa.adicionarFilial(
                    new Filial("abr", "brasil", 6)
            );
            controleEmpresa.adicionarFilial(
                    new Filial("sussy baki", "xina", 7)
            );
            ControleEstoqueFilial meuEstoque = new ControleEstoqueFilial(controleEmpresa, controleEmpresa.buscarFilial(7));
            meuEstoque.adicionarFarmaceutico("Ablublublé", "né", 50.99, 5, 9);
            meuEstoque.adicionarFarmaceutico("vazio", "né", 50.99, 0, 12);
            ControleEstoqueFilial meuEstoque2 = new ControleEstoqueFilial(controleEmpresa, controleEmpresa.buscarFilial(6));
            meuEstoque2.adicionarProdutoQuimico("be", "né", 50.99, 5, 56);
            meuEstoque2.adicionarFarmaceutico("asdf", "né", 50.99, 0, 0xFF);
        } catch (IdRepetidoException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == verFil) {
            new JanelaPesquisa(controleEmpresa, Modos.LISTAR_FILIAIS);
        }

        if (src == verEst) {
            new JanelaPesquisa(controleEmpresa, Modos.LISTAR_ESTOQUE_GERAL);
        }
    }
}
