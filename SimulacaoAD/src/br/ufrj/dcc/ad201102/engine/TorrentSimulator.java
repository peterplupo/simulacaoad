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
	private static Integer TYPE_SCENARIO = 30;
	
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
//	int batches;
	private int transientSize = 100;
	
	public static void main(String[] args) {
		
		SimulationParameters params;
		TorrentSimulator simulator;
		
		String filePrefix = "graficos\\cenario" + TYPE_SCENARIO + "\\reports" + TYPE_SCENARIO;
		
		params = new Scenario().getScenario(TYPE_SCENARIO);
		
		//execucao populacao aberta
//		simulator = new TorrentSimulator(params);
//		simulator.simulate();
//		ReportGenerator.getPopulationPMF(filePrefix, Measurement.getBatchData(false));
//		ReportGenerator.getDownloadTimeCDF(filePrefix, Measurement.getBatchData(false));
//		ReportGenerator.getMeanDownloadTime(filePrefix, Measurement.getBatchData(false));
//		ReportGenerator.getTimes(filePrefix, Measurement.getBatchData(false));
		
		//Execucao populacao fechada
		for (int i = 1; i <= 50; i++) {
			logger.info("Run "+ i +" started.");
			params.setInitialPopulationSize(i);
			simulator = new TorrentSimulator(params);
			simulator.simulate();
			Measurement.newRun(i);
		}
		ReportGenerator.getOutput(filePrefix, false);
		ReportGenerator.getTimes(filePrefix, Measurement.getBatchData(false));
		
		
//		ReportGenerator.getTransientAnalisys(filePrefix, Measurement.getBatchData(true));
		
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
	
	public TorrentSimulator(double lambda, int blocksNumber, double mi,
			double u, double gama, double p, int initialPopulationSize,
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
//		this.batches = batches;
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
//		int eventCounter = 0;
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
				currentEvent.nextEvents(batchData);
//				Measurement.addEventAt(currentTime, eventCounter);
//				eventCounter++;
			}
			batchData.setEndTime(currentTime);
			logger.info(-1 + " transient finished at "+ currentTime +".");
		} else {
			batchData = Measurement.getBatchData(0);
			init(events, publisher, peers, currentTime, batchData);
		}
		
		int batchNumber = 0;
//		ArrayList<Double> medianDownloadTimes = new ArrayList<Double>();
		
		while(!Measurement.confidenceInterval95()) {
//			currentTime = 0;
//			System.out.println(batchNumber);
//		for (; batchNumber < 5000; batchNumber++) {
			batchData = Measurement.getBatchData(batchNumber);
			batchData.setInitialBatchPopulation(peers.size());
//			for (int batchEvent = 0; batchEvent< batchSize; batchEvent++) {
			boolean firstEvent = true;
			while (batchSize > batchData.getDownloadTimes().length) {
				Event currentEvent = events.poll();
				currentTime = currentEvent.getTime();
				if (firstEvent) {
					firstEvent = false;
					batchData.setStartTime(currentTime);
					logger.info(batchNumber + " batch started at "+ currentTime +".");
				}
				currentEvent.nextEvents(batchData);
//				Measurement.addEventAt(currentTime, eventCounter);
//				eventCounter++;
			}
			batchData.setEndTime(currentTime);
			logger.info(batchNumber + " batch finished at "+ currentTime +".");
			
//			medianDownloadTimes.add(batchData.getMedianDownloadTime());
			
			batchNumber++;
			
//			if (batchNumber % 30 == 0) {
//				System.gc();
//				Thread.yield();
//			}
			
		}
		
		events.clear();
		logger.info("Simulation end.");
		
		
	}

//	private double getMedianDownloadTimes(ArrayList<Double> medianDownloadTimes) {
//		double median = 0.0;
//		int divisao = 0;
//		Collections.sort(medianDownloadTimes);
//		if((medianDownloadTimes.size() %2) == 1) {
//			divisao = medianDownloadTimes.size()/2;
//			median = medianDownloadTimes.get(divisao);
//			
//		} else {
//			divisao = medianDownloadTimes.size()/2;
//			median = ((divisao-1) + (divisao+1))/2;
//			median = medianDownloadTimes.get(divisao);
//		}
//		
//		return median;
//	}

	private void init(PriorityQueue<Event> events, Publisher publisher,
			Collection<Peer> peers, double currentTime, BatchData batchData) {
		if (initialPopulationSize == 0) {
			Measurement.setPopulationStatsOn(true);
			events.add(new ArrivalEvent(currentTime + ArrivalEvent.PEERS_ARRIVAL.nextRandom(), new Peer(), peers, batchData, events));
		} else {
			Measurement.setPopulationStatsOn(false);
			for (int i = 1; i <= initialPopulationSize; i++) {
				Peer peer = new Peer();
				peers.add(peer);
				PeerUploadEvent peerUploadEvent = new PeerUploadEvent(currentTime + PeerUploadEvent.PEER_UPLOAD_CLOCK.nextRandom(), publisher, peers, batchData, events);
				events.add(peerUploadEvent);
				peer.addUploadEvent(peerUploadEvent);
			}
		}
		events.add(new PublisherUploadEvent(currentTime + PublisherUploadEvent.PUBLISHER_UPLOAD_CLOCK.nextRandom(), publisher, peers, batchData, events));
	}
	

}
