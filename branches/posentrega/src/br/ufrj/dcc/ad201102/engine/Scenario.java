package br.ufrj.dcc.ad201102.engine;

import org.apache.log4j.Logger;

public class Scenario {
	private static Logger logger = Logger.getLogger(Scenario.class);
	private static final int QTD_BATCHES = 10;
	private static final int BATCH_SIZE = 25;
	private static final int TRANSIENT_SIZE = 155;

	
	public Scenario(){}
	
	public void runScenario(SimulationParameters params, int scenario){
		switch (scenario) {
			case 11: 
				params.setLambda(0.1);
				params.setBlocksNumber(1);
				params.setMi(0.1);
				params.setU(1.0);
				params.setGama(Double.POSITIVE_INFINITY);
				params.setP(0);
				params.setInitialPopulationSize(0);
				params.setBlockRarity(false);
				params.setRandomSeed(0);
				params.setBatchSize(BATCH_SIZE);
				params.setBatches(QTD_BATCHES);
				params.setTransientSize(TRANSIENT_SIZE);
				break;
			case 12: 
				params.setLambda(0.5);
				params.setBlocksNumber(1);
				params.setMi(0.1);
				params.setU(1.0);
				params.setGama(Double.POSITIVE_INFINITY);
				params.setP(0);
				params.setInitialPopulationSize(0);
				params.setBlockRarity(false);
				params.setRandomSeed(0);
				params.setBatchSize(BATCH_SIZE);
				params.setBatches(QTD_BATCHES);
				params.setTransientSize(TRANSIENT_SIZE);
				break;
			case 13: 
				params.setLambda(0.9);
				params.setBlocksNumber(1);
				params.setMi(0.1);
				params.setU(1.0);
				params.setGama(Double.POSITIVE_INFINITY);
				params.setP(0);
				params.setInitialPopulationSize(0);
				params.setBlockRarity(false);
				params.setRandomSeed(0);
				params.setBatchSize(BATCH_SIZE);
				params.setBatches(QTD_BATCHES);
				params.setTransientSize(TRANSIENT_SIZE);
				break;
			case 21:
				params.setLambda(0.1);
				params.setBlocksNumber(1);
				params.setMi(0.1);
				params.setU(1.0);
				params.setGama(0.1);
				params.setP(0);
				params.setInitialPopulationSize(0);
				params.setBlockRarity(false);
				params.setRandomSeed(0);
				params.setBatchSize(BATCH_SIZE);
				params.setBatches(QTD_BATCHES);
				params.setTransientSize(TRANSIENT_SIZE);
				break;
			case 22:
				params.setLambda(0.5);
				params.setBlocksNumber(1);
				params.setMi(0.1);
				params.setU(1);
				params.setGama(0.1);
				params.setP(0);
				params.setInitialPopulationSize(0);
				params.setBlockRarity(false);
				params.setRandomSeed(0);
				params.setBatchSize(BATCH_SIZE);
				params.setBatches(QTD_BATCHES);
				params.setTransientSize(TRANSIENT_SIZE);
				break;
			case 23:
				params.setLambda(0.9);
				params.setBlocksNumber(1);
				params.setMi(0.1);
				params.setU(1);
				params.setGama(0.1);
				params.setP(0);
				params.setInitialPopulationSize(0);
				params.setBlockRarity(false);
				params.setRandomSeed(0);
				params.setBatchSize(BATCH_SIZE);
				params.setBatches(QTD_BATCHES);
				params.setTransientSize(TRANSIENT_SIZE);
				break;
			case 30:
				params.setLambda(0.0);
				params.setBlocksNumber(1);
				params.setMi(0.1);
				params.setU(1);
				params.setGama(Double.POSITIVE_INFINITY);
				params.setP(1);
				params.setInitialPopulationSize(50);
				params.setBlockRarity(false);
				params.setRandomSeed(0);
				params.setBatchSize(BATCH_SIZE);
				params.setBatches(QTD_BATCHES);
				params.setTransientSize(TRANSIENT_SIZE);
				break;
			case 40:
				params.setLambda(0.0);
				params.setBlocksNumber(2);
				params.setMi(1.0);
				params.setU(1.0);
				params.setGama(1.0);
				params.setP(1);
				params.setInitialPopulationSize(50);
				params.setBlockRarity(false);
				params.setRandomSeed(0);
				params.setBatchSize(BATCH_SIZE);
				params.setBatches(QTD_BATCHES);
				params.setTransientSize(TRANSIENT_SIZE);
				break;	
			case 50:
				params.setLambda(0.0);
				params.setBlocksNumber(2);
				params.setMi(1);
				params.setU(1);
				params.setGama(Double.POSITIVE_INFINITY);
				params.setP(1);
				params.setInitialPopulationSize(50);
				params.setBlockRarity(false);
				params.setRandomSeed(0);
				params.setBatchSize(BATCH_SIZE);
				params.setBatches(QTD_BATCHES);
				params.setTransientSize(TRANSIENT_SIZE);
				break;
			case 60:
				params.setLambda(0.0);
				params.setBlocksNumber(2);
				params.setMi(1);
				params.setU(0.5);
				params.setGama(1);
				params.setP(1);
				params.setInitialPopulationSize(50);
				params.setBlockRarity(false);
				params.setRandomSeed(0);
				params.setBatchSize(BATCH_SIZE);
				params.setBatches(QTD_BATCHES);
				params.setTransientSize(TRANSIENT_SIZE);
				break;
			default:
				logger.info("Scenario is not valid.");
				break;
		}
	}
}