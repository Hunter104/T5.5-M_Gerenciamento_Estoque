package modelo;

public class Farmaceutico extends Item {
    //variaveis da classe
    private String tarja;
    private boolean receita;
    private boolean retencaoDeReceita;
    private String composicao;
    private boolean generico;

    public Farmaceutico(String nome, String categoria, double valor, int quantidade, int id,
                        String tarja, String composicao, boolean receita, boolean retencaoDeReceita,
                        boolean generico) {
        super(nome, categoria, valor, quantidade, id);
        this.tarja = tarja;
        this.receita = receita;
        this.retencaoDeReceita = retencaoDeReceita;
        this.composicao = composicao;
        this.generico = generico;
        ajustarRestricao();
    }

    public Farmaceutico(String nome, String categoria, double valor, int quantidade, int id) {
        super(nome, categoria, valor, quantidade, id);
        // Assumir que o item é perigoso
        this.tarja = "preta";
        this.composicao = "";
        this.receita = true;
        this.retencaoDeReceita = true;
        this.generico = true;
        this.restrito = true;
    }

    public String listarCaracteristicasBasicas() {

        return super.listarCaracteristicasBasicas() + String.format("""
                        ---Farmacêutico---
                        Tarja: %s
                        Necessita de receita: %b
                        Retenção de receita: %b
                        Composição: %s
                        genérico: %b
                        restrito: %b
                        """, tarja, receita, retencaoDeReceita,
                composicao, generico, restrito);
    }

    //Outros metodos
    //METODO RESTRINGIR
    public void restringir() throws NivelRestricaoInadequadoException {
        if (tarja.equals("preta") && retencaoDeReceita) {
            restrito = true;
        } else {
            throw new NivelRestricaoInadequadoException(
                    "Erro ao restringir: o nível risco desse farmacêutico não é baixo o suficiente"
            );
        }
    }

    //METODO LIBERAR
    public void liberar() throws NivelRestricaoInadequadoException {
        if (tarja.equals("preta") && retencaoDeReceita) {
            restrito = false;
        } else {
            throw new NivelRestricaoInadequadoException(
                    "Erro ao liberar: o nível risco desse farmacêutico não é alto o suficiente"
            );
        }
    }

    @Override
    protected void ajustarRestricao() {
        restrito = tarja.equals("preta") && retencaoDeReceita;
    }

    public boolean isRestrito() {
        return restrito;
    }

    //gets & sets
    public String getTarja() {
        return tarja;
    }

    public void setTarja(String tarja) {
        this.tarja = tarja;
    }

    public boolean isReceita() {
        return receita;
    }

    public void setReceita(boolean receita) {
        this.receita = receita;
    }

    public boolean isRetencaoDeReceita() {
        return retencaoDeReceita;
    }

    public void setRetencaoDeReceita(boolean retencaoDeReceita) {
        this.retencaoDeReceita = retencaoDeReceita;
    }

    public String getComposicao() {
        return composicao;
    }

    public void setComposicao(String composicao) {
        this.composicao = composicao;
    }

    public boolean isGenerico() {
        return generico;
    }

    public void setGenerico(boolean generico) {
        this.generico = generico;
    }


}
