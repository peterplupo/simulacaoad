package br.ufrj.im.dcc.avaliacaodesempenho.utils;

import java.util.ArrayList;
import java.util.Random;

import br.ufrj.im.dcc.avaliacaodesempenho.estrutura.Bloco;

/**
 * Classe onde serão definidas as funções gerais utilizadas no sistema.
 * 
 * @version 1.0 
 * */
public class Funcoes {
	
	
	/* 
	 * Funcao que devolve o conjunto de blocos pertencentes ao peer de origem 
	 * e nao pertencentes ao peer destino.
	 * */
	public ArrayList<Bloco> buscaBlocosNaoComuns(ArrayList<Bloco> peerOrigem, ArrayList<Bloco> peerDestino) {
		ArrayList<Bloco> blocosNaoComuns = new ArrayList<Bloco>(peerOrigem);
		blocosNaoComuns.removeAll(peerDestino);
		return blocosNaoComuns;
	}
	
}
