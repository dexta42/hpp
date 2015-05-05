package fr.tse.fi2.hpp.labs.queries.impl.lab4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.dispatcher.StreamingDispatcher;
import fr.tse.fi2.hpp.labs.main.MainStreaming;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

@State(Scope.Thread)
public class Benchmark {
	final static Logger logger = LoggerFactory.getLogger(MainStreaming.class);

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// Init query time measure
		QueryProcessorMeasure measure = new QueryProcessorMeasure();
		// Init dispatcher
		StreamingDispatcher dispatch = new StreamingDispatcher(
				"src/main/resources/data/sorted_data.csv");

		// Query processors
		List<AbstractQueryProcessor> processors = new ArrayList<>();
		// Add you query processor here
		 RouteMembershipProcessor RMP = new  RouteMembershipProcessor(measure);
		
		processors.add(RMP);
		// Register query processors
		for (AbstractQueryProcessor queryProcessor : processors) {
			dispatch.registerQueryProcessor(queryProcessor);
		}
		// Initialize the latch with the number of query processors
		CountDownLatch latch = new CountDownLatch(processors.size());
		// Set the latch for every processor
		for (AbstractQueryProcessor queryProcessor : processors) {
			queryProcessor.setLatch(latch);
		}
		// Start everything
		for (AbstractQueryProcessor queryProcessor : processors) {
			// queryProcessor.run();
			Thread t = new Thread(queryProcessor);
			t.setName("QP" + queryProcessor.getId());
			t.start();
		}
		Thread t1 = new Thread(dispatch);
		t1.setName("Dispatcher");
		t1.start();

		// Wait for the latch
		try {
			latch.await();
		} catch (InterruptedException e) {
			logger.error("Error while waiting for the program to end", e);
		}
		// Output measure and ratio per query processor
		measure.setProcessedRecords(dispatch.getRecords());
		measure.outputMeasure();
		
		float dlon =  (float) -73.98353;
		float dlat =  (float) 40.749985;
		float alon =  (float) -73.99183;
		float alat =  (float) 40.74913;
		String lic =  "441D5B00E6EC31C7951D9E5E81CA6A57";
		
		int ligne = RMP.CheckRoute(dlon, dlat, alon, alat, lic);
		//System.out.println(ligne);
		if(ligne!=-1){
			ligne+=1;
			System.out.println("Found it at : "+ ligne );
		}
		else{
			System.out.println("Try again ...");
		}
		
		

}
