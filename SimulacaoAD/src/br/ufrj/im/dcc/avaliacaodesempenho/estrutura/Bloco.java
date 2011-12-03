package br.ufrj.im.dcc.avaliacaodesempenho.estrutura;

/**
 * Classe que representa um bloco de um arquivo.
 *  
 * @version 1.0
 * */
public class Bloco {
	
	/* Define a forma de dintincao entre os blocos*/
	private String chaveBloco;

	public Bloco(int numeroBloco) {
		this.chaveBloco = "BLOCO_" + numeroBloco;
	}

	
	
	/* HASHCODE e EQUALS. */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((chaveBloco == null) ? 0 : chaveBloco.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bloco other = (Bloco) obj;
		if (chaveBloco == null) {
			if (other.chaveBloco != null)
				return false;
		} else if (!chaveBloco.equals(other.chaveBloco))
			return false;
		return true;
	}

	
	/* GETTER e SETTER. */
	public String getChaveBloco() {
		return chaveBloco;
	}


	
	
}
