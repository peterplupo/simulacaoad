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

public class TorrentSimulator {
	
	private static Logger logger = Logger.getLogger(TorrentSimulator.class);
	private static Integer TYPE_SCENARIO = 50;
	
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
	private int transientSize = 100;
	private static boolean transientAnalisys = false;
	
	public static void main(String[] args) {
		
		SimulationParameters params;
		
		TYPE_SCENARIO = 11;
		params = new Scenario().getScenario(TYPE_SCENARIO);
		run(params);
		
//		System.out.println("40 normal");
		
//		TYPE_SCENARIO = 12;
//		params = new Scenario().getScenario(TYPE_SCENARIO);
//		run(params);
		
//		System.out.println("50 normal");
//		
//		TYPE_SCENARIO = 60;
//		params = new Scenario().getScenario(TYPE_SCENARIO);
//		run(params);
//		
//		System.out.println("60 normal");
//		
//		TYPE_SCENARIO = 40;
//		params = new Scenario().getScenario(TYPE_SCENARIO);
//		params.setBlocksNumber(10);
//		run(params);
//		
//		System.out.println("40 10 blocos");
//		
//		TYPE_SCENARIO = 50;
//		params = new Scenario().getScenario(TYPE_SCENARIO);
//		params.setBlocksNumber(10);
//		run(params);
//		
//		System.out.println("50 10 blocos");
//		
//		TYPE_SCENARIO = 60;
//		params = new Scenario().getScenario(TYPE_SCENARIO);
//		params.setBlocksNumber(10);
//		run(params);
//		
//		System.out.println("60 10 blocos");
//		
//		TYPE_SCENARIO = 40;
//		params = new Scenario().getScenario(TYPE_SCENARIO);
//		params.setBlockRarity(true);
//		run(params);
//		
//		System.out.println("40 raro normal");
//		
//		TYPE_SCENARIO = 50;
//		params = new Scenario().getScenario(TYPE_SCENARIO);
//		params.setBlockRarity(true);
//		run(params);
//		
//		System.out.println("50 raro normal");
//		
//		TYPE_SCENARIO = 60;
//		params = new Scenario().getScenario(TYPE_SCENARIO);
//		params.setBlockRarity(true);
//		run(params);
//		
//		System.out.println("60 raro normal");
		
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

		
	}

	private static void run(SimulationParameters params) {
		String filePrefix = "graficos\\cenario" + TYPE_SCENARIO + "\\reports" + TYPE_SCENARIO;
		//execucao populacao aberta
		if (params.isBlockRarity()) {
			filePrefix = filePrefix + "rarity";
		}
		if (params.getBlocksNumber() == 10) {
			filePrefix = filePrefix + "10blocos";
		}
		if (params.getInitialPopulationSize() == 0) {
			if (transientAnalisys) {
				params.setBlocksNumber(10);
			}
			TorrentSimulator simulator = new TorrentSimulator(params);
			simulator.simulate();
//			ReportGenerator.getPopulationPMF(filePrefix, Measurement.getBatchData(false));
//			ReportGenerator.getDownloadTimeCDF(filePrefix, Measurement.getBatchData(false));
//			ReportGenerator.getTimes(filePrefix, Measurement.getBatchData(false));
//			if (transientAnalisys) {
//				ReportGenerator.getTransientAnalisys(filePrefix, Measurement.getBatchData(true));
//			}
			if (TYPE_SCENARIO < 13) {
				ReportGenerator.getPopulationPMF(filePrefix, Measurement.getBatchData(false), params.getLambda(), params.getMi(), params.getU());
			}
		} else {
			//Execucao populacao fechada
			for (int i = 1; i <= 50; i++) {
				logger.info("Run "+ i +" started.");
				params.setInitialPopulationSize(i);
				TorrentSimulator simulator = new TorrentSimulator(params);
				simulator.simulate();
				if (transientAnalisys) {
					ReportGenerator.getTransientAnalisys(filePrefix, Measurement.getBatchData(true));
				}
				Measurement.newRun(i);
			}
			ReportGenerator.getOutput(filePrefix, false);
		}
	}
	
	public TorrentSimulator(double lambda, int blocksNumber, double mi,
			double u, double gama, double p, int initialPopulationSize,
			boolean blockRarity, long randomSeed, int batchSize, int transientSize) {
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
		this.transientSize = transientSize;
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
		Collection<Peer> seeds = new ArrayList<Peer>();
		
		Measurement.setTransientBatch(transientSize != 0);
		double currentTime = 0;
		BatchData batchData = null;
		int eventCounter = 0;
		if (Measurement.hasTransientBatch()) {
			batchData = Measurement.getTransientBatchData();
			init(events, publisher, peers, seeds, currentTime, batchData);
			logger.info(-1 + " transient started at "+ currentTime +".");
			for (int transientCounter = 0; transientCounter < transientSize; transientCounter++) {
				Event currentEvent = events.poll();
				currentTime = currentEvent.getTime();
				if (transientCounter == 0) {
					batchData.setStartTime(currentTime);
				}
				currentEvent.nextEvents(batchData);
				if (transientAnalisys) {
					Measurement.addEventAt(currentTime, eventCounter);
					eventCounter++;
				}
			}
			batchData.setEndTime(currentTime);
			logger.info(-1 + " transient finished at "+ currentTime +".");
		} else {
			batchData = Measurement.getBatchData(0);
			init(events, publisher, peers, seeds, currentTime, batchData);
		}
		
		int batchNumber = 0;
		
		while(!Measurement.confidenceInterval95()) {
//		for (int i = 0; i < 11; i++) {
			batchData = Measurement.getBatchData(batchNumber);
			batchData.setInitialBatchPopulation(peers.size());
			boolean firstEvent = true;
			while (batchSize > batchData.getDownloadTimes().length) {
//			for (int i = 0; i <= batchSize; i++) {
				Event currentEvent = events.poll();
				if (firstEvent) {
					logger.info(batchNumber + " batch started at "+ currentTime +".");
					firstEvent = false;
					batchData.setStartTime(currentTime);
				}
				currentTime = currentEvent.getTime();
				currentEvent.nextEvents(batchData);
				if (transientAnalisys) {
					Measurement.addEventAt(currentTime, eventCounter);
					eventCounter++;
				}
			}
			batchData.setEndTime(currentTime);
			logger.info(batchNumber + " batch finished at "+ currentTime +".");
			
			batchNumber++;
			
		}
		
		events.clear();
		logger.info("Simulation end.");
		
		
	}

	private void init(PriorityQueue<Event> events, Publisher publisher,
			Collection<Peer> peers, Collection<Peer> seeds, double currentTime, BatchData batchData) {
		if (initialPopulationSize == 0) {
			Measurement.setPopulationStatsOn(true);
			events.add(new ArrivalEvent(currentTime + ArrivalEvent.PEERS_ARRIVAL.nextRandom(), new Peer(), peers, seeds, batchData, events));
		} else {
			Measurement.setPopulationStatsOn(false);
			for (int i = 1; i <= initialPopulationSize; i++) {
				Peer peer = new Peer();
//				peer.addBlock(1);
				peers.add(peer);
				PeerUploadEvent peerUploadEvent = new PeerUploadEvent(currentTime + PeerUploadEvent.PEER_UPLOAD_CLOCK.nextRandom(), publisher, peers, seeds, batchData, events);
				events.add(peerUploadEvent);
				peer.addUploadEvent(peerUploadEvent);
			}
		}
		events.add(new PublisherUploadEvent(currentTime + PublisherUploadEvent.PUBLISHER_UPLOAD_CLOCK.nextRandom(), publisher, peers, seeds, batchData, events));
	}

}
