

/**
 * A block, which is just a section of the track
 * @author Team 7
 *
 */
public class Section extends Block {

	private Neighbour upNeigh;
	private Neighbour downNeigh;
	
	/**
	 * 
	 * @param name
	 * @param upNeigh Up Neighbour (Signal, or Location)
	 * @param downNeigh Down Neighbour (Signal, or Location)
	 */
	public Section(String name, Neighbour upNeigh, Neighbour downNeigh) {
		super(name);
		this.upNeigh = upNeigh;
		this.downNeigh = downNeigh;
	}
	
	public Neighbour getDownNeigh() {
		return downNeigh;
	}
	
	public Neighbour getUpNeigh() {
		return upNeigh;
	}
	
}
