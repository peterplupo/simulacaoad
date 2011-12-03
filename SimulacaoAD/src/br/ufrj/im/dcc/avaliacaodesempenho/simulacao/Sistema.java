package br.ufrj.im.dcc.avaliacaodesempenho.simulacao;

import java.util.ArrayList;

import br.ufrj.im.dcc.avaliacaodesempenho.estrutura.Peer;
import br.ufrj.im.dcc.avaliacaodesempenho.estrutura.Publisher;


/**
 * Classe referente ao sistema como um todo.
 * 
 * @version 1.0 
 * */
public class Sistema {
	private Publisher publisher;
	private ArrayList<Peer> peers;
	private double tempoMedioDownload = 0.0;
	private double tempoMedioPermaneciaSistema = 0.0;
	
	
	public Sistema() {
		
	}

}
