package fr.tse.fi2.hpp.labs.queries.impl.debs.query2;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.GridPoint;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class BetterImplement2 extends AbstractQueryProcessor{
	
	//Solution amélioré de la query 2
	
	private Date currentTime = new Date();
	private ArrayList<DebsRecord> tabrecPU30 =  new ArrayList<DebsRecord>();
	private ArrayList<DebsRecord> tabrecDO30 =  new ArrayList<DebsRecord>();

	public BetterImplement2(QueryProcessorMeasure measure) {
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

		currentTime.setTime(record.getDropoff_datetime());
		//cette fois le tableau de pickup de 15 min est instancier dans la méthode et plus en global pour optimiser la mémoire
		ArrayList<DebsRecord> tabrecPU152 =  new ArrayList<DebsRecord>();
		
		ArrayList<Area> tabArea = new ArrayList<Area>();
		
		//On récupère tout les records dont les heures de pickup de moins de 30min 
		tabrecPU30.add(record);
		for(int i=0;i<tabrecPU30.size();i++){
			if ((currentTime.getTime() - tabrecPU30.get(i).getPickup_datetime()) >1800000){
				tabrecPU30.remove(i);
				
			}
			//si le Pickup est également moins de 15mins 
			else if((currentTime.getTime() - tabrecPU30.get(i).getPickup_datetime()) <=900000){
				tabrecPU152.add(tabrecPU30.get(i));
			}
		}
		
		
		
		for(int i=0;i<tabrecPU152.size();i++){
			Area a = new Area();
			a.setCell(convert(tabrecPU152.get(i).getPickup_longitude() , tabrecPU152.get(i).getPickup_latitude()));
			boolean find = false;
			for(int j=0;j<tabArea.size();j++){
				if(a.getCell().equals(tabArea.get(j).getCell())){
					tabArea.get(j).setTotalTrip(tabArea.get(j).getTotalTrip()+1);
					tabArea.get(j).setTotalFare(tabArea.get(j).getTotalFare()+tabrecPU152.get(i).getFare_amount());
					tabArea.get(j).setTotalTip(tabArea.get(j).getTotalTip()+tabrecPU152.get(i).getTip_amount());
					//tabArea.get(j).CalculmedianProfit();
					tabArea.get(j).calculprofitability();
					find = true;
				}
			}
			if(!find){
				a.setTotalTrip(1);
				a.setTaxiEmpty(0);
				a.setTotalFare(tabrecPU152.get(i).getFare_amount());
				a.setTotalTip(tabrecPU152.get(i).getTip_amount());
				//a.CalculmedianProfit();
				a.calculprofitability();
				tabArea.add(a);
			}
			
			
		}
		
		
		//Calcul du nombre de taxi vide
	
		
		
		for(int i=0;i<tabrecDO30.size();i++){
			if ((currentTime.getTime() - tabrecDO30.get(i).getDropoff_datetime()) >1800000){
				tabrecDO30.remove(i);
				
			}
		}
		tabrecDO30.add(record);
		
		for(int i =0;i<tabrecDO30.size();i++){
			boolean find = false;
			for(int j=0;j<tabrecPU30.size();j++){
				if(tabrecDO30.get(i).getHack_license().equals(tabrecPU30.get(j).getHack_license())){
					if(tabrecPU30.get(j).getPickup_datetime()-tabrecDO30.get(i).getDropoff_datetime()>0){
						find = true;
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
			tabArea.get(i).CalculmedianProfit();
			tabArea.get(i).calculprofitability();
		}
		
	
		
		//On utilise la methode Collections.sort pour classer les Area
		Collections.sort(tabArea, new Comparator<Area>() {
		    @Override
		    public int compare(Area A1, Area A2) {
		    	return A1.compareTo(A2);
		    }
		});
		
		
	
		
		long stop = System.currentTimeMillis();
		
		long pickup = currentTime.getTime() - (15*60*1000);
		Date pickupTime = new Date();
		pickupTime.setTime(pickup);
		String list = "";
		NumberFormat formatter = new DecimalFormat("00"); 
		
		
		writeLine((pickupTime.getYear()+1900) + "-" + formatter.format((pickupTime.getMonth()+1)) + "-" + formatter.format(pickupTime.getDate()) + " " + formatter.format(pickupTime.getHours()) + ":" + formatter.format(pickupTime.getMinutes()) + ":" + formatter.format(pickupTime.getSeconds()) + " , " + (currentTime.getYear()+1900) + "-" + formatter.format((currentTime.getMonth()+1)) + "-" + formatter.format(currentTime.getDate()) + " " + formatter.format(currentTime.getHours()) + ":" + formatter.format(currentTime.getMinutes()) + ":" + formatter.format(currentTime.getSeconds()));
		for(int i=0; i<10; i++)
		{
			if(i < tabArea.size())
			{
				list = list.concat(String.valueOf(tabArea.get(i).getCell().getX())).concat(" ").concat(String.valueOf(tabArea.get(i).getCell().getY())).concat(" , ").concat(String.valueOf(tabArea.get(i).getTaxiEmpty())).concat(" , ").concat(String.valueOf(tabArea.get(i).getMedianProfit())).concat(" , ").concat(String.valueOf(tabArea.get(i).getProfitability())).concat(" , ");
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
