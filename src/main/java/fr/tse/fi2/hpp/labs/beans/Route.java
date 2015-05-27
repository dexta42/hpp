package fr.tse.fi2.hpp.labs.beans;

/**
 * A route consists in two {@link GridPoint}, the pickup and the dropoff ones.
 * Each of this {@link GridPoint} are within a grid. The method
 * {@link #isValid(int)} can be used to check the validity of the route in the
 * grid system.
 * 
 * @author Julien
 * 
 */
public class Route {
	/**
	 * Pickup location (where the route starts)
	 */
	GridPoint pickup;
	/**
	 * Dropoff location (where the route ends)
	 */
	GridPoint dropoff;

	public Route(GridPoint pickup, GridPoint dropoff) {
		super();
		this.pickup = pickup;
		this.dropoff = dropoff;
	}

	public Route() {
		super();
	}

	/**
	 * 
	 * @return Pickup location (where the route starts)
	 */
	public GridPoint getPickup() {
		return pickup;
	}

	/**
	 * 
	 * @param pickup
	 *            location (where the route starts)
	 */
	public void setPickup(GridPoint pickup) {
		this.pickup = pickup;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dropoff == null) ? 0 : dropoff.hashCode());
		result = prime * result + ((pickup == null) ? 0 : pickup.hashCode());
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
		Route other = (Route) obj;
		if (dropoff == null) {
			if (other.dropoff != null)
				return false;
		} else if (!dropoff.equals(other.dropoff))
			return false;
		if (pickup == null) {
			if (other.pickup != null)
				return false;
		} else if (!pickup.equals(other.pickup))
			return false;
		return true;
	}

	/**
	 * 
	 * @return Dropoff location (where the route ends)
	 */
	public GridPoint getDropoff() {
		return dropoff;
	}

	/**
	 * 
	 * @param dropoff
	 *            location (where the route ends)
	 */
	public void setDropoff(GridPoint dropoff) {
		this.dropoff = dropoff;
	}

	/**
	 * 
	 * @param gridsize
	 * @return <code>true</code> if the pickup and dropoff location lie within
	 *         the grid
	 */
	public boolean isValid(int gridsize) {
		if (!valid(pickup.getX(), gridsize))
			return false;
		if (!valid(pickup.getX(), gridsize))
			return false;
		if (!valid(dropoff.getX(), gridsize))
			return false;
		if (!valid(dropoff.getY(), gridsize))
			return false;
		return true;
	}

	private boolean valid(int x, int gridsize) {
		return x > 0 && x <= gridsize;
	}

}
