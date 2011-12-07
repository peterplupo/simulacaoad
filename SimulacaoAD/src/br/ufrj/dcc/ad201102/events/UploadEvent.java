package br.ufrj.dcc.ad201102.events;

import java.util.Collection;

import br.ufrj.dcc.ad201102.metrics.Metrics;
import br.ufrj.dcc.ad201102.model.Peer;

public abstract class UploadEvent extends Event {
	
	public UploadEvent(double time, Peer peer, Collection<Peer> peers, int batchNumber) {
		super(time, peer, peers, batchNumber);
	}

	@Override
	void runEvent(Collection<Event> events, int newBatchNumber) {
		Peer destinationPeer = peer.sendBlock(peers);
		System.out.println(batchNumber + " " + peer.getId() + " uploaded at " + time);
		if (destinationPeer != null && destinationPeer.isSeed() && destinationPeer.getExitTime() == 0) {
			destinationPeer.setExitTime(time + ExitEvent.EXIT_CLOCK.nextRandom());
			events.add(new ExitEvent(destinationPeer.getExitTime(), destinationPeer, peers, newBatchNumber));
			Metrics.addDownloadTime(batchNumber, time, time - destinationPeer.getArrivalTime());
			System.out.println(batchNumber + " " + destinationPeer.getId() + " will exit at " + destinationPeer.getExitTime() + ". Download time " + (time - destinationPeer.getArrivalTime()) + " added to metrics.");
		}
		addNextUploadEvent(events, newBatchNumber);
	}
	
	abstract void addNextUploadEvent(Collection<Event> events, int newBatchNumber);
	
}
