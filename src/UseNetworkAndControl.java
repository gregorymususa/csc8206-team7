

public class UseNetworkAndControl {

	public static void main(String[] args) {
		ControlRoom controlRoom = new ControlRoom("./database/Network_ABC.csv");
		controlRoom.displayNetwork();
		controlRoom.buildRoute();
		controlRoom.buildJourney();
	}
	
}
