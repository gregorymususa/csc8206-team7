

public class UseNetworkAndControl {

	public static void main(String[] args) {
		ControlRoom controlRoom = new ControlRoom("./database/Network_old.csv");
		controlRoom.displayNetwork();
		controlRoom.buildRoute();
		controlRoom.buildJourney();
		
	}
	
////	public static void main(String[] args) throws IOException {
////
////		ControlRoom cr = new ControlRoom();
//////		boolean building = true;
//////		boolean reading = true;
////		Location a = new Location("A");
////		Location b = new Location("B");
////		Location c = new Location("C");
////		System.out.println("Building route table...");
//////		while(reading){
////			cr.buildRoute(null, null, null, null, null, null);
//////		}
////		
//////		System.out.println("Would you like to add a journey? (y/n)");
////		System.out.println("Building journey table");
//////		while(building){
////			cr.buildJourney("J1",new String[]{"r1","r2","r3"},a,b);
////			cr.buildJourney("J2",new String[]{"r4","r5","r6"},b,a);
////			cr.buildJourney("J3",new String[]{"r1","r2","r4"},a,c);
////			cr.buildJourney("J4",new String[]{"r3","r4","r5"},b,c);
//////		}
////		
////		cr.runNetwork();
////
////	}
//
//	
////	private static void testBuildRoute(ControlRoom controlRoom){
////		//**********************//
////		// Testing Control Room //
////		//**********************//
////		
////		Section b1 = new Section("b1");
////		Section b2 = new Section("b2");
////		Section b3 = new Section("b3");
////		Section b4 = new Section("b4");
////		Section b5 = new Section("b5");
////		Section b6 = new Section("b6");
////		Section b7 = new Section("b7");
////		Section b8 = new Section("b8");
////		
////		Point p1 = new Point("p1", b1, b2, b3);
////		Point p2 = new Point("p2", b1, b2, b3);
////		Point[] points = {p1,p2};
////		
////		Signal s1 = new Signal("s1",Signal.UP, b2);
////		Signal s2 = new Signal("s2",Signal.DOWN, b1);
////		Signal s3 = new Signal("s3",Signal.DOWN, p1);
////		Signal s4 = new Signal("s4",Signal.UP, p2);
////		Signal s5 = new Signal("s5",Signal.DOWN, p1);
////		Signal s6 = new Signal("s6",Signal.UP, p2);
////		Signal s7 = new Signal("s7",Signal.UP, b6);
////		Signal s8 = new Signal("s8",Signal.DOWN, b5);
////		Signal[] signals = {s1,s2,s3,s4,s5,s6,s7,s8};
////				
////
////		Block[] path = {b1, b2, p1, b4};
////		
////		System.out.println("Building route table...");
////		try {
////			controlRoom.addRoute("r1", s1, s2, points, signals, path);
////		} catch (IOException e) {
////			e.printStackTrace();
////		}
////	}
//	private static void testNetwork(ControlRoom controlRoom){
//		//****************************//
//		// Testing Network.readFile() //
//		//****************************//
//
//		Network network = new Network("./database/Network.csv");
//		Graph netGraph = network.getNetworkGraph();
//		Location a = new Location("A");
//		Location b = new Location("B");
//		
//		network.display();
//		
////		try {
////			network.readFile("./database/Network.csv");
////		} catch (IOException e) {
////			e.printStackTrace();
////		}
//		
//		
//		System.out.println("Building journey table...");
////		controlRoom.buildJourney("J1",new String[]{"r1","r2","r3"},a.getName(),b.getName());
////		controlRoom.buildJourney("J2",new String[]{"r4","r5","r6"},b.getName(),a.getName());
//		
//		controlRoom.runNetwork();
//
//	}
		
}
