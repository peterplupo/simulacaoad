package br.ufrj.im.dcc.avaliacaodesempenho.simulacao;

import java.util.ArrayList;

import br.ufrj.im.dcc.avaliacaodesempenho.estrutura.Peer;
import br.ufrj.im.dcc.avaliacaodesempenho.estrutura.Publisher;
import br.ufrj.im.dcc.avaliacaodesempenho.eventos.Evento;
import br.ufrj.im.dcc.avaliacaodesempenho.eventos.TiposEvento;
import br.ufrj.im.dcc.avaliacaodesempenho.utils.Exponencial;

public class Sistema_Cenario1 extends Sistema{
	private final int TAM_SIMULACAO = 5;
	
	private double[] lambdas = {0.1, 0.5, 0.9};
	private double lambda;      //taxa de chegada de peer no sistema
	private double mi;          //taxa de upload de peers e seeds
	private double gama;        //taxa de saida dos peers
	private double u;           //taxa upload publisher
	private int numeroBlocos;
	
	private Exponencial uploadPublisher;
	private Exponencial chegadaPeer;
	private Exponencial uploadPeer;
	private Double saidaPeer;
	private Double tempo;
	

	public Sistema_Cenario1 (long seed) {
		super(seed);
		this.lambda = 0.1;
		this.mi = 0.1;           // tanto faz já que sai do sistema imediatamente apos virar seed
		this.gama = 0.0;         // quer dizer que o peer sai imediatamente apos virar seed
		this.u = 1.0;
		this.numeroBlocos = 1;
		this.tempo = 0.0;
		
		this.uploadPublisher = new Exponencial(this.u, seed);
		this.chegadaPeer = new Exponencial(this.lambda, seed);
		this.uploadPeer = new Exponencial(this.mi, seed);
		this.saidaPeer = this.gama;    //pois peer sai imediatamente apos virar seed
		
		peers = new ArrayList<Peer>();
		publisher = new Publisher(this.numeroBlocos);
		listaEventos = new ArrayList<Evento>();
		sistemaAberto = true;
		
		
	}
	/**
	 * Método que iniciliza a simulacao deste cenário.
	 * */
	public void simulacao(){
		Evento evento = null;
			
		//Primeiro evento será Upload Publisher
		tempo = tempo + uploadPublisher.gerar();
		System.out.println("tempo1: " + tempo);
		publisher.setInstanteUploadPublisher(tempo);
		evento = new Evento(null, TiposEvento.UPLOAD_PUBLISHER, tempo);
		listaEventos.add(evento);
		
		//Segundo evento será Chegada Peer
		tempo = tempo + chegadaPeer.gerar();
		System.out.println("tempo2: " + tempo);
		evento = new Evento(null, TiposEvento.CHEGADA_PEER, tempo);
		listaEventos.add(evento);
		
		for(int i = 0; i < TAM_SIMULACAO; i++) {
			TiposEvento tipoEvento = listaEventos.get(i).getTipoEvento();
			if(tipoEvento == TiposEvento.UPLOAD_PUBLISHER) {
				tempo = tempo + uploadPublisher.gerar();
				trataEventoUploadPublisher(tempo);
				System.out.println("tipoEvento: " + tipoEvento + " - tempo: " + tempo);
			}
			if(tipoEvento == TiposEvento.CHEGADA_PEER) {
				double tempoEntrada = listaEventos.get(i).getTempoOcorrenciaEvento();
				double tempoChegada = tempo + chegadaPeer.gerar();
				tempo = tempoChegada;
				double tempoUpload = tempo + uploadPeer.gerar();
				tempo = tempoUpload;
				trataEventoChegadaPeer(tempoEntrada, tempoChegada, tempoUpload);  
				System.out.println("tipoEvento: " + tipoEvento + " - tempo: " + tempo);
			}
			if(tipoEvento == TiposEvento.UPLOAD_PEER) {
				Peer peer = listaEventos.get(i).getPeer();
				double tempoUpload = tempo + uploadPeer.gerar();
				tempo = tempoUpload;
				double tempoSaida = tempo + saidaPeer;
				tempo = tempoSaida;
				trataEventoUploadPeer(tempoUpload, peer, tempoSaida);
				System.out.println("tipoEvento: " + tipoEvento + " - tempo: " + tempo);
			}
			if(tipoEvento == TiposEvento.SAIDA_PEER) {
				Peer peer = listaEventos.get(i).getPeer();
				tempo = tempo + chegadaPeer.gerar();
				trataEventoSaidaPeer(tempo, peer);
				System.out.println("tipoEvento: " + tipoEvento + " - tempo: " + tempo);
			}
			
		}
		System.out.println("peers.size(): " + peers.size());
		
		
		
	}
}
