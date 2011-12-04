package br.ufrj.im.dcc.avaliacaodesempenho.start;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import br.ufrj.im.dcc.avaliacaodesempenho.estrutura.Bloco;
import br.ufrj.im.dcc.avaliacaodesempenho.estrutura.Peer;
import br.ufrj.im.dcc.avaliacaodesempenho.estrutura.Publisher;
import br.ufrj.im.dcc.avaliacaodesempenho.eventos.Evento;
import br.ufrj.im.dcc.avaliacaodesempenho.eventos.TiposEvento;
import br.ufrj.im.dcc.avaliacaodesempenho.simulacao.Sistema_Cenario1;
import br.ufrj.im.dcc.avaliacaodesempenho.utils.Exponencial;
import br.ufrj.im.dcc.avaliacaodesempenho.utils.Funcoes;

public class Start {
	private static ArrayList<Peer> peers = new ArrayList<Peer>();
	private static Funcoes funcoes;
	private static Publisher publisher;

	public static void main(String[] args) {
		peers = new ArrayList<Peer>();
		funcoes = new Funcoes();
		publisher = new Publisher(1);
		
		
		/* Lista de eventos que ocorrerao no sistema.  - Otimizar. */
		ArrayList<Evento> eventos = new ArrayList<Evento>();
		
		//Cenario1
		//definir variaveis do cenario 1
		
		//define sistema aberto ou nao
		boolean sistemaAberto = true;
		
		//chegada de peer no sistema.
		Exponencial chegadaPeer; 
		
		//upload peer e seed.
		Exponencial uploadPeer;
		
		//upload publisher.
		Exponencial uploadPublisher;
		
		//double semente = tempoUploadPublisher;
		
		
		//saida peer.
		double saidaPeer = 0.0; //gama = infinito -> peers saem do sistema imediatamente apos concluirem seus downloads.
		
		//Instancia o publisher.
		//Publisher publisher = new Publisher(1);
		//double tempo = semente;
		
		
		TiposEvento tipoEvento = new TiposEvento();
		

		Sistema_Cenario1 sc1 = new Sistema_Cenario1();
		sc1.simulacao();
	}

//	/**
//	 * Método para tratar evento do tipo Upload Publisher.
//	 * @param double 
//	 * @param Exponencial
//	 * */
//	private static void trataEventoUploadPublisher(double tempo, Exponencial uploadPublisher) {
//		int qtdPeersSistema = peers.size();
//		double instanteUploadPublisher = uploadPublisher.gerar();
//		
//		if(qtdPeersSistema == 0) {
//			//seta novo tempo publisher
//			tempo = tempo + instanteUploadPublisher;    //time no qual ocorrerá o proximo upload do publisher.
//			publisher.setInstanteUploadPublisher(tempo);
//			
//			//!!!!!!! ADICIONAR NA LISTA DE EVENTOS - NAO ESQUECER
//			
//		} else {
//			//publisher faz escolha peer
//			int peerEscolhido = funcoes.geraUniforme(qtdPeersSistema);
//			
//			//publisher faz escolha bloco
//			ArrayList<Bloco> blocosNaoComuns = funcoes.buscaBlocosNaoComuns(publisher.getBlocosSistema(), peers.get(peerEscolhido).getBlocosPeer());
//			
//			if(blocosNaoComuns.size() != 0) {
//				int qtdBlocos = blocosNaoComuns.size();
//				int blocoEscolhido = funcoes.geraUniforme(qtdBlocos);
//				Bloco bloco = publisher.getBlocosSistema().get(blocoEscolhido);
//				
//				//publisher envia bloco para peer
//				peers.get(peerEscolhido).addBloco(bloco);
//				
//			}
//			
//			//seta novo tempo publisher
//			tempo = tempo + instanteUploadPublisher;    //time no qual ocorrerá o proximo upload do publisher.
//			publisher.setInstanteUploadPublisher(tempo);
//			
//			
//			//!!!!!!! ADICIONAR NA LISTA DE EVENTOS - NAO ESQUECER
//		} 
//		
//	}
//	/**
//	 * Método para tratar evento do tipo Chegada Peer.
//	 * @param double 
//	 * @param double
//	 * @param Exponencial
//	 * @param Exponencial
//	 * @param boolean
//	 * */
//	private static void trataEventoChegadaPeer(double tempoEntrada, double tempo, Exponencial chegadaPeer, Exponencial uploadPeer, boolean sistemaAberto) {
//		
//		//insere peer no sistema.
//		Peer peer = new Peer();
//		peer.setInstanteEntradaSistema(tempoEntrada); //esse tempo entrada vem da classe evento
//		peers.add(peer);
//		
//		if (sistemaAberto) {
//			//calcula tempo para a proxima chegada de peer no sistema.
//			double instanteChegadaSistema = chegadaPeer.gerar();
//			tempo = tempo + instanteChegadaSistema;
//			//!!!!!!! ADICIONAR UM NOVO EVENTO NA LISTA DE EVENTOS - EVENTO CHEGADA PEER
//		}
//		
//		//calcula tempo do upload desse peer.
//		double instanteUploadPeer = uploadPeer.gerar();
//		tempo = tempo + instanteUploadPeer;
//		peer.setInstanteUploadPeer(tempo);
//		//!!!!!!! ADICIONAR UM NOVO EVENTO NA LISTA DE EVENTOS - EVENTO UPLOAD PEER
//		
//		
//	}
//	
//	/**
//	 * Método para tratar evento do tipo Upload Peer.
//	 * @param double 
//	 * @param Exponencial
//	 * @param Peer
//	 * @param Object
//	 * */
//	private static void trataEventoUploadPeer(double tempo, Exponencial uploadPeer, Peer peer, Object saidaPeer) {
//		//peer faz escolha de peer diferente de si proprio
//		int indicePeerAtual = peers.indexOf(peer);
//		int qtdPeersSistema = peers.size();
//		int peerEscolhido = funcoes.geraUniforme(qtdPeersSistema);
//		
//		while(indicePeerAtual == peerEscolhido) {
//			peerEscolhido = funcoes.geraUniforme(qtdPeersSistema);
//		}
//		
//		//peer faz escolha bloco
//		ArrayList<Bloco> blocosNaoComuns = funcoes.buscaBlocosNaoComuns(peer.getBlocosPeer(), peers.get(peerEscolhido).getBlocosPeer());
//		
//		if(blocosNaoComuns.size() != 0) {
//			int qtdBlocos = blocosNaoComuns.size();
//			int blocoEscolhido = funcoes.geraUniforme(qtdBlocos);
//			Bloco bloco = peer.getBlocosPeer().get(blocoEscolhido);
//			
//			//peer envia bloco para peer escolhido
//			peers.get(peerEscolhido).addBloco(bloco);
//			
//		}
//		
//		//seta tempo de proximo upload do peer escolhido
//		double instanteUploadPeer = uploadPeer.gerar();
//		tempo = tempo + instanteUploadPeer;
//		peers.get(peerEscolhido).setInstanteUploadPeer(tempo);
//		
//		//!!!!!!! ADICIONAR UM NOVO EVENTO NA LISTA DE EVENTOS - EVENTO UPLOAD PEER
//		
//		// decide a saida do peer escolhido
//		int qtdBlocosPeerEscolhido = peers.get(peerEscolhido).getBlocosPeer().size();
//		if(qtdPeersSistema == qtdBlocosPeerEscolhido) {
//			
//			boolean sistemaFechado = false;
//			if(saidaPeer instanceof Double) { //se ele sair imediatamente o tempo para saida eh 0.0
//				Double saida = (Double) saidaPeer;
//				tempo = tempo + saida;
//			} else {
//				Exponencial expSaida = (Exponencial) saidaPeer;
//				double saida = expSaida.gerar();
//				tempo = tempo + saida;
//			}
//			
//			peers.get(peerEscolhido).setInstanteSaidaSistema(tempo);
//			//!!!!!!! ADICIONAR UM NOVO EVENTO NA LISTA DE EVENTOS - EVENTO SAIDA PEER
//		}
//		
//	}
//	
//	/**
//	 * Método para tratar evento do tipo Saida Peer.
//	 * @param double 
//	 * @param Exponencial
//	 * @param Peer
//	 * @param boolean
//	 * */
//	private static void trataEventoSaidaPeer(double tempo, Exponencial uploadPeer, Peer peer, boolean sistemaAberto) {
//		//peer sai do sistema
//		peers.remove(peer);
//		
//		if (!sistemaAberto) { 
//			//agenda a chegada de um novo peer.
//			//!!!!!!! ADICIONAR UM NOVO EVENTO NA LISTA DE EVENTOS - EVENTO CHEGADA PEER
//		}
//		
//	}

}
