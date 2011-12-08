package br.ufrj.dcc.ad201102.events;

import java.util.ArrayList;
import java.util.Collection;

import br.ufrj.dcc.ad201102.model.Peer;

public abstract class Event implements Comparable<Event>{
	
	double time;
	Collection<Peer> peers;
	Peer peer;
	int batchNumber;

	public double getTime() {
		return time;
	}

	public Event(double time, Peer peer, Collection<Peer> peers, int batchNumber) {
		super();
		this.time = time;
		this.peer = peer;
		this.peers = peers;
		this.batchNumber = batchNumber;
	}

	@Override
	public int compareTo(Event o) {
		return new Double(time).compareTo(new Double(o.getTime()));
	}

	public Collection<Event> nextEvents(int newBatchNumber) {
		Collection<Event> events = new ArrayList<Event>();
		runEvent(events, newBatchNumber);
		return events;
	}

	abstract void runEvent(Collection<Event> events, int batchNumber);
	
}