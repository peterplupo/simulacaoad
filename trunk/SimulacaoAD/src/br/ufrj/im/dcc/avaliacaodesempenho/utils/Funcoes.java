package br.ufrj.im.dcc.avaliacaodesempenho.utils;

import java.util.ArrayList;
import java.util.Random;

import br.ufrj.im.dcc.avaliacaodesempenho.estrutura.Bloco;

/**
 * Classe onde ser�o definidas as fun��es gerais utilizadas no sistema.
 * 
 * @version 1.0 
 * */
public class Funcoes {
	
	/* Funcao que gera um numero aleatorio uniformemente distribuido entre 0 e (i-1). */
	public int geraUniforme(int i) {
		Random aleatorio = new Random();
		
		return aleatorio.nextInt(i);
	}
	
	/* 
	 * Funcao que devolve o conjunto de blocos pertencentes ao peer de origem 
	 * e nao pertencentes ao peer destino.
	 * */
	public ArrayList<Bloco> buscaBlocosNaoComuns(ArrayList<Bloco> peerOrigem, ArrayList<Bloco> peerDestino) {
		ArrayList<Bloco> blocosNaoComuns = new ArrayList<Bloco>();
		
		for(Bloco bloco:peerOrigem){
			if(!peerDestino.contains(bloco)) {
				blocosNaoComuns.add(bloco);
			}
			
		}
		
		return blocosNaoComuns;
	}
	
}
