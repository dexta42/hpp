package fr.tse.fi2.hpp.labs.queries.impl.lab2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class Write implements Runnable {
	
	private int nb = 0;
	private float sum = 0;
	DebsRecord record;
	
	

	
	public final BlockingQueue<String> Resultqueue;
	
	private static BufferedWriter outputWriter;
	private final int id ;
	private final static Logger logger = LoggerFactory
			.getLogger(AbstractQueryProcessor.class);
	
	
	
	 public Write( BlockingQueue<String> Resultqueue, int id ) {
	        
	        this.id  =id;
	        this.Resultqueue = Resultqueue;
	        
	        try {
				outputWriter=new BufferedWriter(new FileWriter(new File(
						"result/query" + id + ".txt")));
			} catch (IOException e) {
				logger.error("Cannot open output file for " + id, e);
				System.exit(-1);
			}
	    }
	 
	public void run ()  {
		String line="";
		while(true){
		try {
			 line = Resultqueue.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(line.equals("DIE!!!")){
			break;
		}
		else{
			writeLine(line);
		}
		}
		finish();
		
		 }
	
	protected void writeLine(String line) {
		try {
			outputWriter.write(line);
			outputWriter.newLine();
		} catch (IOException e) {
			logger.error("Could not write new line for query processor " + id
					+ ", line content " + line, e);
		}
		
		

	}
	
	protected void finish() {
		// Close writer
		try {
			outputWriter.flush();
			outputWriter.close();
		} catch (IOException e) {
			logger.error("Cannot property close the output file for query "
					+ id, e);
		}
	}
	
	
}