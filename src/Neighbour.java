
/**
 * Parent class for Signal, and Location
 * @author Team 7
 *
 */
public abstract class Neighbour {
	
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
}
