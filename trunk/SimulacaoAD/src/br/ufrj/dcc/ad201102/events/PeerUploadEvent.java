package br.ufrj.dcc.ad201102.events;

import java.util.Collection;

import br.ufrj.dcc.ad201102.data.BatchData;
import br.ufrj.dcc.ad201102.engine.Exponential;
import br.ufrj.dcc.ad201102.model.Peer;

public class PeerUploadEvent extends UploadEvent {
	
	public static Exponential PEER_UPLOAD_CLOCK;
	
	public PeerUploadEvent(double time, Peer peer, Collection<Peer> peers, BatchData batchData) {
		super(time, peer, peers, batchData);
	}

	@Override
	void addNextUploadEvent(Collection<Event> events, BatchData newbatchData) {
		
		double uploadTime = time + PeerUploadEvent.PEER_UPLOAD_CLOCK.nextRandom();
		
		if (!peer.isSeed() || (peer.isSeed() && uploadTime < peer.getExitTime())) {
			events.add(new PeerUploadEvent(uploadTime, peer, peers, newbatchData));
		}
	}
	
}
