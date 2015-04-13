package fr.tse.fi2.hpp.labs.queries;

import java.util.concurrent.atomic.AtomicInteger;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.GridPoint;
import fr.tse.fi2.hpp.labs.beans.Route;

/**
 * Every query must extend this class that provides basic functionalities such
 * as :
 * <ul>
 * <li>Receives notification from the</li>
 * <li>Grid mapping: maps lat/long to x,y in a discrete grid of given size</li>
 * </ul>
 * 
 * @author Julien
 * 
 */
public abstract class AbstractQueryProcessor {
	/**
	 * Counter to uniquely identify the query processors
	 */
	private static AtomicInteger counter = new AtomicInteger();
	/**
	 * Unique ID of the query processor
	 */
	private int id = counter.incrementAndGet();

	/**
	 * Process an event that is received from the dispatcher
	 * 
	 * @param record
	 */
	public abstract void onReceiveMessage(DebsRecord record);

	/**
	 * 
	 * @param record
	 *            the record to process
	 * @return the route in a 600*600 grid
	 */
	protected Route convertRecordToRoute(DebsRecord record) {
		// Convert pickup coordinates into cell
		float lat1 = record.getPickup_latitude();
		float long1 = record.getPickup_longitude();
		GridPoint pickup = convert(lat1, long1);
		// Convert pickup coordinates into cell
		float lat2 = record.getDropoff_latitude();
		float long2 = record.getDropoff_longitude();
		GridPoint dropoff = convert(lat2, long2);
		return new Route(pickup, dropoff);
	}

	/**
	 * 
	 * @param lat1
	 * @param long1
	 * @return The lat/long converted into grid coordinates
	 */
	private GridPoint convert(float lat1, float long1) {
		return new GridPoint(cellX(lat1), cellY(long1));
	}

	/**
	 * Provided by Syed and Abderrahmen
	 * 
	 * @param x
	 * @return
	 */
	private int cellX(float x) {

		// double x=0;
		double x_0 = -74.913585;
		double delta_x = 0.005986 / 2;

		// double cell_x;
		Double cell_x = 1 + Math.floor(((x - x_0) / delta_x) + 0.5);

		return cell_x.intValue();
	}

	/**
	 * Provided by Syed and Abderrahmen
	 * 
	 * @param y
	 * @return
	 */
	private int cellY(double y) {

		double y_0 = 41.474937;
		double delta_y = 0.004491556 / 2;

		Double cell_y = 1 + Math.floor(((y_0 - y) / delta_y) + 0.5);

		return cell_y.intValue();

	}

}