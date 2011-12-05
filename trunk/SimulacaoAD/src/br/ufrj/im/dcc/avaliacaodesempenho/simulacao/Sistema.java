package br.ufrj.im.dcc.avaliacaodesempenho.simulacao;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

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
	protected Uniforme uniforme;
	protected PriorityQueue<Evento> listaEventos;
	protected Publisher publisher;
	protected ArrayList<Peer> peers;
	protected boolean sistemaAberto;
	
	private double tempoMedioDownload = 0.0;
	private double tempoMedioPermaneciaSistema = 0.0;
	
	
	public Sistema(long seed) {
		uniforme = new Uniforme(seed);
	}

	
	
	/**
	 * M�todo para tratar evento do tipo Upload Publisher.
	 * @param double 
	 * */
	protected void trataEventoUploadPublisher(double tempoUploadPublisher, double tempoUploadPeer, double tempoSaidaPeer) {
		int qtdPeersSistema = peers.size();

		Evento evento = null;
		System.out.println("tempo publisher: " + publisher.getInstanteUploadPublisher());
		if(qtdPeersSistema == 0) {
			//seta novo tempo publisher
			publisher.setInstanteUploadPublisher(tempoUploadPublisher);
			
			//adicionando evento Upload Publisher na lista de eventos.
			evento = new Evento(null, TiposEvento.UPLOAD_PUBLISHER, tempoUploadPublisher);
			listaEventos.add(evento);
			
		} else {
			Peer peer = peers.get(uniforme.geraUniforme(qtdPeersSistema));
			
			//publisher escolhe um bloco dentre os blocos que o peer n�o tem
			List<Bloco> blocosNaoComuns = buscaBlocosNaoComuns(publisher.getBlocosSistema(), peer.getBlocosPeer());
			
			if(blocosNaoComuns.size() > 0) {
				int qtdBlocos = blocosNaoComuns.size();
				int blocoEscolhido = uniforme.geraUniforme(qtdBlocos);
				Bloco bloco = publisher.getBlocosSistema().get(blocoEscolhido);
				
				//publisher envia bloco para peer
				peer.addBloco(bloco);
				
				// decide a saida do peer escolhido
				int qtdBlocosPublisher = publisher.getQtdBlocos();
				int qtdBlocosPeerEscolhido = peer.getBlocosPeer().size();
				if((qtdBlocosPublisher == qtdBlocosPeerEscolhido)) {
					if(!peer.isSeed()) {
						System.out.println("passou em teste se qtd blocos no sistema e no peer ta ok.");
						peer.setInstanteSaidaSistema(tempoSaidaPeer);
						peer.setSeed(true);
			
						//adicionando evento Saida Peer na lista de eventos.
						evento = new Evento(peer, TiposEvento.SAIDA_PEER, tempoSaidaPeer);
						listaEventos.add(evento);
						
						//adicionar possivel upload para seed.
						if(tempoUploadPeer < tempoSaidaPeer) {
							evento = new Evento(peer, TiposEvento.UPLOAD_PEER, tempoUploadPeer);
							listaEventos.add(evento);
						}
					} else {
						//adicionar possivel upload para seed.
						if(tempoUploadPeer < peer.getInstanteSaidaSistema()) {
							evento = new Evento(peer, TiposEvento.UPLOAD_PEER, tempoUploadPeer);
							listaEventos.add(evento);
						}
					}
					
				} else {
					//seta tempo de proximo upload do peer escolhido
					peer.setInstanteUploadPeer(tempoUploadPeer);
					
					//adicionando evento Upload Peer na lista de eventos.
					evento = new Evento(peer, TiposEvento.UPLOAD_PEER, tempoUploadPeer);
					listaEventos.add(evento);
				}
				
			}
			
			//seta novo tempo publisher
			publisher.setInstanteUploadPublisher(tempoUploadPublisher);
			
			//adicionando evento Upload Publisher na lista de eventos.
			evento = new Evento(null, TiposEvento.UPLOAD_PUBLISHER, tempoUploadPublisher);
			listaEventos.add(evento);
		} 
		
	}
	/**
	 * M�todo para tratar evento do tipo Chegada Peer.
	 * @param double 
	 * @param double
	 * @param double
	 * */
	protected void trataEventoChegadaPeer(double tempoEntrada, double tempoChegadaPeer, double tempoUploadPeer, int numeroBlocos) {
		Evento evento = null;
		
		//insere peer no sistema.
		Peer peer = new Peer(numeroBlocos);
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
	 * M�todo para tratar evento do tipo Upload Peer.
	 * @param double 
	 * @param Peer
	 * @param double
	 * */
	protected void trataEventoUploadPeer(double tempoUploadPeer, Peer peer, double tempoSaidaPeer) {
		Evento evento = null;
		
		Peer peerDestino = peers.get(uniforme.geraUniforme(peers.size()));
		
		//publisher escolhe um bloco dentre os blocos que o peer n�o tem
		List<Bloco> blocosNaoComuns = buscaBlocosNaoComuns(peer.getBlocosPeer(), peerDestino.getBlocosPeer());
		
		//peer faz escolha bloco
		if(blocosNaoComuns.size() > 0) {
			int qtdBlocos = blocosNaoComuns.size();
			int blocoEscolhido = uniforme.geraUniforme(qtdBlocos);
			Bloco bloco = blocosNaoComuns.get(blocoEscolhido);
			
			//publisher envia bloco para peer
			peerDestino.addBloco(bloco);
			
		}
		
		// decide a saida do peer escolhido
		int qtdBlocosPublisher = publisher.getQtdBlocos();
		int qtdBlocosPeerEscolhido = peerDestino.getBlocosPeer().size();
		if((qtdBlocosPublisher == qtdBlocosPeerEscolhido)) {
			if(!peerDestino.isSeed()) {
				System.out.println("passou em teste se qtd blocos no sistema e no peer ta ok.");
				peerDestino.setInstanteSaidaSistema(tempoSaidaPeer);
				peerDestino.setSeed(true);
	
				//adicionando evento Saida Peer na lista de eventos.
				evento = new Evento(peerDestino, TiposEvento.SAIDA_PEER, tempoSaidaPeer);
				listaEventos.add(evento);
			}
			//adicionar possivel upload para seed.
			if(tempoUploadPeer < tempoSaidaPeer) {
				evento = new Evento(peerDestino, TiposEvento.UPLOAD_PEER, tempoUploadPeer);
				listaEventos.add(evento);
			}
			
		} else {
			//seta tempo de proximo upload do peer escolhido
			peerDestino.setInstanteUploadPeer(tempoUploadPeer);
			
			//adicionando evento Upload Peer na lista de eventos.
			evento = new Evento(peerDestino, TiposEvento.UPLOAD_PEER, tempoUploadPeer);
			listaEventos.add(evento);
		}
		
	}
	
	/**
	 * M�todo para tratar evento do tipo Saida Peer.
	 * @param double 
	 * @param Peer
	 * */
	protected void trataEventoSaidaPeer(double tempoChegadaPeer, Peer peer, boolean recomenda) {
		Evento evento = null;
		
		//peer sai do sistema
		peers.remove(peer);
		if (recomenda) { 
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
	private List<Bloco> buscaBlocosNaoComuns(List<Bloco> peerOrigem, List<Bloco> peerDestino) {
		ArrayList<Bloco> blocosNaoComuns = new ArrayList<Bloco>(peerOrigem);
		blocosNaoComuns.removeAll(peerDestino);
		return blocosNaoComuns;
	}

}
