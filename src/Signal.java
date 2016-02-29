

/**
 * Implements the signal, as part of the Railway Interlocking system
 * @author Team 7
 *
 */
public class Signal extends Neighbour{
	
	public static final String UP = "up";
	public static final String DOWN = "down";
	public static final String CLEAR = "clear";
	public static final String STOP = "stop";
	
	private String status = STOP;
	private String viewDirection;
	
	/**
	 * 
	 * @param name name of signal
	 * @param viewDirection set direction, UP or DOWN
	 */
	public Signal(String name, String viewDirection){
		super(name);
		
		if(!(name.startsWith("s"))) {
			throw new IllegalArgumentException("Invalid signal name: " + name);
		}

		if(viewDirection.equals(UP)||viewDirection.equals(DOWN))
			this.viewDirection = viewDirection;
		else 
			throw new IllegalArgumentException("Invalid signal view direction: " + viewDirection);
	}
	
	/**
	 * Tells a train if it can GO (CLEAR), or HALT (STOP)
	 * @return CLEAR or STOP
	 */
	public String getStatus(){
		String s = status;
		return s;
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
}
