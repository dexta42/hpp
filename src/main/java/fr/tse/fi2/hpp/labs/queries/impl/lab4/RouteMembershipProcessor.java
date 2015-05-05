package fr.tse.fi2.hpp.labs.queries.impl.lab4;

import java.util.ArrayList;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class RouteMembershipProcessor extends AbstractQueryProcessor {
	ArrayList<DebsRecord> tabRecords = new ArrayList<DebsRecord>();

	public RouteMembershipProcessor(QueryProcessorMeasure measure) {
		super(measure);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void process(DebsRecord record) {
		tabRecords.add(record);
		
		
		// TODO Auto-generated method stub

	}
	
	public int CheckRoute(float dlon, float dlat, float alon, float alat, String lic){
		/*System.out.println("Departure longitude : " + tabRecords.get(200).getPickup_longitude()+"\n");
		System.out.println("Departure latitude : " + tabRecords.get(200).getPickup_latitude()+"\n");
		System.out.println("Arrival longitude : " + tabRecords.get(200).getDropoff_longitude()+"\n");
		System.out.println("Arrival latitude : " + tabRecords.get(200).getDropoff_latitude()+"\n");
		System.out.println("License : " + tabRecords.get(200).getHack_license()+"\n");
		*/
		for(int i=0;i<tabRecords.size();i++){
			if(dlon == tabRecords.get(i).getPickup_longitude() && dlat == tabRecords.get(200).getPickup_latitude() && alon == tabRecords.get(200).getDropoff_longitude() && alat == tabRecords.get(200).getDropoff_latitude() && lic.equals(tabRecords.get(200).getHack_license())){
				return i;
			}

		}
		return -1;
	
	}

}
