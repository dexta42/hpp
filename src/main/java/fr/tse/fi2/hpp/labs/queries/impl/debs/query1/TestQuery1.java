package fr.tse.fi2.hpp.labs.queries.impl.debs.query1;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.dispatcher.StreamingDispatcher;
import fr.tse.fi2.hpp.labs.main.MainStreaming;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;
import fr.tse.fi2.hpp.labs.queries.impl.debs.query1.NaiveImplement;

public class TestQuery1 {

	@Test
	public void test() {
		Logger logger = LoggerFactory.getLogger(MainStreaming.class);
		// Init query time measure
		QueryProcessorMeasure measure = new QueryProcessorMeasure();
		// Init dispatcher
		StreamingDispatcher dispatch = new StreamingDispatcher(
				"src/main/resources/test/test02/test_02.csv");

		// Query processors
		List<AbstractQueryProcessor> processors = new ArrayList<>();
		// Add you query processor here
		NaiveImplement RMP = new NaiveImplement(measure);

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
		// DebsRecord recordTestFaux = new DebsRecord("", "", 4, 4, 4, 4, 4, 4,
		// 4, 4, "", 4, 4, 4, 4, 4, 4, false);
		
		// System.out.println(RMP.CheckRoute(recordTestFaux));
		
		String lastLineTest = "1.4,1.1,1.3,1.6,1.1,1.5,1.5,1.4,1.4,1.3,1.3,1.1,1.1,1.6,1.5,1.5,1.4,1.4,1.3,1.3";
		assertEquals(NaiveImplement.getLastLine(),lastLineTest);
	}

}
