
/**
 * Models train object moving through a given Network
 * Train interfaces with the ControlRoom to figure out where it is
 *   travelling next
 * Train itself does not hold any of its travel information aside 
 *   from its station of origin and its intended final destination
 * 
 * @author team7
 *
 */
public class Train {
	
	String name;
	boolean moving = false;
	Location origin;
	Location destination;
	Section next;			//Neighbour the train will move to next
//	Block currentBlock; 	//where the train is currently located
//	Block nextBlock; 		//where the train is heading next
	
	
	public Train(String name, Location origin){
		if(name.isEmpty()) 
			throw new IllegalArgumentException("Invalid train name: empty string.");
		if(!name.startsWith("t")) 
			throw new IllegalArgumentException("Invalid train name: must follow convention \"t#\".");
		
		if(origin.equals(null))
			throw new IllegalArgumentException("Invalid starting station.");
		
		this.name = name;
		this.origin = origin;

	}
	
	
	/**
	 * Sets train journey details
	 * @param origin
	 * @param destination
	 */
	public void setJourney(Location origin, Location destination){
		if(origin.equals(null)|| destination.equals(null))
			throw new IllegalArgumentException("Missing location setting.");
		if(origin.equals(destination))
			throw new IllegalArgumentException("Cannot set same start and end location");

		this.origin = origin;
		this.destination = destination;
		
		System.out.println("Train "+ this.name + " is travelling from " + origin + " to " + destination + ".");
	}
	
	
	/**
	 * checks for a STOP or CLEAR from the signal 
	 * @return
	 */
	public boolean checkSignal(Signal s){
		if(s.getStatus().equals(Signal.CLEAR)){
			moving = true;
			s.setStatus(Signal.STOP); 			//Sets signal to stop to prevent following collision
//			s.getPair().setStatus(Signal.STOP); //TODO: write a getPair() method to get the signal's paired UP or DOWN flow signal. Prevents head-on collision
		}
		return false;
	}

	/**
	 * queries ControlRoom to set the next destination for the Train's current journey
	 * @return the name of the next destination
	 */
	String setNext(){
		return null;
	}
	/**
	 * 
	 * @return
	 */
	public String getName(){
		String n = this.name;
		return n;
	}

}
