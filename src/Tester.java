

import java.io.IOException;

public class Tester {

	public static void main(String[] args) {
		Location a = new Location("A");
		Signal s1 = new Signal("s1",Signal.UP);
		
		Section b1 = new Section("b1",s1,a);
		
//		System.out.println("Up Neighbour:\t" + s1.getStatus() + "\t" + s1.getDirection());
//		System.out.println("Down Neighbour:\t" + a.getName()+"\n");
//		
//		System.out.println("Up Neighbour:\t" + ((Signal) b1.getUpNeigh()).getStatus() + "\t" + ((Signal) b1.getUpNeigh()).getDirection());
//		System.out.println("Down Neighbour:\t" + b1.getDownNeigh().getName());
//		
//		System.out.println("\nName:\t" + b1.getUpNeigh().getClass().getName());
		
		//**********************//
		// Testing Control Room //
		//**********************//
		Signal s2 = new Signal("s2",Signal.DOWN);
		
		Section b2 = new Section("b2",s2,a);
		Section b3 = new Section("b3",s2,a);
		
		Point p1 = new Point("p1", b1, b2, b3);
		Point p2 = new Point("p2", b1, b2, b3);
		Point[] points = {p1,p2};
		
		Signal[] signals = {s1,s2};
		
		Block[] path = {b1,b2,p1,b3};
		
		ControlRoom controlRoom = new ControlRoom();
		
		try {
			controlRoom.buildRoute("r1", s1, s2, points, signals, path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//****************************//
		// Testing Network.readFile() //
		//****************************//
		Network network = new Network("path");
		
		try {
			network.readFile("./database/Network.csv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
