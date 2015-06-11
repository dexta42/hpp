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
	
	//solution de la query 1
	
	public static String lastLine;
	private int nb = 0;
	private float sum = 0;
	private Date currentTime = new Date();
	private ArrayList<DebsRecord> tabrec =  new ArrayList<DebsRecord>();//a faire en linkedList

	public NaiveImplement(QueryProcessorMeasure measure) {
		
		super(measure);
	}
	
	public static String getLastLine() {
		return lastLine;
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
	
	
	
	
	
	@Override
	protected void process(DebsRecord record) {
		
		long start = System.currentTimeMillis();
		//On récupère l'heure de dropoff du derniers record qui sera l'heure actuelle
		currentTime.setTime(record.getDropoff_datetime());
		
		
		LinkedHashMap<Route,Integer> tabRoutes = new LinkedHashMap<Route,Integer>();
		
		//On parcours le tableau de dropoff de moins de 30min
		for(int i=0;i<tabrec.size();i++){
			if ((currentTime.getTime() - tabrec.get(i).getDropoff_datetime()) >1800000){
				tabrec.remove(i);
				
			}
		}
		tabrec.add(record);
		
		//On remplit le tableau de Route de moins de 30min
		for(int i=0;i<tabrec.size();i++){
			Route r = convertRecordToRoute( tabrec.get(i));
			
			Iterator<Route> keySetIterator = tabRoutes.keySet().iterator();
			boolean find = false;
			//Pour chaque record on parcour le tableau de Routes
			while(keySetIterator.hasNext()){
				  Route key = keySetIterator.next();
				  //si la route est déjà présente, on incrémente le nombre de route (qui correspond à la value de la Hasmpa
				  if(r.equals(key)){
					  Integer nb=tabRoutes.get(key);
					  tabRoutes.put(key,nb+1);
					  find = true;
				  }
				}
			
			//Si on a pas trouvé de route correspondant on ajoute la nouvelle Route au tableau
			if(!find){
				tabRoutes.put(r, 1);
			}
			
		
		}
		
		//On reclasse le tableau de Route du plus recent au plus ancien
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
		}
		
		
		//On classe le tableau de Route par le nombre de trajet effectué
		Iterator<Route> keySetIterator2 = reverseRoutes.keySet().iterator();
		LinkedHashMap <Route,Integer> sortedRoutes = new LinkedHashMap <Route,Integer>();
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
			
			sortedRoutes.put(rmax, reverseRoutes.get(rmax));
			reverseRoutes.remove(rmax);
		}
		
		Iterator<Route> keySetIterator3 = sortedRoutes.keySet().iterator();
		
		
		//Mise en forme du résultat de la query
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
				list = list.concat(key.getPickup().getX() + "." + key.getPickup().getY() + " , " + key.getDropoff().getX() + "." + key.getDropoff().getY() + " , ");
			}
			else
			{
				list = list.concat("NULL , ");
			}
		}
		list = list.substring(0, list.length()-2);
		writeLine(list);
		writeLine("Delay : " + (stop-start) + " ms\n");
		
		lastLine = list.replace(" ", "");
		
	
	}
	
	
	
	
	
	
	
	

}