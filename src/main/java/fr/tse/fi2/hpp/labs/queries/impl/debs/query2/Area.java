package fr.tse.fi2.hpp.labs.queries.impl.debs.query2;

import fr.tse.fi2.hpp.labs.beans.GridPoint;

public class Area {
	
	private GridPoint cell;
	
	private int taxiEmpty;
	
	private int totalTrip;
	
	private float totalFare;
	

	private float medianProfit;
	
	private float profitability;
	
	private float totalTip;
	



	public void CalculmedianProfit(){
		medianProfit= (totalFare + totalTip) /totalTrip;
	}
	
	
	public void calculprofitability(){
		if(taxiEmpty>0){
			profitability = medianProfit/taxiEmpty;
		}
		else{
			profitability=-1;
		}
	}
	
	
	
	
	public float getTotalTip() {
		return totalTip;
	}


	public void setTotalTip(float totalTip) {
		this.totalTip = totalTip;
	}
	
	public float getTotalFare() {
		return totalFare;
	}

	public void setTotalFare(float totalFare) {
		this.totalFare = totalFare;
	}
	public GridPoint getCell() {
		return cell;
	}

	public void setCell(GridPoint cell) {
		this.cell = cell;
	}

	public int getTaxiEmpty() {
		return taxiEmpty;
	}

	public void setTaxiEmpty(int taxiEmpty) {
		this.taxiEmpty = taxiEmpty;
	}

	public int getTotalTrip() {
		return totalTrip;
	}

	public void setTotalTrip(int totalTrip) {
		this.totalTrip = totalTrip;
	}

	public float getMedianProfit() {
		return medianProfit;
	}

	public void setMedianProfit(float medianProfit) {
		this.medianProfit = medianProfit;
	}

	public float getProfitability() {
		return profitability;
	}

	public void setProfitability(float profitability) {
		this.profitability = profitability;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cell == null) ? 0 : cell.hashCode());
		result = prime * result + Float.floatToIntBits(medianProfit);
		result = prime * result + Float.floatToIntBits(profitability);
		result = prime * result + taxiEmpty;
		result = prime * result + Float.floatToIntBits(totalFare);
		result = prime * result + Float.floatToIntBits(totalTip);
		result = prime * result + totalTrip;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Area other = (Area) obj;
		if (cell == null) {
			if (other.cell != null)
				return false;
		} else if (!cell.equals(other.cell))
			return false;
		if (Float.floatToIntBits(medianProfit) != Float
				.floatToIntBits(other.medianProfit))
			return false;
		if (Float.floatToIntBits(profitability) != Float
				.floatToIntBits(other.profitability))
			return false;
		if (taxiEmpty != other.taxiEmpty)
			return false;
		if (Float.floatToIntBits(totalFare) != Float
				.floatToIntBits(other.totalFare))
			return false;
		if (Float.floatToIntBits(totalTip) != Float
				.floatToIntBits(other.totalTip))
			return false;
		if (totalTrip != other.totalTrip)
			return false;
		return true;
	}
	
	

	

}
