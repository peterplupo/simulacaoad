package br.ufrj.im.dcc.avaliacaodesempenho.simulacao;

import java.util.ArrayList;

import br.ufrj.im.dcc.avaliacaodesempenho.estrutura.Bloco;
import br.ufrj.im.dcc.avaliacaodesempenho.estrutura.Peer;
import br.ufrj.im.dcc.avaliacaodesempenho.estrutura.Publisher;
import br.ufrj.im.dcc.avaliacaodesempenho.eventos.Evento;
import br.ufrj.im.dcc.avaliacaodesempenho.eventos.TiposEvento;
import br.ufrj.im.dcc.avaliacaodesempenho.utils.Exponencial;
import br.ufrj.im.dcc.avaliacaodesempenho.utils.Funcoes;


/**
 * Classe referente ao sistema como um todo.
 * 
 * @version 1.0 
 * */
public class Sistema {
	private Funcoes funcoes;
	protected ArrayList<Evento> listaEventos;
	protected Publisher publisher;
	protected ArrayList<Peer> peers;
	protected boolean sistemaAberto;
	
	private double tempoMedioDownload = 0.0;
	private double tempoMedioPermaneciaSistema = 0.0;
	
	
	public Sistema() {
		funcoes = new Funcoes();
	}

	
	
	/**
	 * Método para tratar evento do tipo Upload Publisher.
	 * @param double 
	 * @param Exponencial
	 * */
	protected void trataEventoUploadPublisher(double tempo, Exponencial uploadPublisher) {
		int qtdPeersSistema = peers.size();
		double instanteUploadPublisher = uploadPublisher.gerar();
		Evento evento = null;
		
		if(qtdPeersSistema == 0) {
			//seta novo tempo publisher
			tempo = tempo + instanteUploadPublisher;    //time no qual ocorrerá o proximo upload do publisher.
			publisher.setInstanteUploadPublisher(tempo);
			
			//adicionando evento Upload Publisher na lista de eventos.
			evento = new Evento(null, TiposEvento.UPLOAD_PUBLISHER, tempo);
			listaEventos.add(evento);
			
		} else {
			//publisher faz escolha peer
			int peerEscolhido = funcoes.geraUniforme(qtdPeersSistema);
			
			//publisher faz escolha bloco
			ArrayList<Bloco> blocosNaoComuns = funcoes.buscaBlocosNaoComuns(publisher.getBlocosSistema(), peers.get(peerEscolhido).getBlocosPeer());
			
			if(blocosNaoComuns.size() > 0) {
				int qtdBlocos = blocosNaoComuns.size();
				int blocoEscolhido = funcoes.geraUniforme(qtdBlocos);
				Bloco bloco = publisher.getBlocosSistema().get(blocoEscolhido);
				
				//publisher envia bloco para peer
				peers.get(peerEscolhido).addBloco(bloco);
				
			}
			
			//seta novo tempo publisher
			tempo = tempo + instanteUploadPublisher;    //time no qual ocorrerá o proximo upload do publisher.
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
	 * @param Exponencial
	 * @param Exponencial
	 * */
	protected void trataEventoChegadaPeer(double tempoEntrada, double tempo, Exponencial chegadaPeer, Exponencial uploadPeer) {
		Evento evento = null;
		
		//insere peer no sistema.
		Peer peer = new Peer();
		peer.setInstanteEntradaSistema(tempoEntrada); //esse tempo entrada vem da classe evento
		peers.add(peer);
		
		if (sistemaAberto) {
			//calcula tempo para a proxima chegada de peer no sistema.
			double instanteChegadaSistema = chegadaPeer.gerar();
			tempo = tempo + instanteChegadaSistema;
			
			//adicionando evento Chegada Peer na lista de eventos.
			//evento = new Evento(peer, TiposEvento.CHEGADA_PEER, tempo);
			evento = new Evento(null, TiposEvento.CHEGADA_PEER, tempo);
			listaEventos.add(evento);
			
			
		}
		
		//calcula tempo do upload desse peer.
		double instanteUploadPeer = uploadPeer.gerar();
		tempo = tempo + instanteUploadPeer;
		peer.setInstanteUploadPeer(tempo);
		
		//adicionando evento Upload Peer na lista de eventos.
		evento = new Evento(peer, TiposEvento.UPLOAD_PEER, tempo);
		listaEventos.add(evento);
		
		
	}
	
	/**
	 * Método para tratar evento do tipo Upload Peer.
	 * @param double 
	 * @param Exponencial
	 * @param Peer
	 * @param Object
	 * */
	protected void trataEventoUploadPeer(double tempo, Exponencial uploadPeer, Peer peer, Object saidaPeer) {
		Evento evento = null;
		
		//peer faz escolha de peer diferente de si proprio
		int indicePeerAtual = peers.indexOf(peer);
		int qtdPeersSistema = peers.size();
		int peerEscolhido = funcoes.geraUniforme(qtdPeersSistema);
		
		while(indicePeerAtual == peerEscolhido) {
			peerEscolhido = funcoes.geraUniforme(qtdPeersSistema);
		}
		
		//peer faz escolha bloco
		ArrayList<Bloco> blocosNaoComuns = funcoes.buscaBlocosNaoComuns(peer.getBlocosPeer(), peers.get(peerEscolhido).getBlocosPeer());
		
		if(blocosNaoComuns.size() != 0) {
			int qtdBlocos = blocosNaoComuns.size();
			int blocoEscolhido = funcoes.geraUniforme(qtdBlocos);
			Bloco bloco = peer.getBlocosPeer().get(blocoEscolhido);
			
			//peer envia bloco para peer escolhido
			peers.get(peerEscolhido).addBloco(bloco);
			
		}
		
		//seta tempo de proximo upload do peer escolhido
		double instanteUploadPeer = uploadPeer.gerar();
		tempo = tempo + instanteUploadPeer;
		peers.get(peerEscolhido).setInstanteUploadPeer(tempo);
		
		//adicionando evento Upload Peer na lista de eventos.
		evento = new Evento(peers.get(peerEscolhido), TiposEvento.UPLOAD_PEER, tempo);
		listaEventos.add(evento);
		
		
		// decide a saida do peer escolhido
		int qtdBlocosPublisher = publisher.getQtdBlocos();
		int qtdBlocosPeerEscolhido = peers.get(peerEscolhido).getBlocosPeer().size();
		if(qtdBlocosPublisher == qtdBlocosPeerEscolhido) {
			System.out.println("passou em teste se qtd blocos no sistema e no peer ta ok.");
			//boolean sistemaFechado = false;
			if(saidaPeer instanceof Double) { //se ele sair imediatamente o tempo para saida eh 0.0
				Double saida = (Double) saidaPeer;
				tempo = tempo + saida;
			} else {
				Exponencial expSaida = (Exponencial) saidaPeer;
				double saida = expSaida.gerar();
				tempo = tempo + saida;
			}
			
			peers.get(peerEscolhido).setInstanteSaidaSistema(tempo);

			//adicionando evento Saida Peer na lista de eventos.
			evento = new Evento(peers.get(peerEscolhido), TiposEvento.SAIDA_PEER, tempo);
			listaEventos.add(evento);
		}
		
	}
	
	/**
	 * Método para tratar evento do tipo Saida Peer.
	 * @param double 
	 * @param Exponencial
	 * @param Peer
	 * */
	protected void trataEventoSaidaPeer(double tempo, Exponencial chegadaPeer, Peer peer) {
		Evento evento = null;
		
		//peer sai do sistema
		peers.remove(peer);
		
		if (!sistemaAberto) { 
			//calcula tempo para proxima chegada de peer no sistema
			double instanteChegadaSistema = chegadaPeer.gerar();
			tempo = tempo + instanteChegadaSistema;
			
			//adicionando evento Chegada Peer na lista de eventos.
			evento = new Evento(null, TiposEvento.CHEGADA_PEER, tempo);
			listaEventos.add(evento);
		}
		
	}

}
