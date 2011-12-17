package br.ufrj.dcc.ad201102.events;

import java.util.Collection;

import org.apache.log4j.Logger;

import br.ufrj.dcc.ad201102.data.BatchData;
import br.ufrj.dcc.ad201102.model.Peer;

public abstract class UploadEvent extends Event {
	
	private static Logger logger = Logger.getLogger(UploadEvent.class);
	
	public UploadEvent(double time, Peer newPeer, Collection<Peer> peers, BatchData batchData, Collection<Event> events) {
		super(time, newPeer, peers, batchData, events);
	}

	@Override
	void runEvent(BatchData newbatchData) {
		Peer destinationPeer = peer.sendBlock(peers);
		if (destinationPeer != null) {
			logger.debug(batchData + " " + peer + " uploaded at " + time + " to peer " + destinationPeer);
		}
		if (destinationPeer != null && destinationPeer.isSeed() && destinationPeer.getExitTime() == 0) {
			destinationPeer.setExitTime(time + ExitEvent.EXIT_CLOCK.nextRandom());
			events.add(new ExitEvent(destinationPeer.getExitTime(), destinationPeer, peers, newbatchData, events));
			batchData.addDownloadTime(time, time - destinationPeer.getArrivalTime());
			logger.debug(batchData + " " + destinationPeer + " will exit at " + destinationPeer.getExitTime() + ". Download time measure " + (time - destinationPeer.getArrivalTime()) + " registered.");
		}
		addNextUploadEvent(newbatchData);
	}
	
	abstract void addNextUploadEvent(BatchData newbatchData);
	
}
