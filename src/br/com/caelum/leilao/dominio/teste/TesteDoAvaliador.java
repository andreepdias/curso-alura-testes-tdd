package br.com.caelum.leilao.dominio.teste;

import br.com.caelum.leilao.builder.CriadorDeLeilao;
import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Usuario;
import br.com.caelum.leilao.dominio.leilao.servico.Avaliador;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

public class TesteDoAvaliador {

    private Avaliador leiloeiro;
    private Usuario joao;
    private Usuario jose;
    private Usuario maria;

    @Before
    public void criaAvaliador(){
        this.leiloeiro = new Avaliador();
    }

    @Before
    public void ciraUsuario(){
        this.joao = new Usuario("João");
        this.jose = new Usuario("José");
        this.maria = new Usuario("Maria");
    }

    @Test(expected = RuntimeException.class)
    public void naoDeveAvaliarLeiloesSemNenhumLanceDado(){
        Leilao leilao = new CriadorDeLeilao().para("Play 4").constroi();
        leiloeiro.avalia(leilao);
    }

    @Test
    public void deveEntenderLancesEmOrdemCrescente() {
        // cenário
        Leilao leilao = new Leilao("Play 5");

        leilao.propoe(new Lance(joao, 250.0));
        leilao.propoe(new Lance(jose, 300.0));
        leilao.propoe(new Lance(maria, 400.0));

        // ação
        leiloeiro.avalia(leilao);

        // validação
        assertEquals(400, leiloeiro.getMaiorLance(), 0.00001);
        assertThat(leiloeiro.getMaiorLance(), equalTo(400.));
        assertThat(leiloeiro.getMenorLance(), equalTo(250.));
    }

    @Test
    public void deveEntenderLeilaoComApenasUmLance() {
        // cenário
        Usuario joao = new Usuario("João");

        Leilao leilao = new Leilao("Play 4");

        leilao.propoe(new Lance(joao, 1000.0));

        // ação

        leiloeiro.avalia(leilao);

        // validação
        assertEquals(1000.0, leiloeiro.getMaiorLance(), 0.00001);
        assertEquals(1000.0, leiloeiro.getMenorLance(), 0.00001);
    }

    @Test
    public void deveEncontrarOsTresMaioresLances(){
        Leilao leilao = new CriadorDeLeilao().para("Playstation 5")
                .lance(joao, 100.0)
                .lance(maria, 200.)
                .lance(joao, 300.)
                .lance(maria, 400)
                .constroi();

            leiloeiro.avalia(leilao);

        List<Lance> maiores = leiloeiro.getTresMaiores();
        assertEquals(3, maiores.size());

        assertThat(maiores, hasItems(
                new Lance(maria , 400),
                new Lance(joao, 300),
                new Lance(maria, 200)
        ));
    }

    @Test
    public void deveEntenderLeilaoComLancesEmOrdemRandomica() {
        Leilao leilao = new Leilao("Playstation 5 Novo");

        leilao.propoe(new Lance(joao,200.0));
        leilao.propoe(new Lance(maria,450.0));
        leilao.propoe(new Lance(joao,120.0));
        leilao.propoe(new Lance(maria,700.0));
        leilao.propoe(new Lance(joao,630.0));
        leilao.propoe(new Lance(maria,230.0));


        leiloeiro.avalia(leilao);

        assertEquals(700.0, leiloeiro.getMaiorLance(), 0.0001);
        assertEquals(120.0, leiloeiro.getMenorLance(), 0.0001);
    }
}
