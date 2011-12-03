package br.ufrj.im.dcc.avaliacaodesempenho.simulacao;

import java.util.ArrayList;

import br.ufrj.im.dcc.avaliacaodesempenho.estrutura.Peer;
import br.ufrj.im.dcc.avaliacaodesempenho.estrutura.Publisher;
import br.ufrj.im.dcc.avaliacaodesempenho.eventos.Evento;

public class Sistema_Cenario1 extends Sistema{
	private double lambda;      //taxa de entrada de peer no sistema
	private double mi;          //taxa de upload de peers e seeds.
	private double gama;        //taxa de saida dos peers
	private int numeroBlocos;
	

	public Sistema_Cenario1 () {
		super();
		this.lambda = 0.1;
		this.mi = 0.0;           // tanto faz já que sai do sistema imediatamente apos virar seed
		this.gama = 0.0;         // quer dizer que o peer sai imediatamente apos virar seed.
		this.numeroBlocos = 1;
			
		peers = new ArrayList<Peer>();
		publisher = new Publisher(this.numeroBlocos);
		listaEventos = new ArrayList<Evento>();
		sistemaAberto = true;
		
	}
}
