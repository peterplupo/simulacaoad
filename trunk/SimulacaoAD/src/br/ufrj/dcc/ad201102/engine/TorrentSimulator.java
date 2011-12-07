package br.ufrj.dcc.ad201102.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.PriorityQueue;

import br.ufrj.dcc.ad201102.events.ArrivalEvent;
import br.ufrj.dcc.ad201102.events.Event;
import br.ufrj.dcc.ad201102.events.ExitEvent;
import br.ufrj.dcc.ad201102.events.PeerUploadEvent;
import br.ufrj.dcc.ad201102.events.PublisherUploadEvent;
import br.ufrj.dcc.ad201102.metrics.Metrics;
import br.ufrj.dcc.ad201102.model.Peer;
import br.ufrj.dcc.ad201102.model.Publisher;


//otimizações:
//1 só enviar blocos úteis
//2 download ao invés de upload (melhora?)
//3 enviar e receber simultaneamente (pode? não é só a política de escolha e de envio que pode mudar?)

public class TorrentSimulator {
	
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
	private int transientSize;
	
	public static void main(String[] args) {
		
		SimulationParameters params = new SimulationParameters();
		
		params.lambda = 0.1;
		params.blocksNumber = 100;
		params.mi = 0.1;
		params.u = 1;
		params.gama = 0.1;
		params.p = 0;
		params.initialPopulationSize = 0;
		params.blockRarity = false;
		params.randomSeed = 0;
		params.batchSize = 50000;
		params.batches = 2;
		params.transientSize = 3000;
		
		TorrentSimulator simulator = new TorrentSimulator(params);
		simulator.simulate();
		Metrics.generateReport();
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
		Metrics.reset();
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
		
		
		if (initialPopulationSize == 0) {
			if (transientSize != 0) {
				events.add(new ArrivalEvent(0 + ArrivalEvent.PEERS_ARRIVAL.nextRandom(), new Peer(), peers, -1));
			} else {
				events.add(new ArrivalEvent(0 + ArrivalEvent.PEERS_ARRIVAL.nextRandom(), new Peer(), peers, 0));
			}
		} else {
			for (int i = 1; i < initialPopulationSize; i++) {
				peers.add(new Peer());
			}
		}
		
		if (transientSize != 0) {
			events.add(new PublisherUploadEvent(0 + PublisherUploadEvent.PUBLISHER_UPLOAD_CLOCK.nextRandom(), publisher, peers, -1));
		} else {
			events.add(new PublisherUploadEvent(0 + PublisherUploadEvent.PUBLISHER_UPLOAD_CLOCK.nextRandom(), publisher, peers, 0));
		}
		
		if (transientSize != 0) {
			System.out.println(-1 + " transient started.");
		}
		
		for (int trans = 0; trans < transientSize; trans++) {
			events.addAll(events.poll().nextEvents(-1));
		}
		
		for (int batchNumber = 0; batchNumber < batches; batchNumber++) {
			System.out.println(batchNumber + " batch started.");
			for (int batchEvent = 0; batchEvent< batchSize; batchEvent++) {
				events.addAll(events.poll().nextEvents(batchNumber));
				if (batchEvent % 1000 == 0) {
					System.gc();
				}
			}
		}
		System.out.println("Simulation end.");
	}
	
	public static class SimulationParameters {
		public double lambda;
		public int blocksNumber;
		public double mi;
		public double u;
		public double gama;
		public double p;
		public double initialPopulationSize;
		public boolean blockRarity;
		public long randomSeed;
		public int batchSize;
		public int batches;
		public int transientSize;
	}

}
