package br.ufrj.dcc.ad201102.engine;

public class SimulationParameters {
	public double lambda;
	public int blocksNumber;
	public double mi;
	public double u;
	public double gama;
	public double p;
	public int initialPopulationSize;
	public boolean blockRarity;
	public long randomSeed;
	public int batchSize;
	public int transientSize;
	
	/* GETTERS e SETTERS */
	public double getLambda() {
		return lambda;
	}
	public void setLambda(double lambda) {
		this.lambda = lambda;
	}
	public int getBlocksNumber() {
		return blocksNumber;
	}
	public void setBlocksNumber(int blocksNumber) {
		this.blocksNumber = blocksNumber;
	}
	public double getMi() {
		return mi;
	}
	public void setMi(double mi) {
		this.mi = mi;
	}
	public double getU() {
		return u;
	}
	public void setU(double u) {
		this.u = u;
	}
	public double getGama() {
		return gama;
	}
	public void setGama(double gama) {
		this.gama = gama;
	}
	public double getP() {
		return p;
	}
	public void setP(double p) {
		this.p = p;
	}
	public int getInitialPopulationSize() {
		return initialPopulationSize;
	}
	public void setInitialPopulationSize(int initialPopulationSize) {
		this.initialPopulationSize = initialPopulationSize;
	}
	public boolean isBlockRarity() {
		return blockRarity;
	}
	public void setBlockRarity(boolean blockRarity) {
		this.blockRarity = blockRarity;
	}
	public long getRandomSeed() {
		return randomSeed;
	}
	public void setRandomSeed(long randomSeed) {
		this.randomSeed = randomSeed;
	}
	public int getBatchSize() {
		return batchSize;
	}
	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}
	public int getTransientSize() {
		return transientSize;
	}
	public void setTransientSize(int transientSize) {
		this.transientSize = transientSize;
	}
	
	
}
