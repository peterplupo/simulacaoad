package br.ufrj.dcc.ad201102.events;

import java.util.Collection;

import br.ufrj.dcc.ad201102.data.BatchData;
import br.ufrj.dcc.ad201102.model.Peer;

public abstract class Event implements Comparable<Event>{
	
	double time;
	Collection<Peer> peers;
	Peer peer;
	BatchData batchData;
	Collection<Event> events;
	Collection<Peer> seeds;

	public double getTime() {
		return time;
	}

	public Event(double time, Peer peer, Collection<Peer> peers, Collection<Peer> seeds, BatchData batchData, Collection<Event> events) {
		super();
		this.time = time;
		this.peer = peer;
		this.peers = peers;
		this.batchData = batchData;
		this.events = events;
		this.seeds = seeds;
	}

	@Override
	public int compareTo(Event o) {
		return new Double(time).compareTo(new Double(o.getTime()));
	}

	public void nextEvents(BatchData batchData) {
		runEvent(batchData);
	}

	abstract void runEvent(BatchData batchData);
	
}
