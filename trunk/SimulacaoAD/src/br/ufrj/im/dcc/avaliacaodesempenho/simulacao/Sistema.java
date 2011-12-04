package br.ufrj.im.dcc.avaliacaodesempenho.simulacao;

import java.util.ArrayList;

import br.ufrj.im.dcc.avaliacaodesempenho.distribuicoes.Uniforme;
import br.ufrj.im.dcc.avaliacaodesempenho.estrutura.Bloco;
import br.ufrj.im.dcc.avaliacaodesempenho.estrutura.Peer;
import br.ufrj.im.dcc.avaliacaodesempenho.estrutura.Publisher;
import br.ufrj.im.dcc.avaliacaodesempenho.eventos.Evento;
import br.ufrj.im.dcc.avaliacaodesempenho.eventos.TiposEvento;


/**
 * Classe referente ao sistema como um todo.
 * 
 * @version 1.0 
 * */
public class Sistema {
	private Uniforme uniforme;
	protected ArrayList<Evento> listaEventos;
	protected Publisher publisher;
	protected ArrayList<Peer> peers;
	protected boolean sistemaAberto;
	
	private double tempoMedioDownload = 0.0;
	private double tempoMedioPermaneciaSistema = 0.0;
	
	
	public Sistema(long seed) {
		uniforme = new Uniforme(seed);
	}

	
	
	/**
	 * Método para tratar evento do tipo Upload Publisher.
	 * @param double 
	 * */
	protected void trataEventoUploadPublisher(double tempo) {
		int qtdPeersSistema = peers.size();

		Evento evento = null;
		System.out.println("tempo publisher: " + publisher.getInstanteUploadPublisher());
		if(qtdPeersSistema == 0) {
			//seta novo tempo publisher
			publisher.setInstanteUploadPublisher(tempo);
			
			//adicionando evento Upload Publisher na lista de eventos.
			evento = new Evento(null, TiposEvento.UPLOAD_PUBLISHER, tempo);
			listaEventos.add(evento);
			
		} else {
			//publisher faz escolha peer
			int peerEscolhido = uniforme.geraUniforme(qtdPeersSistema);
			
			//publisher faz escolha bloco
			ArrayList<Bloco> blocosNaoComuns = buscaBlocosNaoComuns(publisher.getBlocosSistema(), peers.get(peerEscolhido).getBlocosPeer());
			
			if(blocosNaoComuns.size() > 0) {
				int qtdBlocos = blocosNaoComuns.size();
				int blocoEscolhido = uniforme.geraUniforme(qtdBlocos);
				Bloco bloco = publisher.getBlocosSistema().get(blocoEscolhido);
				
				//publisher envia bloco para peer
				peers.get(peerEscolhido).addBloco(bloco);
				
			}
			
			//seta novo tempo publisher
			publisher.setInstanteUploadPublisher(tempo);
			
			//adicionando evento Upload Publisher na lista de eventos.
			evento = new Evento(null, TiposEvento.UPLOAD_PUBLISHER, tempo);
			listaEventos.add(evento);
		} 
		
	}
	/**
	 * Método para tratar evento do tipo Chegada Peer.
	 * @param double 
	 * @param double
	 * @param double
	 * */
	protected void trataEventoChegadaPeer(double tempoEntrada, double tempoChegadaPeer, double tempoUploadPeer) {
		Evento evento = null;
		
		//insere peer no sistema.
		Peer peer = new Peer();
		peer.setInstanteEntradaSistema(tempoEntrada); //esse tempo entrada vem da classe evento
		peers.add(peer);
		
		if (sistemaAberto) {
			//adicionando evento Chegada Peer na lista de eventos.
			evento = new Evento(null, TiposEvento.CHEGADA_PEER, tempoChegadaPeer);
			listaEventos.add(evento);
			
			
		}
		
		//seta tempo do upload desse peer.
		peer.setInstanteUploadPeer(tempoUploadPeer);
		
		//adicionando evento Upload Peer na lista de eventos.
		evento = new Evento(peer, TiposEvento.UPLOAD_PEER, tempoUploadPeer);
		listaEventos.add(evento);
		
		
	}
	
	/**
	 * Método para tratar evento do tipo Upload Peer.
	 * @param double 
	 * @param Peer
	 * @param double
	 * */
	protected void trataEventoUploadPeer(double tempoUploadPeer, Peer peer, double tempoSaidaPeer) {
		Evento evento = null;
		
		//peer faz escolha de peer diferente de si proprio
		int indicePeerAtual = peers.indexOf(peer);
		int qtdPeersSistema = peers.size();
		int peerEscolhido = uniforme.geraUniforme(qtdPeersSistema);
		
		while(indicePeerAtual == peerEscolhido) {
			peerEscolhido = uniforme.geraUniforme(qtdPeersSistema);
		}
		
		//peer faz escolha bloco
		ArrayList<Bloco> blocosNaoComuns = buscaBlocosNaoComuns(peer.getBlocosPeer(), peers.get(peerEscolhido).getBlocosPeer());
		
		if(blocosNaoComuns.size() != 0) {
			int qtdBlocos = blocosNaoComuns.size();
			int blocoEscolhido = uniforme.geraUniforme(qtdBlocos);
			Bloco bloco = peer.getBlocosPeer().get(blocoEscolhido);
			
			//peer envia bloco para peer escolhido
			peers.get(peerEscolhido).addBloco(bloco);
			
		}
		
		//seta tempo de proximo upload do peer escolhido
		peers.get(peerEscolhido).setInstanteUploadPeer(tempoUploadPeer);
		
		//adicionando evento Upload Peer na lista de eventos.
		evento = new Evento(peers.get(peerEscolhido), TiposEvento.UPLOAD_PEER, tempoUploadPeer);
		listaEventos.add(evento);
		
		
		// decide a saida do peer escolhido
		int qtdBlocosPublisher = publisher.getQtdBlocos();
		int qtdBlocosPeerEscolhido = peers.get(peerEscolhido).getBlocosPeer().size();
		if(qtdBlocosPublisher == qtdBlocosPeerEscolhido) {
			System.out.println("passou em teste se qtd blocos no sistema e no peer ta ok.");
			peers.get(peerEscolhido).setInstanteSaidaSistema(tempoSaidaPeer);

			//adicionando evento Saida Peer na lista de eventos.
			evento = new Evento(peers.get(peerEscolhido), TiposEvento.SAIDA_PEER, tempoSaidaPeer);
			listaEventos.add(evento);
		}
		
	}
	
	/**
	 * Método para tratar evento do tipo Saida Peer.
	 * @param double 
	 * @param Peer
	 * */
	protected void trataEventoSaidaPeer(double tempoChegadaPeer, Peer peer) {
		Evento evento = null;
		
		//peer sai do sistema
		peers.remove(peer);
		
		if (!sistemaAberto) { 
			System.out.println("passou aqui - tratamento saida de peer");
			
			//adicionando evento Chegada Peer na lista de eventos.
			evento = new Evento(null, TiposEvento.CHEGADA_PEER, tempoChegadaPeer);
			listaEventos.add(evento);
		}
		
	}
	
	/* 
	 * Funcao que devolve o conjunto de blocos pertencentes ao peer de origem 
	 * e nao pertencentes ao peer destino.
	 * */
	private ArrayList<Bloco> buscaBlocosNaoComuns(ArrayList<Bloco> peerOrigem, ArrayList<Bloco> peerDestino) {
		ArrayList<Bloco> blocosNaoComuns = new ArrayList<Bloco>(peerOrigem);
		blocosNaoComuns.removeAll(peerDestino);
		return blocosNaoComuns;
	}

}
