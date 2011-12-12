package br.ufrj.dcc.ad201102.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.PriorityQueue;

import org.apache.log4j.Logger;

import br.ufrj.dcc.ad201102.data.BatchData;
import br.ufrj.dcc.ad201102.data.Measurement;
import br.ufrj.dcc.ad201102.events.ArrivalEvent;
import br.ufrj.dcc.ad201102.events.Event;
import br.ufrj.dcc.ad201102.events.ExitEvent;
import br.ufrj.dcc.ad201102.events.PeerUploadEvent;
import br.ufrj.dcc.ad201102.events.PublisherUploadEvent;
import br.ufrj.dcc.ad201102.model.Peer;
import br.ufrj.dcc.ad201102.model.Publisher;
import br.ufrj.dcc.ad201102.report.ReportGenerator;



//otimizações:
//1 só enviar blocos úteis
//2 download ao invés de upload (melhora?)
//3 enviar e receber simultaneamente (pode? não é só a política de escolha e de envio que pode mudar?)

public class TorrentSimulator {
	
	private static Logger logger = Logger.getLogger(TorrentSimulator.class);
	
	double lambda;
	int blocksNumber;
	double mi;
	double u;
	double gama;
	double p;
	double initialPopulationSize;
	boolean blockRarity;
	long randomSeed;
	int batchSize;
	int batches;
	private int transientSize = 100;
	
	public static void main(String[] args) {
		SimulationParameters params = new SimulationParameters();
		Scenario scenario = new Scenario();
		scenario.runScenario(params, 60);
		
//		params.lambda = 1;
//		params.blocksNumber = 10;
//		params.mi = 1;
//		params.u = 0.5;
//		params.gama = 1;
//		params.p = 0;
//		params.initialPopulationSize = 0;
//		params.blockRarity = false;
//		params.randomSeed = 0;
//		params.batchSize = 25;
//		params.batches = 10;
//		params.transientSize = 150;

		
		
		TorrentSimulator simulator = new TorrentSimulator(params);
		simulator.simulate();
		ReportGenerator.getPopulationPMF(Measurement.getBatchData(false));
		ReportGenerator.getPopulationCDF(Measurement.getBatchData(false));
	}
	
	public TorrentSimulator(double lambda, int blocksNumber, double mi,
			double u, double gama, double p, double initialPopulationSize,
			boolean blockRarity, long randomSeed, int batchSize, int batches, int transientSize) {
		super();
		this.lambda = lambda;
		this.blocksNumber = blocksNumber;
		this.mi = mi;
		this.u = u;
		this.gama = gama;
		this.p = p;
		this.initialPopulationSize = initialPopulationSize;
		this.blockRarity = blockRarity;
		this.randomSeed = randomSeed;
		this.batchSize = batchSize;
		this.batches = batches;
		this.transientSize = transientSize;
		Measurement.reset();
	}



	public TorrentSimulator(SimulationParameters params) {
		this(params.lambda,
		params.blocksNumber,
		params.mi,
		params.u,
		params.gama,
		params.p,
		params.initialPopulationSize,
		params.blockRarity,
		params.randomSeed,
		params.batchSize,
		params.batches,
		params.transientSize);
	}

	public void simulate() {
		Peer.BLOCK_RARITY = blockRarity;
		Peer.BLOCKS_NUMBER = blocksNumber;
		ExitEvent.P = p;
		Peer.CHOICES = new Uniform(randomSeed);
		Peer.BLOCKS_FREQUENCY = new int[Peer.BLOCKS_NUMBER];
		ArrivalEvent.PEERS_ARRIVAL = new Exponential(lambda, randomSeed);
		PublisherUploadEvent.PUBLISHER_UPLOAD_CLOCK = new Exponential(u, randomSeed);
		PeerUploadEvent.PEER_UPLOAD_CLOCK = new Exponential(mi, randomSeed);
		ExitEvent.EXIT_CLOCK = new Exponential(gama, randomSeed);
		ExitEvent.EXIT_PROBABILITY = new Uniform(randomSeed);
		
		PriorityQueue<Event> events = new PriorityQueue<Event>();
		Publisher publisher = new Publisher(Peer.BLOCKS_NUMBER);
		Collection<Peer> peers = new ArrayList<Peer>();
		
		Measurement.setTransientBatch(transientSize != 0);
		double currentTime = 0;
		BatchData batchData = null;
		
		if (Measurement.hasTransientBatch()) {
			batchData = Measurement.getTransientBatchData();
			init(events, publisher, peers, currentTime, batchData);
			logger.info(-1 + " transient started at "+ currentTime +".");
			for (int transientCounter = 0; transientCounter < transientSize; transientCounter++) {
				Event currentEvent = events.poll();
				currentTime = currentEvent.getTime();
				if (transientCounter == 0) {
					batchData.setStartTime(currentTime);
				}
				events.addAll(currentEvent.nextEvents(batchData));
			}
			batchData.setEndTime(currentTime);
			logger.info(-1 + " transient finished at "+ currentTime +".");
		} else {
			batchData = Measurement.getBatchData(0);
			init(events, publisher, peers, currentTime, batchData);
		}
		
		int batchNumber = 0;
		double sumMeanDownloadTime = 0.0;
		while(!Measurement.confidenceInterval95()) {
//			currentTime = 0;
//			System.out.println(batchNumber);
//		for (; batchNumber < 5000; batchNumber++) {
			batchData = Measurement.getBatchData(batchNumber);
			batchData.setInitialPopulation(peers.size());
//			for (int batchEvent = 0; batchEvent< batchSize; batchEvent++) {
			boolean firstEvent = true;
			while (batchSize > batchData.getDownloadTimes().size()) {
				Event currentEvent = events.poll();
				currentTime = currentEvent.getTime();
				
				if (firstEvent) {
					firstEvent = false;
					batchData.setStartTime(currentTime);
					logger.info(batchNumber + " batch started at "+ currentTime +".");
				}
				
				events.addAll(currentEvent.nextEvents(batchData));
				
//				if (batchEvent % 1000 == 0) {
//					System.gc();
//				}
			}
			batchData.setEndTime(currentTime);
			logger.info(batchNumber + " batch finished at "+ currentTime +".");
			
			sumMeanDownloadTime = sumMeanDownloadTime + batchData.getMeanDownloadTime();
			
			batchNumber++;
		}
		
		
		logger.info("Simulation end.");
		
		double totalMeanDownloadTime = sumMeanDownloadTime/(batchNumber-1);
		double inferiorLimitCI = totalMeanDownloadTime - Measurement.valueConfidenceInterval95();
		double superiorLimitCI = totalMeanDownloadTime + Measurement.valueConfidenceInterval95();
		
		
		logger.info("======================================");
		logger.info("Tempo Médio de Download da Simulação: " + totalMeanDownloadTime);
		logger.info("Intervalo de Confiança: " + Measurement.valueConfidenceInterval95());
		logger.info("Limite Inferior Intervalo de Confiança: " + inferiorLimitCI);
		logger.info("Limite Superior Intervalo de Confiança: " + superiorLimitCI);
	}

	private void init(PriorityQueue<Event> events, Publisher publisher,
			Collection<Peer> peers, double currentTime, BatchData batchData) {
		if (initialPopulationSize == 0) {
			events.add(new ArrivalEvent(currentTime + ArrivalEvent.PEERS_ARRIVAL.nextRandom(), new Peer(), peers, batchData));
		} else {
			for (int i = 1; i <= initialPopulationSize; i++) {
				peers.add(new Peer());
				events.add(new PeerUploadEvent(currentTime + PeerUploadEvent.PEER_UPLOAD_CLOCK.nextRandom(), publisher, peers, batchData));
			}
		}
		events.add(new PublisherUploadEvent(currentTime + PublisherUploadEvent.PUBLISHER_UPLOAD_CLOCK.nextRandom(), publisher, peers, batchData));
	}
	
//	public static class SimulationParameters {
//		public double lambda;
//		public int blocksNumber;
//		public double mi;
//		public double u;
//		public double gama;
//		public double p;
//		public double initialPopulationSize;
//		public boolean blockRarity;
//		public long randomSeed;
//		public int batchSize;
//		public int batches;
//		public int transientSize;
//	}

}
