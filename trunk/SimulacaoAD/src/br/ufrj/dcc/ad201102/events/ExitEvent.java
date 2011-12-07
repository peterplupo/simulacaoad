package br.ufrj.dcc.ad201102.events;

import java.util.Collection;

import br.ufrj.dcc.ad201102.engine.Exponential;
import br.ufrj.dcc.ad201102.engine.Uniform;
import br.ufrj.dcc.ad201102.metrics.Metrics;
import br.ufrj.dcc.ad201102.model.Peer;

public class ExitEvent extends Event {

	public static Exponential EXIT_CLOCK;
	public static double P;
	public static Uniform EXIT_PROBABILITY;
	
	public ExitEvent(double time, Peer peer, Collection<Peer> peers, int batchNumber) {
		super(time, peer, peers, batchNumber);
	}

	@Override
	void runEvent(Collection<Event> events, int newBatchNumber) {
		peers.remove(peer);
		if (P > EXIT_PROBABILITY.nextRandom()) {
			events.add(new ArrivalEvent(time + ArrivalEvent.PEERS_ARRIVAL.nextRandom(), new Peer(), peers, newBatchNumber));
		}
		Metrics.addPopulationSize(batchNumber, time, peers.size()+1);
		Metrics.addExit(batchNumber, time);
		System.out.println(batchNumber + " " + peer.getId() + " Exit at " + time + " added to metrics. Pop. size " + (peers.size()+1) + " added to metrics.");
	}

}
