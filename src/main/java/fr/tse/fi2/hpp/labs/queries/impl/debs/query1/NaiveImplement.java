package fr.tse.fi2.hpp.labs.queries.impl.debs.query1;

import java.util.Date;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class NaiveImplement extends AbstractQueryProcessor {

	private int nb = 0;
	private float sum = 0;

	public NaiveImplement(QueryProcessorMeasure measure) {
		super(measure);
	}
	
	
	private int cellX(float x) {

		// double x=0;
		double x_0 = -74.913585;
		double delta_x = 0.005986 ;

		// double cell_x;
		Double cell_x = 1 + Math.floor(((x - x_0) / delta_x) + 0.5);

		return cell_x.intValue();
	}

	private int cellY(double y) {

		double y_0 = 41.474937;
		double delta_y = 0.004491556;

		Double cell_y = 1 + Math.floor(((y_0 - y) / delta_y) + 0.5);

		return cell_y.intValue();

	}

	@SuppressWarnings("deprecation")
	
	
	@Override
	protected void process(DebsRecord record) {
		System.out.println("cell x : " + cellX(record.getPickup_longitude()) +" y : "+cellY(record.getPickup_latitude()));
				
	
	
	}

}