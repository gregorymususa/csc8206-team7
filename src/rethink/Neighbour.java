package rethink;


/**
 * Parent class for Signal, and Location
 * @author Team 7
 *
 */
public class Neighbour {
	
	private String name;
	
	public Neighbour(String name) {
		this.name = name;
	}
	
	/**
	 * Returns name
	 * @return name
	 */
	public final String getName() {
		String n = this.name;
		return n;
	}
	
	/**
	 * Override toString method
	 */
	public String toString() {
		String n = this.name;
		return n;
	}
}
