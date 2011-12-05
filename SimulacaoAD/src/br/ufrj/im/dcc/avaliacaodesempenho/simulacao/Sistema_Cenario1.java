package br.ufrj.im.dcc.avaliacaodesempenho.simulacao;

import java.util.ArrayList;
import java.util.PriorityQueue;

import br.ufrj.im.dcc.avaliacaodesempenho.distribuicoes.Exponencial;
import br.ufrj.im.dcc.avaliacaodesempenho.estrutura.Peer;
import br.ufrj.im.dcc.avaliacaodesempenho.estrutura.Publisher;
import br.ufrj.im.dcc.avaliacaodesempenho.eventos.Evento;
import br.ufrj.im.dcc.avaliacaodesempenho.eventos.TiposEvento;

public class Sistema_Cenario1 extends Sistema{
	private final int TAM_SIMULACAO = 5000;
	
	private double[] lambdas = {0.1, 0.5, 0.9};
	private double lambda;      //taxa de chegada de peer no sistema
	private double mi;          //taxa de upload de peers e seeds
	private double gama;        //taxa de saida dos peers
	private double u;           //taxa upload publisher
	private int p;
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
		this.p = 0;
		this.numeroBlocos = 1;
		this.tempo = 0.0;
		
		this.uploadPublisher = new Exponencial(this.u, seed);
		this.chegadaPeer = new Exponencial(this.lambda, seed);
		this.uploadPeer = new Exponencial(this.mi, seed);
		this.saidaPeer = this.gama;    //pois peer sai imediatamente apos virar seed
		
		peers = new ArrayList<Peer>();
		publisher = new Publisher(this.numeroBlocos);
		listaEventos = new PriorityQueue<Evento>();
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
			Evento eventoLista = listaEventos.poll();
			switch (eventoLista.getTipoEvento()) {
			case UPLOAD_PUBLISHER:
				double tempoUploadPublisher = tempo + uploadPublisher.gerar();
				double tempoUploadPeer = tempo + uploadPeer.gerar();
				double tempoSaidaPeer = tempo + saidaPeer;
				tempo = Math.max(Math.max(tempoUploadPublisher, tempoUploadPeer), tempoSaidaPeer);
				trataEventoUploadPublisher(tempoUploadPublisher, tempoUploadPeer, tempoSaidaPeer);
				System.out.println("tipoEvento: " + TiposEvento.UPLOAD_PUBLISHER + " - tempo: " + tempo);
				break;
			case CHEGADA_PEER:
				double tempoEntrada = eventoLista.getTempoOcorrenciaEvento();
				double tempoChegada = tempo + chegadaPeer.gerar();
				tempo = tempoChegada;
				double tempoUploadChegada = tempo + uploadPeer.gerar();
				tempo = tempoUploadChegada;
				trataEventoChegadaPeer(tempoEntrada, tempoChegada, tempoUploadChegada, numeroBlocos);  
				System.out.println("tipoEvento: " + TiposEvento.CHEGADA_PEER + " - tempo: " + tempo);
				break;
			case UPLOAD_PEER:
				Peer peer = eventoLista.getPeer();
				double tempoUpload = tempo + uploadPeer.gerar();
				//tempo = tempoUpload;
				double tempoSaida = tempo + saidaPeer;
				//tempo = tempoSaida;
				
				tempo = Math.max(tempoUpload, tempoSaida);
				
				trataEventoUploadPeer(tempoUpload, peer, tempoSaida);
				System.out.println("tipoEvento: " + TiposEvento.UPLOAD_PEER + " - tempo: " + tempo);
				break;
			case SAIDA_PEER:
				Peer peerSaida = eventoLista.getPeer();
				tempo = tempo + chegadaPeer.gerar();
				boolean recomenda = false;
				if(uniforme.geraProbabilidade() < p) {
					recomenda = true;
				}
				trataEventoSaidaPeer(tempo, peerSaida, recomenda);
				System.out.println("tipoEvento: " + TiposEvento.SAIDA_PEER + " - tempo: " + tempo);
				break;
			}
			
		}
		System.out.println("peers.size(): " + peers.size());
		
	}
}
