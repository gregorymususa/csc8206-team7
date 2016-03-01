
/**
 * Implements the Location, as part of the Railway Interlocking system
 * @author Team 7
 *
 */
public class Location extends Neighbour{
	
	public Location(String name){
		super(name);
		if(name.isEmpty()) {
			throw new IllegalArgumentException("Invalid location name.");
		}
	}

	//Possible future extension: could modify to say whether or not a station can be accessed at this time

}
