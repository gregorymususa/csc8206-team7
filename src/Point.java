



/**
 * Point models a section of track that forks off from a stem line
 * Point can set track to be in plus or minus setting
 *  
 * @author Team 7
 *
 */
public class Point extends Block {
	public static final String PLUS = "plus";
	public static final String MINUS = "minus";
	
	private Section stemline;
	private Section plusline;
	private Section minusline;
	private String track = PLUS;
	
	public Point(String name, Section stem_line, Section plus_line, Section minus_line){
		super(name);
		
		//check signal follows naming convention
		if(!(name.startsWith("p")))
			throw new IllegalArgumentException("Invalid point name: " + name);
		
		//Checks to make sure none of the signals are null.
		if(stem_line.getName().equals(null)||minus_line.getName().equals(null)||plus_line.getName().equals(null))
			throw new IllegalArgumentException("Duplicate defining signal.");

		//Checks to make sure none of the signals are the same.
		if(stem_line.equals(plus_line)||stem_line.equals(minus_line)||plus_line.equals(minus_line))
			throw new IllegalArgumentException("Duplicate defining signal.");
		
		this.stemline = stem_line;
		this.minusline = minus_line;
		this.plusline = plus_line;
		
//		System.out.println(stemline);
	}
	
	/**
	 * 
	 * @param s PLUS or MINUS track setting
	 * @return TRUE if point is successfully changed
	 */
	public boolean setTrack(String s){
		if(!s.equals(PLUS)||!s.equals(MINUS))
			throw new IllegalArgumentException("Invalid point setting: not PLUS or MINUS");
		track = s;
		return true;
	}
	
	/**
	 * 
	 * @return current track setting, PLUS or MINUS
	 */
	public String getTrack(){
		String t = track;
		return t;
	}

	/**
	 * 
	 * @return block controlling the stem line
	 */
	public Section getStemLine(){
//		return stemline;
		return new Section(stemline.getName());
//		return new Section(stemline.getName(), stemline.getUpNeigh(), stemline.getDownNeigh());
	}
	
	/**
	 * 
	 * @return block controlling the plus line
	 */	
	public Section getPlusLine(){
//		return plusline;
		return new Section(plusline.getName());
//		return new Section(plusline.getName(), plusline.getUpNeigh(), plusline.getDownNeigh());
	}
	
	/**
	 * 
	 * @return block controlling the minus line
	 */
	public Section getMinusLine(){
//		return minusline;
		return new Section(minusline.getName());
//		return new Section(minusline.getName(), minusline.getUpNeigh(), minusline.getDownNeigh());
	}
	/**
	 * Points are considered to be identical if their names match and their stem, plus, and minus lines match
	 * @param p
	 * @return
	 */
	public boolean equals(Point p){
		if(p.getStemLine().equals(this.stemline))
			if(p.getPlusLine().equals(this.plusline))
				if(p.getMinusLine().equals(this.minusline))
					return p.getName().equals(this.getName());

		//if any of these fields is unequal, return false
		return false;
	}
}