package fr.tse.fi2.hpp.labs.queries.impl.debs.query2;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.GridPoint;
import fr.tse.fi2.hpp.labs.beans.Route;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class NaiveImplement2 extends AbstractQueryProcessor {
	
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
		
		currentTime.setTime(record.getDropoff_datetime());
		
		//Récupération des zone dans les 15 dernière minutes et calcul du nombre de trajet
		ArrayList<Area> tabArea = new ArrayList<Area>();
		
		tabrecPU15.add(record);
		for(int i=0;i<tabrecPU15.size();i++){
			if ((currentTime.getTime() - tabrecPU15.get(i).getPickup_datetime()) >900000){
				tabrecPU15.remove(i);
				
			}
		}
		
		
		
		for(int i=0;i<tabrecPU15.size();i++){
			Area a = new Area();
			a.setCell(convert(tabrecPU15.get(i).getPickup_longitude() , tabrecPU15.get(i).getPickup_latitude()));
			boolean find = false;
			for(int j=0;j<tabArea.size();j++){
				if(a.getCell().equals(tabArea.get(j).getCell())){
					tabArea.get(j).setTotalTrip(tabArea.get(j).getTotalTrip()+1);
					tabArea.get(j).setTotalFare(tabArea.get(j).getTotalFare()+tabrecPU15.get(i).getFare_amount());
					tabArea.get(j).setTotalTip(tabArea.get(j).getTotalTip()+tabrecPU15.get(i).getTip_amount());
					tabArea.get(j).CalculmedianProfit();
					tabArea.get(j).calculprofitability();
					find = true;
				}
			}
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
		
		
		//Calcul du nombre de taxi vide
		tabrecPU30.add(record);
		for(int i=0;i<tabrecPU30.size();i++){
			if ((currentTime.getTime() - tabrecPU30.get(i).getPickup_datetime()) >1800000){
				tabrecPU30.remove(i);
				
			}
		}
		
		
		for(int i=0;i<tabrecDO30.size();i++){
			if ((currentTime.getTime() - tabrecDO30.get(i).getPickup_datetime()) >1800000){
				tabrecDO30.remove(i);
				
			}
		}
		tabrecDO30.add(record);
		
		for(int i =0;i<tabrecDO30.size();i++){
			boolean find = false;
			int cpt=0;
			for(int j=0;j<tabrecPU30.size();j++){
				if(tabrecDO30.get(i).getHack_license().equals(tabrecPU30.get(j).getHack_license())){
					if(tabrecPU30.get(j).getPickup_datetime()-tabrecDO30.get(i).getDropoff_datetime()>0){
						find = true;
						cpt++;
					}
				}
			}
			
			if(!find){
				GridPoint dropArea = convert(tabrecDO30.get(i).getPickup_longitude() , tabrecDO30.get(i).getPickup_latitude());
				for(int k =0;k<tabArea.size();k++){
					if(dropArea.equals(tabArea.get(k).getCell())){
						tabArea.get(k).setTaxiEmpty(tabArea.get(k).getTaxiEmpty()+1);
					}
				}
			}
		}
		
		
		for(int i = 0;i<tabArea.size();i++){
			tabArea.get(i).calculprofitability();
		}
		
		
		System.out.println(tabrecPU30.size());
		System.out.println("Strart");
		for(int i=0;i<tabArea.size();i++){
			System.out.println("cell : " + tabArea.get(i).getCell().getX() + " " + tabArea.get(i).getCell().getY() + " Total trip : " + tabArea.get(i).getTotalTrip() +
								" Taxi Empty : " + tabArea.get(i).getTaxiEmpty() + " Median profit : " + tabArea.get(i).getMedianProfit() + " Profitability : "  + tabArea.get(i).getProfitability());
		}
		System.out.println("END");
		
		
	}

}
