package br.ufrj.dcc.ad201102.events;

import java.util.Collection;

import br.ufrj.dcc.ad201102.engine.Exponential;
import br.ufrj.dcc.ad201102.model.Peer;

public class PeerUploadEvent extends UploadEvent {
	
	public static Exponential PEER_UPLOAD_CLOCK;
	
	public PeerUploadEvent(double time, Peer peer, Collection<Peer> peers, int batchNumber) {
		super(time, peer, peers, batchNumber);
	}

	@Override
	void addNextUploadEvent(Collection<Event> events, int newBatchNumber) {
		
		double uploadTime = time + PeerUploadEvent.PEER_UPLOAD_CLOCK.nextRandom();
		
		if (!peer.isSeed() || (peer.isSeed() && uploadTime < peer.getExitTime())) {
			events.add(new PeerUploadEvent(uploadTime, peer, peers, newBatchNumber));
		}
	}
	
}
