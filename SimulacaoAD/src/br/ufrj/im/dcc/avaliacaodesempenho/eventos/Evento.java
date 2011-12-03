package br.ufrj.im.dcc.avaliacaodesempenho.eventos;

import br.ufrj.im.dcc.avaliacaodesempenho.estrutura.Peer;
import br.ufrj.im.dcc.avaliacaodesempenho.estrutura.Publisher;

/**
 * Classe referente aos Eventos que acontecem no sistema.
 * 
 * @version 1.0
 * */
public class Evento implements Comparable<Evento> {
	//private Integer tipoEvento;
	private String tipoEvento;
	private Double tempoOcorrenciaEvento;
	Publisher pulisher = null;
	Peer peer = null;
	
	/* Aqui nem sempre o publisher ou o peer serah instanciado. Vai depender do tipo de evento. */
	//public Evento(Publisher publisher, Peer peer, Integer tipoEvento, Double tempoOcorrenciaEvento){
	public Evento(Publisher publisher, Peer peer, String tipoEvento, Double tempoOcorrenciaEvento){
		this.pulisher = publisher;
		this.peer = peer;
		this.tipoEvento = tipoEvento;
		this.tempoOcorrenciaEvento = tempoOcorrenciaEvento;
	}
	
	
	@Override
	public int compareTo(Evento evento) {
		if(this.tempoOcorrenciaEvento > ((Evento)evento).tempoOcorrenciaEvento) {
			return 1;
		} else { 
			return -1;
		}
	}

	/* GETTER e SETTER. */
	public Double getTempoOcorrenciaEvento() {
		return tempoOcorrenciaEvento;
	}

	public Publisher getPulisher() {
		return pulisher;
	}

	public Peer getPeer() {
		return peer;
	}

	public String getTipoEvento() {
		return tipoEvento;
	}

}