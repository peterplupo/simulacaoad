package br.ufrj.dcc.ad201102.events;

import java.util.Collection;

import org.apache.log4j.Logger;

import br.ufrj.dcc.ad201102.data.BatchData;
import br.ufrj.dcc.ad201102.model.Peer;

public abstract class UploadEvent extends Event {
	
	private static Logger logger = Logger.getLogger(UploadEvent.class);
	
	public UploadEvent(double time, Peer peer, Collection<Peer> peers, BatchData batchData) {
		super(time, peer, peers, batchData);
	}

	@Override
	void runEvent(Collection<Event> events, BatchData newbatchData) {
		Peer destinationPeer = peer.sendBlock(peers);
		logger.debug(batchData + " " + peer + " uploaded at " + time);
		if (destinationPeer != null && destinationPeer.isSeed() && destinationPeer.getExitTime() == 0) {
			destinationPeer.setExitTime(time + ExitEvent.EXIT_CLOCK.nextRandom());
			events.add(new ExitEvent(destinationPeer.getExitTime(), destinationPeer, peers, newbatchData));
			batchData.addDownloadTime(time, time - destinationPeer.getArrivalTime());
			logger.debug(batchData + " " + destinationPeer + " will exit at " + destinationPeer.getExitTime() + ". Download time measure " + (time - destinationPeer.getArrivalTime()) + " registered.");
		}
		addNextUploadEvent(events, newbatchData);
	}
	
	abstract void addNextUploadEvent(Collection<Event> events, BatchData newbatchData);
	
}
