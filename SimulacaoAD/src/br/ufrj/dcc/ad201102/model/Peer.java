package br.ufrj.dcc.ad201102.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import br.ufrj.dcc.ad201102.engine.Uniform;

public class Peer {

	public static Uniform CHOICES;
	public static int BLOCKS_NUMBER;
	public static int idCounter;
	
	public static int[] BLOCKS_FREQUENCY;
	public static boolean BLOCK_RARITY;
	
	private boolean[] signature;
	private boolean seed;
	private double exitTime;
	private double arrivalTime;
	private String id;
	
	public Peer() {
		signature = new boolean[BLOCKS_NUMBER];
		id = getClass().getSimpleName() + "#" + idCounter;
		idCounter++;
	}
	
	public boolean isSeed() {
		return seed;
	}
	
	public void setSeed(boolean seed) {
		this.seed = seed;
		System.out.println(getId() + " is now a seed.");
	}
	
	public boolean[] getSignature() {
		return signature;
	}
	
	public void addBlock(int position) {
		if (!signature[position]) {
			signature[position] = true;
			BLOCKS_FREQUENCY[position]++;
			for (int i = 0; i < signature.length; i++) {
				if (!signature[i]) {
					return;
				}
			}
			seed = true;
		}
	}
	
	public String getId() {
		return id;
	}
	
	public Peer sendBlock(Collection<Peer> peers) {
		List<Peer> shuffledPeers = new ArrayList<Peer>(peers);
		Collections.shuffle(shuffledPeers, CHOICES.getRandom());
		
		Peer chosenPeer = null;
		if (shuffledPeers.size() != 0) {
			chosenPeer = shuffledPeers.get(0);
		} else {
			return null;
		}
		
		//transverse through all peers in no order (shuffled collection)
//		for (Peer peer : shuffledPeers) {
			//compare signatures
//			if (!peer.signature.equals(signature)) {
//				chosenPeer = peer;
				//a peer with different signature was found and is going to receive a block
				//there is no need to keep looking (useful peer found)
//				break;
//			}
//		}
		
		//a peer was found, send a block
		//send most rare block
		int blockNumber = 0;
		if (BLOCK_RARITY) {
			int frequency = Integer.MAX_VALUE;
			for (int i = 0; i<BLOCKS_FREQUENCY.length; i++) {
				if (BLOCKS_FREQUENCY[i] <= frequency && signature[i] && chosenPeer.signature[i] == false) {
					blockNumber = i;
					frequency = BLOCKS_FREQUENCY[i];
				}
			}
			if (frequency == Integer.MAX_VALUE) {
				return chosenPeer;
			}
		//send any random block
		} else {
			List<Integer> candidateBlocks = new ArrayList<Integer>();
			for (int i = 0; i < chosenPeer.signature.length; i++) {
				//this if was intended to send only useful blocks
				if (chosenPeer.signature[i] == false && signature[i] == true) {
					//findind out which blocks the destination peer doens't have and this does
						candidateBlocks.add(i);
				}
			}
			if (candidateBlocks.size() != 0) {
				Collections.shuffle(candidateBlocks, CHOICES.getRandom());
				blockNumber = candidateBlocks.get(0);
			}
		}
		chosenPeer.addBlock(blockNumber);
		
		return chosenPeer;
	}

	public double getExitTime() {
		return exitTime;
	}

	public void setExitTime(double exitTime) {
		this.exitTime = exitTime;
	}

	public double getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(double arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

}
