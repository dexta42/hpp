package fr.tse.fi2.hpp.labs.queries.impl.lab2;

import java.util.concurrent.BlockingQueue;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;


	
public class Incremental implements Runnable {
	
	private int nb = 0;
	private float sum = 0;
	DebsRecord record;
	protected BlockingQueue queue = null;
	
	private void  getRecord (DebsRecord r){
		record = r;
	}
	
	 public Incremental(BlockingQueue queue) {
	        this.queue = queue;
	    }
	public void run () {
		nb++;
		sum += record.getFare_amount();
		queue.add(sum / nb);
		 }
	
}
		
	
