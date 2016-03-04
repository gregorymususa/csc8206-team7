package copy;

/**
 * Implements the Location, as part of the Railway Interlocking system
 * @author Team 7
 *
 */
public class Location{
	private String name;
	
	public Location(String name){
		this.name = name;
		if(name.isEmpty()) {
			throw new IllegalArgumentException("Invalid location name.");
		}
	}
	
	public String getName(){
		String n = this.name;
		return n;
	}

	public String toString(){
		String n = this.name;
		return n;
	}
	
	public boolean equals(Location l){
		return l.getName().equals(this.getName());
	}
	//Possible future extension: could modify to say whether or not a station can be accessed at this time

}
