

/**
 * TODO Do we want to rename this to SectionBlock? To make more sense, at first glance? (What about PointBlock?)
 * @author Team 7
 *
 */
public class Section extends Block {

	private Neighbour upNeigh;
	private Neighbour downNeigh;
	
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
