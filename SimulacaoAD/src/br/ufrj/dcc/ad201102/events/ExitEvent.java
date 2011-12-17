package br.ufrj.dcc.ad201102.events;

import java.util.Collection;

import org.apache.log4j.Logger;

import br.ufrj.dcc.ad201102.data.BatchData;
import br.ufrj.dcc.ad201102.engine.Exponential;
import br.ufrj.dcc.ad201102.engine.Uniform;
import br.ufrj.dcc.ad201102.model.Peer;

public class ExitEvent extends Event {

	private static Logger logger = Logger.getLogger(ExitEvent.class);
	
	public static Exponential EXIT_CLOCK;
	public static double P;
	public static Uniform EXIT_PROBABILITY;
	
	public ExitEvent(double time, Peer newPeer, Collection<Peer> peers, BatchData batchData, Collection<Event> events) {
		super(time, newPeer, peers, batchData, events);
	}

	@Override
	void runEvent(BatchData newBatchData) {
		peers.remove(peer);
		events.removeAll(peer.getUploadEvents());
		if (P > EXIT_PROBABILITY.nextRandom()) {
			events.add(new ArrivalEvent(time, new Peer(), peers, newBatchData, events));
		}
		batchData.addPopulationSize(time, peers.size());
		batchData.addExit(time);
		logger.debug(batchData + " " + peer + " Exit time measure " + time + " and population size measure " + peers.size() + " registered.");
	}

}
