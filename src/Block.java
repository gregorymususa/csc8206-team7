



/**
 * A non-instantiated parent class
 * 
 * This was created to:
 * - Add semantic sense to the systems structures
 * - To aid ControlRoom.buildRoute(....,path[]); an array which contains both block, and point
 *  
 * @author Team 7
 *
 */
public abstract class Block {

	private String name;
	
	public Block(String name) {
		this.name = name;
	}
	
	public String getName() {
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
}
