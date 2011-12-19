package br.ufrj.dcc.ad201102.events;

import java.util.Collection;

import org.apache.log4j.Logger;

import br.ufrj.dcc.ad201102.data.BatchData;
import br.ufrj.dcc.ad201102.engine.Exponential;
import br.ufrj.dcc.ad201102.model.Peer;

public class ArrivalEvent extends Event {

	private static Logger logger = Logger.getLogger(ArrivalEvent.class);
	
	public static Exponential PEERS_ARRIVAL;
	public static int MAX_PEERS;
	
	public ArrivalEvent(double time, Peer newPeer, Collection<Peer> peers, Collection<Peer> seeds, BatchData batchData, Collection<Event> events) {
		super(time, newPeer, peers, seeds, batchData, events);
	}

	@Override
	void runEvent(BatchData newBatchData) {
		events.add(new ArrivalEvent(time + PEERS_ARRIVAL.nextRandom(), new Peer(), peers, seeds, newBatchData, events));
		if (MAX_PEERS == 0 || peers.size() < MAX_PEERS) {
			peer.setArrivalTime(time);
			peers.add(peer);
			batchData.addPopulationSize(time, peers.size() + seeds.size());
			PeerUploadEvent peerUploadEvent = new PeerUploadEvent(time + PeerUploadEvent.PEER_UPLOAD_CLOCK.nextRandom(), peer, peers, seeds, newBatchData, events);
			events.add(peerUploadEvent);
			peer.addUploadEvent(peerUploadEvent);
			events.add(peerUploadEvent);
			logger.debug(batchData + " " + peer + " arrived at " + time + ". Population size measure " + (peers.size() + seeds.size()) + " registered.");
		}
	}

}
