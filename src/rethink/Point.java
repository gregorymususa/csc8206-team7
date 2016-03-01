package rethink;

/**
 * Point models a section of track that forks off from a main line
 * Point can set track to be in plus or minus setting
 *  
 * @author Team 7
 *
 */
public class Point extends Block {
	public static final String PLUS = "plus";
	public static final String MINUS = "minus";
	
	private Section mainline;
	private Section plusline;
	private Section minusline;
	private String track = PLUS;
	
	public Point(String name, Section main_line, Section plus_line, Section minus_line){
		super(name);
		
		//check signal follows naming convention
		if(!(name.startsWith("p")))
			throw new IllegalArgumentException("Invalid point name: " + name);
		
		//Checks to make sure none of the signals are null.
		if(main_line.equals(null)||minus_line.equals(null)||plus_line.equals(null))
			throw new IllegalArgumentException("Duplicate defining signal.");

		//Checks to make sure none of the signals are the same.
		if(main_line.equals(plus_line)||main_line.equals(minus_line)||plus_line.equals(minus_line))
			throw new IllegalArgumentException("Duplicate defining signal.");
		
		this.mainline = mainline;
		this.minusline = minusline;
		this.plusline = plusline;
		
//		System.out.println(mainline);
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
	 * @return block controlling the main line
	 */
	public Section getMainLine(){
		return mainline;
	}
	
	/**
	 * 
	 * @return block controlling the plus line
	 */	
	public Section getPlusLine(){
		return plusline;
	}
	
	/**
	 * 
	 * @return block controlling the minus line
	 */
	public Section getMinusLine(){
		return minusline;
	}

}