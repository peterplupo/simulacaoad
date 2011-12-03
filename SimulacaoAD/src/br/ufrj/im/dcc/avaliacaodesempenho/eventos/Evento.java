package br.ufrj.im.dcc.avaliacaodesempenho.eventos;

import br.ufrj.im.dcc.avaliacaodesempenho.estrutura.Peer;

/**
 * Classe referente aos Eventos que acontecem no sistema.
 * 
 * @version 1.0
 * */
public class Evento implements Comparable<Evento> {
	//private Integer tipoEvento;
	private String tipoEvento;
	private Double tempoOcorrenciaEvento;
	Peer peer = null;
	
	/* Aqui nem sempre o peer serah instanciado. Vai depender do tipo de evento. */
	public Evento(Peer peer, String tipoEvento, Double tempoOcorrenciaEvento){
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

	public Peer getPeer() {
		return peer;
	}

	public String getTipoEvento() {
		return tipoEvento;
	}

}