package debug;



/**
 * Implements the signal, as part of the Railway Interlocking system
 * @author Team 7
 *
 */
public class Signal{
	
	public static final String UP = "up";
	public static final String DOWN = "down";
	public static final String CLEAR = "clear";
	public static final String STOP = "stop";
	public static final String STEM = "stem";
	public static final String PLUS = "plus";
	public static final String MINUS= "minus";
	
	private String name;
	private String status = STOP;
	private String viewDirection;
	private Block protects;
	private String line = STEM;
	
	/**
	 * 
	 * @param name name of signal
	 * @param viewDirection set direction, UP or DOWN
	 */
	public Signal(String name, String viewDirection, Block protects){
		this.name = name;
		
		if(!(name.startsWith("s"))) {
			throw new IllegalArgumentException("Invalid signal name: " + name);
		}

		if(viewDirection.equals(UP)||viewDirection.equals(DOWN))
			this.viewDirection = viewDirection;
		else 
			throw new IllegalArgumentException("Invalid signal view direction: " + viewDirection);
	
		this.protects = protects;
	}
	
	public Signal(String name, String viewDirection, Block protects,String line){
		this.name = name;
		
		if(line.equals(PLUS)||line.equals(MINUS))
			this.line = line;
		else 
			throw new IllegalArgumentException("Invalid signal view direction: " + viewDirection);
		
		if(!(name.startsWith("s"))) {
			throw new IllegalArgumentException("Invalid signal name: " + name);
		}

		if(viewDirection.equals(UP)||viewDirection.equals(DOWN))
			this.viewDirection = viewDirection;
		else 
			throw new IllegalArgumentException("Invalid signal view direction: " + viewDirection);
	
		this.protects = protects;
	}
	
	/**
	 * Tells a train if it can GO (CLEAR), or HALT (STOP)
	 * @return CLEAR or STOP
	 */
	public String getStatus(){
		String s = status;
		return s;
	}
	
	public Block getProtects(){
		Block p = protects;
		return p;
	}

	/**
	 * Sets the signal as CLEAR or STOP
	 * @param s
	 * @return true if status is successfully changed
	 */
	public boolean setStatus(String s){
		if(!(s.equals(CLEAR)||s.equals(STOP)))
			return false;
		
		status = s;
		return true;
	}

	/**
	 * Returns if this is an UP or DOWN signal
	 * @return UP or DOWN
	 */
	public String getDirection() {
		String v = this.viewDirection;
		return v;
	}
	
	/**
	 * A signal is considered equal if it matches in Direction, Protects, and Name fields
	 * @param s
	 * @return
	 */
	public boolean equals(Signal s){
		if(s.getDirection().equals(this.getDirection()))
			if(s.getProtects().equals(this.getProtects()))
				return s.getName().equals(this.getName());
		return false;
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
	
	public String getLine(){
		String l = line;
		return l;
	}

}
