package fr.tse.fi2.hpp.labs.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.dispatcher.StreamingDispatcher;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;
import fr.tse.fi2.hpp.labs.queries.impl.lab5b.RecordBloomMembershipProcessor;
import fr.tse.fi2.hpp.labs.queries.impl.lab5b.RecordMembershipProcessor;
import fr.tse.fi2.hpp.labs.queries.impl.lab5b.RecordMixMembershipProcessor;

/**
 * Main class of the program. Register your new queries here
 * 
 * Design choice: no thread pool to show the students explicit {@link CountDownLatch} based synchronization.
 * 
 * @author Julien
 * 
 */
@State(Scope.Thread)
@Fork(5)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class BenchmarkMainStreaming {

    final static Logger logger = LoggerFactory.getLogger(BenchmarkMainStreaming.class);

    private static RecordMembershipProcessor processor;
    private static RecordBloomMembershipProcessor bprocessor;
    private static RecordMixMembershipProcessor mprocessor;

    protected static DebsRecord rndRecord;

    /**
     * @param args
     * @throws IOException
     */

    @Setup
    public static void loadData() {
        // Init query time measure
        final QueryProcessorMeasure measure = new QueryProcessorMeasure();
        // Init dispatcher
        final StreamingDispatcher dispatch = new StreamingDispatcher("src/main/resources/data/1000Records.csv");

        // Query processors
        final List<AbstractQueryProcessor> processors = new ArrayList<>();
        // Add you query processor here
        processor = new RecordMembershipProcessor(measure);
        bprocessor = new RecordBloomMembershipProcessor(measure);
        mprocessor = new RecordMixMembershipProcessor(measure);
        processors.add(processor);
        processors.add(bprocessor);
        processors.add(mprocessor);
        // Register query processors
        for (final AbstractQueryProcessor queryProcessor : processors) {
            dispatch.registerQueryProcessor(queryProcessor);
        }
        // Initialize the latch with the number of query processors
        final CountDownLatch latch = new CountDownLatch(processors.size());
        // Set the latch for every processor
        for (final AbstractQueryProcessor queryProcessor : processors) {
            queryProcessor.setLatch(latch);
        }
        // Start everything
        for (final AbstractQueryProcessor queryProcessor : processors) {
            // queryProcessor.run();
            final Thread t = new Thread(queryProcessor);
            t.setName("QP" + queryProcessor.getId());
            t.start();
        }
        final Thread t1 = new Thread(dispatch);
        t1.setName("Dispatcher");
        t1.start();

        // Wait for the latch
        try {
            latch.await();
        } catch (final InterruptedException e) {
            logger.error("Error while waiting for the program to end", e);
        }
        // Output measure and ratio per query processor
        measure.setProcessedRecords(dispatch.getRecords());
        measure.outputMeasure();

        rndRecord = processor.getRandomRecord();

    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public boolean search() {
        return processor.exists(rndRecord);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public boolean searchBloom() {
        return bprocessor.exists(rndRecord);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public boolean searchMix() {
        return mprocessor.exists(rndRecord);
    }
}