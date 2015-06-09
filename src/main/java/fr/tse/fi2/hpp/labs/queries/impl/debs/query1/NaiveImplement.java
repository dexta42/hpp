package fr.tse.fi2.hpp.labs.queries.impl.debs.query1;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.GridPoint;
import fr.tse.fi2.hpp.labs.beans.Route;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class NaiveImplement extends AbstractQueryProcessor {

	private int nb = 0;
	private float sum = 0;
	private Date currentTime = new Date();
	private ArrayList<DebsRecord> tabrec =  new ArrayList<DebsRecord>();//a faire en linkedList

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
	
	protected Route convertRecordToRoute(DebsRecord record) {
		// Convert pickup coordinates into cell
		float lat1 = record.getPickup_latitude();
		float long1 = record.getPickup_longitude();
		GridPoint pickup = convert(long1 , lat1);
		// Convert pickup coordinates into cell
		float lat2 = record.getDropoff_latitude();
		float long2 = record.getDropoff_longitude();
		GridPoint dropoff = convert(long2 , lat2);
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
	
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	protected void process(DebsRecord record) {
		long start = System.currentTimeMillis();
		currentTime.setTime(record.getDropoff_datetime());
		LinkedHashMap<Route,Integer> tabRoutes = new LinkedHashMap<Route,Integer>();
		
		for(int i=0;i<tabrec.size();i++){
			if ((currentTime.getTime() - tabrec.get(i).getDropoff_datetime()) >1800000){
				tabrec.remove(i);
				
			}
		}
		
		tabrec.add(record);
		
		for(int i=0;i<tabrec.size();i++){
			Route r = convertRecordToRoute( tabrec.get(i));
			
			Iterator<Route> keySetIterator = tabRoutes.keySet().iterator();
			boolean find = false;
			
			while(keySetIterator.hasNext()){
				  Route key = keySetIterator.next();
				  //System.out.println("r :  " + r.getPickup().getX()+" "+r.getPickup().getY()+" to "+r.getDropoff().getX()+" "+r.getDropoff().getY());
				  //System.out.println("key :  " + key.getPickup().getX()+" "+key.getPickup().getY()+" to "+key.getDropoff().getX()+" "+key.getDropoff().getY() );
				  if(r.equals(key)){
					  Integer nb=tabRoutes.get(key);
					  tabRoutes.replace(key, nb, nb+1);
					  find = true;
				  }
				}
			
			if(!find){
				tabRoutes.put(r, 1);
			}
			
		
		}
		
		 
		/*TreeMap<Route,Integer> sortedtabRoutes = new TreeMap<Route,Integer>(tabRoutes);
		Iterator<Route> it2 = sortedtabRoutes.keySet().iterator();
		System.out.println("Start");
		
		
		while(it2.hasNext()){
			 Route key = it2.next();
			System.out.println("Route :  " + key.getPickup().getX()+" "+key.getPickup().getY()+" to "+key.getDropoff().getX()+" "+key.getDropoff().getY() + " nb : " + tabRoutes.get(key));
		}
		
		System.out.println("END");
	*/  
		LinkedHashMap <Route,Integer> reverseRoutes = new LinkedHashMap <Route,Integer>();
		Iterator<Route> keySetIterator = tabRoutes.keySet().iterator();
		while(!tabRoutes.isEmpty()){
			
			keySetIterator = tabRoutes.keySet().iterator();
			Route lastr = null;
			while(keySetIterator.hasNext()){
				Route key = keySetIterator.next();
				lastr = key;
				
			}
			reverseRoutes.put(lastr, tabRoutes.get(lastr));
			tabRoutes.remove(lastr);
			System.out.println("Rwoute :  " + lastr.getPickup().getX()+" "+lastr.getPickup().getY()+" to "+lastr.getDropoff().getX()+" "+lastr.getDropoff().getY() + " nb : " + reverseRoutes.get(lastr));
			
		}
		
		
		Iterator<Route> keySetIterator2 = reverseRoutes.keySet().iterator();
		LinkedHashMap <Route,Integer> sortedRoutes = new LinkedHashMap <Route,Integer>();
		//Map<Route,Integer> sortedRoutes  = new TreeMap<Route,Integer>(Collections.reverseOrder());
		while(!reverseRoutes.isEmpty()){
			keySetIterator2 = reverseRoutes.keySet().iterator();
			Route rmax = keySetIterator2.next();
			Integer  nb=-1;
			while(keySetIterator2.hasNext()){
				Route key = keySetIterator2.next();
				if(reverseRoutes.get(key)>reverseRoutes.get(rmax)){
					rmax = key;
				}
			}
			//System.out.println("Route :  " + rmax.getPickup().getX()+" "+rmax.getPickup().getY()+" to "+rmax.getDropoff().getX()+" "+rmax.getDropoff().getY() + " nb : " + tabRoutes.get(nb));
			sortedRoutes.put(rmax, reverseRoutes.get(rmax));
			reverseRoutes.remove(rmax);
		}
		
		Iterator<Route> keySetIterator3 = sortedRoutes.keySet().iterator();
		
		
		System.out.println("Start");
		while(keySetIterator3.hasNext()){
			
			 Route key = keySetIterator3.next();
			System.out.println("Route :  " + key.getPickup().getX()+" "+key.getPickup().getY()+" to "+key.getDropoff().getX()+" "+key.getDropoff().getY() + " nb : " + sortedRoutes.get(key));
		}
		
		System.out.println("END");
		
		
		long stop = System.currentTimeMillis();
		
		long pickup = currentTime.getTime() - (30*60*1000);
		Date pickupTime = new Date();
		pickupTime.setTime(pickup);
		String list = "";
		NumberFormat formatter = new DecimalFormat("00"); 
		
		writeLine((pickupTime.getYear()+1900) + "-" + formatter.format((pickupTime.getMonth()+1)) + "-" + formatter.format(pickupTime.getDate()) + " " + formatter.format(pickupTime.getHours()) + ":" + formatter.format(pickupTime.getMinutes()) + ":" + formatter.format(pickupTime.getSeconds()) + " , " + (currentTime.getYear()+1900) + "-" + formatter.format((currentTime.getMonth()+1)) + "-" + formatter.format(currentTime.getDate()) + " " + formatter.format(currentTime.getHours()) + ":" + formatter.format(currentTime.getMinutes()) + ":" + formatter.format(currentTime.getSeconds()));
		keySetIterator3 = sortedRoutes.keySet().iterator();
		for(int i=0; i<10; i++)
		{
			if(keySetIterator3.hasNext())
			{
				Route key = keySetIterator3.next();
				list = list.concat(key.getPickup().getX() + " " + key.getPickup().getY() + " , " + key.getDropoff().getX() + " " + key.getDropoff().getY() + " , ");
			}
			else
			{
				list = list.concat("NULL , ");
			}
		}
		list = list.substring(0, list.length()-2);
		writeLine(list);
		writeLine("Delay : " + (stop-start) + "\n");
		
		
		
	
	}
	
	
	
	
	
	
	
	

}