package br.ufrj.dcc.ad201102.events;

import java.util.Collection;

import br.ufrj.dcc.ad201102.data.BatchData;
import br.ufrj.dcc.ad201102.engine.Exponential;
import br.ufrj.dcc.ad201102.model.Peer;

public class ArrivalEvent extends Event {

	public static Exponential PEERS_ARRIVAL;
	public static int MAX_PEERS;
	
	public ArrivalEvent(double time, Peer newPeer, Collection<Peer> peers, BatchData batchData) {
		super(time, newPeer, peers, batchData);
	}

	@Override
	void runEvent(Collection<Event> events, BatchData newBatchData) {
		events.add(new ArrivalEvent(time + PEERS_ARRIVAL.nextRandom(), new Peer(), peers, newBatchData));
		if (MAX_PEERS == 0 || peers.size() < MAX_PEERS) {
			peer.setArrivalTime(time);
			peers.add(peer);
			System.out.println(batchData + " " + peer + " arrived at " + time + ". Population size " + (peers.size()+1) + " added to metrics.");
			batchData.addPopulationSize(time, peers.size());
			events.add(new PeerUploadEvent(time + PeerUploadEvent.PEER_UPLOAD_CLOCK.nextRandom(), peer, peers, newBatchData));
		}
	}

}
