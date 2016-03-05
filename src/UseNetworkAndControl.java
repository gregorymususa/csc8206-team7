

public class UseNetworkAndControl {

	public static void main(String[] args) {
		ControlRoom controlRoom = new ControlRoom("./database/Network_old.csv");
		controlRoom.displayNetwork();
		controlRoom.buildRoute();
		controlRoom.buildJourney();
	}
	
}
