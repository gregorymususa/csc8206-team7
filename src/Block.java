/**
 * Implements the Block, as part of the Railway Interlocking system
 * @author Team 7
 *
 */
public class Block {

	private String name;
	private Neighbour upNeigh;
	private Neighbour downNeigh;
	private Location location;
	
	
	public Block(String name) {
		this.name = name;
	}

	public Block(Neighbour upNeigh, Neighbour downNeigh) {
		this.upNeigh = upNeigh;
		this.downNeigh = downNeigh;
	}
	
	public Block(String name,Neighbour upNeigh, Neighbour downNeigh) {
		this.name = name;
		this.upNeigh = upNeigh;
		this.downNeigh = downNeigh;
	}
	
//	/**
//	 * tells if it is clear
//	 * @return isClear
//	 */
//	public boolean getIsClear(){
//		if(upNeigh.getStatus() && downNeigh.getStatus())
//			return true;
//		return false;	
//	}

	public Neighbour getDownNeigh() {
		return downNeigh;
	}
	
	public void setDownNeigh(Neighbour downNeigh) {
		this.downNeigh = downNeigh;
	}

	public Neighbour getUpNeigh() {
		return upNeigh;
	}
	
	public void setUpNeigh(Neighbour upNeigh) {
		this.upNeigh = upNeigh;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}


}
