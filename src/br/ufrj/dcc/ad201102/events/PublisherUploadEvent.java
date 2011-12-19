package br.ufrj.dcc.ad201102.events;

import java.util.Collection;

import br.ufrj.dcc.ad201102.data.BatchData;
import br.ufrj.dcc.ad201102.engine.Exponential;
import br.ufrj.dcc.ad201102.model.Peer;
import br.ufrj.dcc.ad201102.model.Publisher;

public class PublisherUploadEvent extends UploadEvent {
	
	public static Exponential PUBLISHER_UPLOAD_CLOCK;
	
	public PublisherUploadEvent(double time, Peer newPeer, Collection<Peer> peers, BatchData batchData, Collection<Event> events) {
		super(time, newPeer, peers, batchData, events);
	}

	@Override
	void addNextUploadEvent(BatchData newBatchData) {
		events.add(new PublisherUploadEvent(time + PUBLISHER_UPLOAD_CLOCK.nextRandom(), (Publisher)peer, peers, newBatchData, events));
	}
	
}
