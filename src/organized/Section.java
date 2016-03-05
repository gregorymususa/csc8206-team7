package organized;



/**
 * A block, which is just a section of the track
 * @author Team 7
 *
 */
public class Section extends Block {
	

	public static final String NONE = "none";

//	//	Temporarily commented out old neighbour structure.
//	private String upNeigh = NONE;
//	private String downNeigh = NONE;
//	/**
//	 * 
//	 * @param name
//	 * @param upNeigh Up Neighbour (Signal, or Location)
//	 * @param downNeigh Down Neighbour (Signal, or Location)
//	 */
//	public Section(String name, String upNeigh, String downNeigh) {
//		super(name);
//		this.upNeigh = upNeigh;
//		this.downNeigh = downNeigh;
//	}
//	
//	public String getDownNeigh() {
//		String s = downNeigh;
//		return s;
//	}
//	
//	public String getUpNeigh() {
//		String s = upNeigh;
//		return s;
//	}
//	
	public Section(String name){
		super(name);
	}

	public boolean equals(Section s){
		//Sections are considered to be identical if their names match
		return s.getName().equals(this.getName());
	}

	
}
