/**
 * Point models a section of track that forks off from a main line
 * Point can set track to be in plus or minus setting
 *  
 * @author Lisa
 *
 */
public class Point {
	public static final String PLUS = "plus";
	public static final String MINUS = "minus";
	
	private String name;
	private Signal mainline;
	private Signal plusline;
	private Signal minusline;
	private String track = PLUS;
	
	public Point(String name, Signal mainline, Signal plusline, Signal minusline){
		//check signal follows naming convention
		if(!(name.startsWith("p")))
			throw new IllegalArgumentException("Invalid point name: " + name);
		
		//Checks to make sure none of the signals are null.
		if(mainline.equals(null)||minusline.equals(null)||plusline.equals(null))
			throw new IllegalArgumentException("Duplicate defining signal.");

		//Checks to make sure none of the signals are the same.
		if(mainline.equals(plusline)||mainline.equals(minusline)||plusline.equals(minusline))
			throw new IllegalArgumentException("Duplicate defining signal.");
		
		this.name = name; 
		this.mainline = mainline;
		this.minusline = minusline;
		this.plusline = plusline;
	}

	/**
	 * 
	 * @return name of point
	 */
	public String getName(){
		String n = name;
		return n;
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
	 * @return name of the signal controlling the main line
	 */
	public String getMain(){
		return mainline.getName();
	}
	
	/**
	 * 
	 * @return name of the signal controlling the plus line
	 */	
	public String getPlus(){
		return plusline.getName();
	}
	
	/**
	 * 
	 * @return name of the signal controlling the minus line
	 */
	public String getMinus(){
		return minusline.getName();
	}

}