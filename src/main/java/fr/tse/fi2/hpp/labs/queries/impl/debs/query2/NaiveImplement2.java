package fr.tse.fi2.hpp.labs.queries.impl.debs.query2;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.GridPoint;
import fr.tse.fi2.hpp.labs.beans.Route;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class NaiveImplement2 extends AbstractQueryProcessor {
	
	//1e solution basique de la query 2
	
	private Date currentTime = new Date();
	private ArrayList<DebsRecord> tabrecPU15 =  new ArrayList<DebsRecord>();
	private ArrayList<DebsRecord> tabrecPU30 =  new ArrayList<DebsRecord>();
	private ArrayList<DebsRecord> tabrecDO30 =  new ArrayList<DebsRecord>();

	public NaiveImplement2(QueryProcessorMeasure measure) {
		super(measure);
		// TODO Auto-generated constructor stub
	}
	
	
	
	private int cellX(float x) {

		// double x=0;
		double x_0 = -74.913585;
		double delta_x = 0.005986 / 2 ;

		// double cell_x;
		Double cell_x = 1 + Math.floor(((x - x_0) / delta_x) + 0.5);

		return cell_x.intValue();
	}

	private int cellY(double y) {

		double y_0 = 41.474937;
		double delta_y = 0.004491556 / 2;

		Double cell_y = 1 + Math.floor(((y_0 - y) / delta_y) + 0.5);

		return cell_y.intValue();

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
		
		//Récupération des zone dans les 15 dernière minutes et calcul du nombre de trajet
		ArrayList<Area> tabArea = new ArrayList<Area>();
		
		//On récupère tout les records dont les heures de pickup de moins de 15min 
		tabrecPU15.add(record);
		for(int i=0;i<tabrecPU15.size();i++){
			if ((currentTime.getTime() - tabrecPU15.get(i).getPickup_datetime()) >900000){
				tabrecPU15.remove(i);
				
			}
		}
		
		
		//On remplit le tableau d'area avec le tableau des pickup de moins de 15min
		for(int i=0;i<tabrecPU15.size();i++){
			Area a = new Area();
			//on récupère les coordonnées du record en cours
			a.setCell(convert(tabrecPU15.get(i).getPickup_longitude() , tabrecPU15.get(i).getPickup_latitude()));
			boolean find = false;
			for(int j=0;j<tabArea.size();j++){
				//si on a déjà une Area correspondante à ces coordonnées on incrémente les différente vaariable
				if(a.getCell().equals(tabArea.get(j).getCell())){
					tabArea.get(j).setTotalTrip(tabArea.get(j).getTotalTrip()+1);
					tabArea.get(j).setTotalFare(tabArea.get(j).getTotalFare()+tabrecPU15.get(i).getFare_amount());
					tabArea.get(j).setTotalTip(tabArea.get(j).getTotalTip()+tabrecPU15.get(i).getTip_amount());
					tabArea.get(j).CalculmedianProfit();
					tabArea.get(j).calculprofitability();
					find = true;
				}
			}
			//Si on a pas d'Area correspondante à ces coordonndées on ajoute une nouvelle Area
			if(!find){
				a.setTotalTrip(1);
				a.setTaxiEmpty(0);
				a.setTotalFare(tabrecPU15.get(i).getFare_amount());
				a.setTotalTip(tabrecPU15.get(i).getTip_amount());
				a.CalculmedianProfit();
				a.calculprofitability();
				tabArea.add(a);
			}
			
			
		}
		
		
		//On récupère tout les records dont les heures de pickup de moins de 30min 
		tabrecPU30.add(record);
		for(int i=0;i<tabrecPU30.size();i++){
			if ((currentTime.getTime() - tabrecPU30.get(i).getPickup_datetime()) >1800000){
				tabrecPU30.remove(i);
				
			}
		}
		
		//On récupère tout les records dont les heures de dropoff de moins de 30min 
		for(int i=0;i<tabrecDO30.size();i++){
			if ((currentTime.getTime() - tabrecDO30.get(i).getDropoff_datetime()) >1800000){
				tabrecDO30.remove(i);
				
			}
		}
		tabrecDO30.add(record);
		
		//On parcours le tableau de dropoff de moins de 30min
		for(int i =0;i<tabrecDO30.size();i++){
			boolean find = false;
			//Pour chaque dropoff on cherche un pickup qui a la même license de taxi
			for(int j=0;j<tabrecPU30.size();j++){
				//on verifie que le pickup est posterieur au dropoff
				if(tabrecDO30.get(i).getHack_license().equals(tabrecPU30.get(j).getHack_license())){
					if(tabrecPU30.get(j).getPickup_datetime()-tabrecDO30.get(i).getDropoff_datetime()>0){
						find = true;
					}
				}
			}
			//Si on a pas trouvé de pickup posterieur au dropoff alors le taxi est vide, on incrémente le nombre de taxi vide à la zone correspondante
			if(!find){
				GridPoint dropArea = convert(tabrecDO30.get(i).getPickup_longitude() , tabrecDO30.get(i).getPickup_latitude());
				for(int k =0;k<tabArea.size();k++){
					if(dropArea.equals(tabArea.get(k).getCell())){
						tabArea.get(k).setTaxiEmpty(tabArea.get(k).getTaxiEmpty()+1);
					}
				}
			}
		}
		
		
		//on calcul la profitabilité pour tout les Area
		for(int i = 0;i<tabArea.size();i++){
			tabArea.get(i).calculprofitability();
		}
		
		//on classe les Areas par ordre décroissant de prfitabilité
		ArrayList<Area> sortedtabArea = new ArrayList<Area>();
		while(!tabArea.isEmpty()){
			
			Area Amax= tabArea.get(0);
			for(int i=1;i<tabArea.size();i++){
				if(tabArea.get(i).getProfitability()>Amax.getProfitability()){
					Amax=tabArea.get(i);
				}
			}
			sortedtabArea.add(Amax);
				tabArea.remove(Amax);
		}
		
		
		//Mise en forme du résultat de la query
		long stop = System.currentTimeMillis();
		
		long pickup = currentTime.getTime() - (15*60*1000);
		Date pickupTime = new Date();
		pickupTime.setTime(pickup);
		String list = "";
		NumberFormat formatter = new DecimalFormat("00"); 
		
		
		writeLine((pickupTime.getYear()+1900) + "-" + formatter.format((pickupTime.getMonth()+1)) + "-" + formatter.format(pickupTime.getDate()) + " " + formatter.format(pickupTime.getHours()) + ":" + formatter.format(pickupTime.getMinutes()) + ":" + formatter.format(pickupTime.getSeconds()) + " , " + (currentTime.getYear()+1900) + "-" + formatter.format((currentTime.getMonth()+1)) + "-" + formatter.format(currentTime.getDate()) + " " + formatter.format(currentTime.getHours()) + ":" + formatter.format(currentTime.getMinutes()) + ":" + formatter.format(currentTime.getSeconds()));
		for(int i=0; i<10; i++)
		{
			if(i < sortedtabArea.size())
			{
				list = list.concat(String.valueOf(sortedtabArea.get(i).getCell().getX())).concat(" ").concat(String.valueOf(sortedtabArea.get(i).getCell().getY())).concat(" , ").concat(String.valueOf(sortedtabArea.get(i).getTaxiEmpty())).concat(" , ").concat(String.valueOf(sortedtabArea.get(i).getMedianProfit())).concat(" , ").concat(String.valueOf(sortedtabArea.get(i).getProfitability())).concat(" , ");
			}
			else
			{
				list = list.concat("NULL , ");
			}
		}
		list = list.substring(0, list.length()-2);
		writeLine(list);
		writeLine("Delay : " + (stop-start) + " ms\n");
	}

}
