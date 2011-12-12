package br.ufrj.dcc.ad201102.model;


public class Publisher extends Peer {
	
	public Publisher(int fileSize) {
		super();
		for (int i = 0; i < getSignature().length; i++) {
			addBlock(i);
		}
	}

}
