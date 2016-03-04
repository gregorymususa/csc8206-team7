
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.graphstream.algorithm.APSP;
import org.graphstream.algorithm.APSP.APSPInfo;
import org.graphstream.graph.*;


/**
 * 
 * @author Team 7
 *
 */
public class ControlRoom {

	private Network network;
	private Graph netGraph;
	private HashMap<String, String[]> routeTableMap;
	APSP apsp = new APSP();
	
	public ControlRoom(String filepath) {
		network = new Network(filepath);
		netGraph = network.getNetworkGraph();	
		apsp.init(netGraph);
		apsp.setDirected(false);
		apsp.compute();
		//TODO: Add something that would erase old journey and route files when a new ControlRoom object is created?
	}
	
	public void displayNetwork(){
		network.display();
	}
	
	
	/**
	 * For control room to set Points. Prints out Point track setting.
	 * @param p
	 * @param status
	 * @return
	 */
	boolean setPoint(Point p, String track){
		if(p.setTrack(track)){
			System.out.println(p + " set to " + track + ": "+ p.getTrack() + ".");
			return true;
		}
		
		System.out.println(p + " unable to be set.");
		return false;
	}

	/**
	 * For control room to set Signals. Prints out Signal status setting.
	 * @param s
	 * @param status
	 * @return
	 */
	boolean setSignal(Signal s, String status){
		if(s.setStatus(status)){
			System.out.println(s + "set to " + status + ".");
			return true;
		}

		System.out.println(s + " unable to be set.");
		return false;
	}
		
	/** returns an array of Strings that lists the names of the nodes that make up the shortest path between two given Node objects
	 * 
	 * @param currNode
	 * @param nextNode
	 * @return
	 */
	private String[] getShortestPath(Node currNode, Node nextNode){		
		String nodePath = "";
		APSPInfo c = currNode.getAttribute(APSPInfo.ATTRIBUTE_NAME);
		nodePath = c.getShortestPathTo(nextNode.getId()).toString();				
		String[] nodeParts = nodePath.substring(1, nodePath.length()-1).split(", ");
		return nodeParts;
	}

	/** returns an array of Strings that lists the names of the nodes that make up the shortest path between two Nodes
	 *  takes in the names of the nodes and searches the network for the nodes in question
	 * @param currNodeName
	 * @param nextNodeName
	 * @return
	 */
	private String[] getShortestPath(String currNodeName, String nextNodeName){
		Node currNode = null;
		Node nextNode = null;
		for(Node n: netGraph.getEachNode()){
			if(n.getId().equals(currNodeName))
				currNode = n;
			if(n.getId().equals(nextNodeName))
				nextNode = n;
		}
		String nodePath = "";
		APSPInfo c = currNode.getAttribute(APSPInfo.ATTRIBUTE_NAME);
		nodePath = c.getShortestPathTo(nextNode.getId()).toString();				
		String[] nodeParts = nodePath.substring(1, nodePath.length()-1).split(", ");
		return nodeParts;
	}
	
	/**
	 * builds the route from a given network and saves it to persistent storage (.CSV)
	 * @return HashMap of the route that was constructed. <String routeName, String[] route details (source, destination, points, signals, path, conflicts)>
	 */
	public void buildRoute(){
		ArrayList<Node> upSigN= new ArrayList<Node>();
		ArrayList<Node> downSigN = new ArrayList<Node>();
		HashMap<String, String[]> upFlowRoutes = new HashMap<String,String[]>();
		HashMap<String, String[]> downFlowRoutes= new HashMap<String,String[]>();
		Set<String> keysUp = new HashSet<String>();
		Set<String> keysDown = new HashSet<String>();
		
		String type = "";
		int routeCount = 0;
		
		//build an array of nodes
		for(Node n: netGraph.getEachNode()){
			type = n.getAttribute("type");
			if(type.equals("Signal")){
				String[] potato = n.getAttribute("signal").toString().toUpperCase().split(";");
				if(potato[0].equals("UP")) //generate list of UP view direction signals
					upSigN.add(n);
				else 					   //generate a list of DOWN view direction signals
					downSigN.add(n);
			}
		}

		//Generate UP flow route table fields: 'source', 'destination', 'points', and 'path'
		for(int m = 0; m < upSigN.size()-1; m++)
			for(int n = m+1; n<upSigN.size(); n++){
				//find shortest path between current upSignal and subsequent upSigs in a network
				upFlowRoutes = findRoutes(getShortestPath(upSigN.get(m), upSigN.get(n)), routeCount, upFlowRoutes);
				//only routes that are between adjacent signals will be saved as routes
				routeCount = upFlowRoutes.size();
			}
		
		//Generate DOWN flow route table fields: 'source', 'destination', 'points', and 'path'
		for(int m = 0; m < downSigN.size()-1; m++)
			for(int n = m+1; n<downSigN.size(); n++){
				String[] temp = getShortestPath(downSigN.get(m), downSigN.get(n));
				String[] nodeParts = new String[temp.length];
				for(int k = 0; k<temp.length; k++)
					nodeParts[temp.length-1-k] = temp[k];
				downFlowRoutes = findRoutes(nodeParts, routeCount, downFlowRoutes);
				routeCount = downFlowRoutes.size()+upFlowRoutes.size();
			}
		
		//Fields 'source', 'destination', and 'path' are now complete.

		
		//Next step: finish route table fields: 'points' & 'signals' & 'conflicts'
		
		//create malleable keysets that are editable in the for loops
		keysUp = new HashSet<String>(upFlowRoutes.keySet());
		keysDown = new HashSet<String>(downFlowRoutes.keySet());
		

		String[] routeData1; //data from first route in comparison
		String[] routeData2;//data from second route in comparison
		
		//for each route in upflow...
		for(String k1: upFlowRoutes.keySet()){
			routeData1 = upFlowRoutes.get(k1);
			keysUp.remove(k1); //removes from keyset to be tested against to avoid doubles

			//COMPARING: UP v UP
			for(String k2: keysUp){
				routeData2 = upFlowRoutes.get(k2);
				
				//IF: shared source signal...
				if(routeData1[0].equals(routeData2[0])){
					//add the two routes to each others' 'conflict' lists
					routeData1[5] = routeData1[5]+k2+";";
					routeData2[5] = routeData2[5]+k1+";";

					//add the signals from one conflicting route to the other's 'signals' field and vice versa
					String temp = routeData1[3];
					routeData1[3] = routeData1[3]+routeData2[3]+";";
					routeData2[3] = routeData2[3]+temp+";";
					upFlowRoutes.put(k1,routeData1);
					upFlowRoutes.put(k2,routeData2);					
				}								
				
				//IF: shared destination signal...
				if(routeData1[1].equals(routeData2[1])){
					//add the two routes to each others' 'conflict' lists
					routeData1[5] = routeData1[5]+k2+";";
					routeData2[5] = routeData2[5]+k1+";";
					// the source signals have to go in to each others' 'signals' lists
					routeData1[3] = routeData1[3]+routeData2[0]+";";
					routeData2[3] = routeData2[3]+routeData1[0]+";";
				}
				
				//IF: first route follows the second route...
				if(routeData2[0].equals(routeData1[1])){
					//these routes are not in conflict. 
					//However, the following route does tell the leading route where it could go next.
					//The leading route can reference the follow route's point setting and update its own point field to 
					//require the opposite setting in order to avoid a head-on collision
					String[] temp1 = routeData1[2].split(";");
					String[] temp2 = routeData2[2].split(";");

					if(temp1[0].endsWith("p"))
						routeData2[2] = routeData2[2] + temp1[0].substring(0, temp1[0].length()-1)+"m;";
					else 
						routeData2[2] = routeData2[2] + temp1[0].substring(0, temp1[0].length()-1)+"p;";
					
					if(temp2[0].endsWith("p"))
						routeData1[2] = routeData1[2] + temp2[0].substring(0, temp2[0].length()-1)+"m;";
					else 
						routeData1[2] = routeData1[2] + temp2[0].substring(0, temp2[0].length()-1)+"p;";
				}

				//IF: any routes share a Block in their path
				for(String p1: routeData1[4].split(";"))
					for(String p2: routeData2[4].split(";"))
						if(p1.equals(p2)){
							//add the two routes to each others' 'conflict' lists
							routeData1[5] = routeData1[5] + k2 + ";";
							routeData2[5] = routeData2[5] + k1 + ";";
						}
			}
			
			//COMPARING: UP v DOWN
			for(String k2: downFlowRoutes.keySet()){
				routeData2 = downFlowRoutes.get(k2);
				
				//compare paths for up and down flow
				for(String p1: routeData1[4].split(";"))
					for(String p2: routeData2[4].split(";")){
						//IF: opposite flow routes share a Block in their path, head-on collision can occur
						if(p1.equals(p2)){
							//add the two routes to each others' 'conflict' lists
							routeData1[5] = routeData1[5] + k2 + ";";
							routeData2[5] = routeData2[5] + k1 + ";";
						
							//IF: that shared Block is a Point
							if(p1.startsWith("p")){
								//put each others' source signal onto the others' 'signals' list to avoid derailment accident
								routeData1[3] = routeData1[3] + ";" + routeData2[0] + ";";
								routeData2[3] = routeData2[3] + ";" + routeData1[0]+ ";";
							}
						}
					}
			}
		}

		//for each route in downflow...
		for(String k1: downFlowRoutes.keySet()){
			routeData1 = downFlowRoutes.get(k1);
			keysDown.remove(k1); //removing the key being compared prevents non-conflicting signals from being added to each others' conflict lists.

			//COMPARING: DOWN v DOWN
			for(String k2: keysDown){
				if(!k1.equals(k2)){
					routeData2 = downFlowRoutes.get(k2);
					
					//IF: shared source signal...
					if(routeData1[0].equals(routeData2[0])){
						//add the two routes to each others' 'conflict' lists
						routeData1[5] = routeData1[5]+k2+";";
						routeData2[5] = routeData2[5]+k1+";";

						//add the signals from one conflicting route to the other's 'signals' field and vice versa
						String temp = routeData1[3];
						routeData1[3] = routeData1[3]+routeData2[3]+";";
						routeData2[3] = routeData2[3]+temp+";";
						upFlowRoutes.put(k1,routeData1);
						upFlowRoutes.put(k2,routeData2);					
					}								
					
					//IF: shared destination signal...
					if(routeData1[1].equals(routeData2[1])){
						//add the two routes to each others' 'conflict' lists
						routeData1[5] = routeData1[5]+k2+";";
						routeData2[5] = routeData2[5]+k1+";";
						// the source signals have to go in to each others' 'signals' lists
						routeData1[3] = routeData1[3]+routeData2[0]+";";
						routeData2[3] = routeData2[3]+routeData1[0]+";";
					}
					
					//IF: first route follows the second route...
					if(routeData1[0].equals(routeData2[1])){
						//these routes are not in conflict. 
						//However, the following route does tell the leading route where it could go next.
						//The leading route can reference the follow route's point setting and update its own point field to 
						//require the opposite setting in order to avoid a head-on collision
						String[] temp1 = routeData1[2].split(";");
						String[] temp2 = routeData2[2].split(";");

						if(temp1[0].endsWith("p"))
							routeData2[2] = routeData2[2] + temp1[0].substring(0, temp1[0].length()-1)+"m;";
						else 
							routeData2[2] = routeData2[2] + temp1[0].substring(0, temp1[0].length()-1)+"p;";
						
						if(temp2[0].endsWith("p"))
							routeData1[2] = routeData1[2] + temp2[0].substring(0, temp2[0].length()-1)+"m;";
						else 
							routeData1[2] = routeData1[2] + temp2[0].substring(0, temp2[0].length()-1)+"p;";
					}

					//IF: any routes share a Block in their path
					for(String p1: routeData1[4].split(";"))
						for(String p2: routeData2[4].split(";"))
							if(p1.equals(p2)){
								//add the two routes to each others' 'conflict' lists
								routeData1[5] = routeData1[5] + k2 + ";";
								routeData2[5] = routeData2[5] + k1 + ";";
							}
				}
			}
		}

		//combine upFlowRoutes and downFlowRoutes into the same HashMap...
		for(Entry<String, String[]> e: downFlowRoutes.entrySet())
			upFlowRoutes.putIfAbsent(e.getKey(), e.getValue());

		//get rid of duplicate entries in 'signals' and 'conflicts' in the combined HashMap
		for(String k: upFlowRoutes.keySet()){
			String[] e = upFlowRoutes.get(k);
			String[] parts = e[3].split(";");
			Set<String> unique = new HashSet<String>(Arrays.asList(parts));
			e[3] = "";
			for(String u: unique)
				if(!u.equals(""))
					e[3] = e[3] + u + ";";
			upFlowRoutes.put(k,e);
		}
		for(String k: upFlowRoutes.keySet()){
			String[] e = upFlowRoutes.get(k);
			String[] parts = e[5].split(";");
			Set<String> unique = new HashSet<String>(Arrays.asList(parts));
			e[5] = "";
			for(String u: unique)
				if(!u.equals(""))
					e[5] = e[5] + u + ";";
			upFlowRoutes.put(k,e);
		}
		
		routeTableMap = upFlowRoutes;
		//Save the route interlocking table to persistent storage (CSV)
		saveRoute(routeTableMap);
	}
	
	private void saveRoute(HashMap<String,String[]> routeMap){
		for(Entry<String, String[]> ent: routeMap.entrySet()){
			String[] p = ent.getValue();
			try {
				addRoute(ent.getKey(), p[0], p[1], p[2].substring(0, p[2].length()), p[3], p[4].substring(0, p[4].length()-1), p[5].substring(0, p[5].length()-1));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method builds the preliminary route table entry for a given list of nodes between two signals
	 * The source signal is the first node.
	 * The destination signal is the last node.
	 * @param nodeParts - an array of strings/names of each node in the railway network 
	 * @param routeCount - for route naming purposes
	 * @return the current number of routes created in the system
	 */
	private HashMap<String, String[]> findRoutes(String[] nodeParts, int routeCount, HashMap<String, String[]> routeMap){
		
		routeCount++;
		if(nodeParts.length<8 && nodeParts.length>3){
			String source = nodeParts[0];
			String destination = nodeParts[nodeParts.length-1];
			String sigs = "";
			String path = "";
			String points = "";
			for(int k = 1; k < nodeParts.length-1; k++){
				//any signals that exist between the source and destination are paired downflow signals
				if(nodeParts[k].startsWith("s"))
					sigs = sigs+nodeParts[k]+";";
				else //anything else is a point or block that is part of the path
					path = path+nodeParts[k]+";";
				
				//if the current node being checked is a point...
				if(nodeParts[k].startsWith("p")){
					Point p = netGraph.getNode(nodeParts[k]).getAttribute("object");
					
					//if the node before the point is a block...
					if(nodeParts[k-1].startsWith("b")){ 		//if moving FROM stem to plus or minus...
						if(p.getMinusLine().getName().equals(nodeParts[k+2])) //checks to see if moving to minus line...
							points = p+":m;"; 					//set: minus
						else									//otherwise, set: plus 
							points = p+":p;";						
					}
					else{//the node before the point is a signal, so this route is moving from plus or minus TO stem
						Signal s = netGraph.getNode(nodeParts[k-1]).getAttribute("SignalObject");
						points = p+":"+ s.getLine().substring(0,1)+";"; 					//set: minus
					}
					
				}
			}
//			System.out.println("ROUTE " + routeCount+ "\tSource:\t" + source+ "\n\tDest:\t" + destination + "\n\tPoints:\t" + points + "\n\tSignals:"+ sigs + "\n\tPath:\t" + path);
			routeMap.put("r"+routeCount, new String[] {source, destination, points, sigs, path, ""});
			sigs = "";
			path = "";
			points = "";
		}
		return routeMap;
	}

	/**
	 * Saves the names of the parameters to persistent storage (CSV file)
	 * TODO potential extension � save to DB
	 * TODO potential extension � use fewer parameters, and extrapolate the route from it (less chance of input/human error)
	 * 
	 * @param route_name
	 * @param source
	 * @param destination
	 * @param points
	 * @param signals
	 * @param path
	 * @param conflicts 
	 * @throws IOException 
	 */
	public void addRoute(String route_name,String source,String destination,String points,String signals,String path, String conflicts) throws IOException {
		File file = new File("./database/Route.csv");
		
		CSVFormat csvformat = null;
		if(file.isFile()) 
			csvformat = CSVFormat.MYSQL.withRecordSeparator("\n").withDelimiter(',');
		else
			csvformat = CSVFormat.MYSQL.withHeader("id","source_signal","dest_signal","points","signals","path", "conflicts").withRecordSeparator("\n").withDelimiter(',');
		
		FileWriter filewriter = new FileWriter(file,true);
				
		CSVPrinter csvPrinter = new CSVPrinter(filewriter,csvformat);
		
		ArrayList<String> record = new ArrayList<String>();
		
		record.add(route_name);
		record.add(source);
		record.add(destination);
		record.add(points);
		record.add(signals);
		record.add(path);
		record.add(conflicts);
		
		csvPrinter.printRecord(record);
		
		filewriter.flush();
		filewriter.close();
		csvPrinter.close();
		
//		System.out.println(System.getProperty("user.dir"));
	}
	

	/**
	 * judge the source location is exist
	 * @param location
	 */
	private  boolean isLocationValid(String location) {
		for(Node n: netGraph.getEachNode()){
			if(location.equalsIgnoreCase(n.getId()))
				return true;
		}
		return false;
	}
	
	/**
	 * Public-facing method that allows the user to build a journey.
	 * @param routeTableMap 
	 */
	public void buildJourney(){
		Scanner in = new Scanner(System.in);
		String[] routesArr;
		String routesStr= "";
		String startlocation = "";
		String endlocation = "";
		int numJourneys = 0;
		
		printRoutes(routeTableMap);
		
		do{	//route sequence reset
			routesStr = "";
			numJourneys++;
			
			System.out.println("Building journey \"j"+numJourneys+"\"...");
			
			//Enter origin location
			System.out.print("  Enter origin location: ");
			startlocation = in.nextLine().toLowerCase();
			while(!isLocationValid(startlocation)){
				System.out.print("    ERROR: Origin location does not exist. \n  Please re-enter origin: ");
				startlocation = in.nextLine().toLowerCase();
			}

			//Enter destination location
			System.out.print("  Enter destination location: ");
			endlocation = in.nextLine().toLowerCase();
			while(startlocation.equals(endlocation)){
				System.out.print("    ERROR: Origin and destination locations cannot be the same. \n  Please re-enter destination: ");
				endlocation = in.nextLine();
			}
			while(!isLocationValid(endlocation)){
				System.out.print("    ERROR: Destination location does not exist. \n  Please re-enter destination: ");
				endlocation = in.nextLine().toLowerCase();
			}

			//Enter routes for journey details
			System.out.print("  Enter a sequence of routes that make up the journey separated by semi-colons (\";\"): ");
			routesStr= in.nextLine().toLowerCase();
			routesArr = routesStr.split(";");
			
			if(isJourneyValid(startlocation, endlocation, routesArr)){			
				saveJourney("j"+numJourneys, routesArr, startlocation, endlocation);
				System.out.println("Saved journey j"+numJourneys+"("+ startlocation + ","+endlocation+"): :"+routesStr);
			}else{
				System.out.println("Unable to save invalid journey. Quitting building journey j"+numJourneys);
				numJourneys--;
			}
			System.out.print("Would you like to add another journey? (y/n) ");
		}while(in.nextLine().toLowerCase().startsWith("y"));
		System.out.println("... Finished building journeys.");
		in.close();
	}
	
	
	/**
	 * prints the route table given a HashMap containing the data
	 * @param routeTableMap
	 */
	public void printRoutes(HashMap<String, String[]> routeTableMap) {
		System.out.println("Routes Avalable: ");
		for( Entry<String, String[]> e:routeTableMap.entrySet()){
			String[] parts = e.getValue();
			System.out.println("   "+e.getKey()+" ("+parts[0]+","+parts[1]+"): pts("+parts[2]+")  sigs("+parts[3]+")  path("+parts[4]+")  conf("+parts[5]+")");
		}
		System.out.println();
	}

	/**
	 * returns true if journey input is valid
	 * @param origin
	 * @param destination
	 * @param routesArr
	 * @return
	 */
	private boolean isJourneyValid(String origin, String destination, String[] routesArr){
		boolean valid = true;
		String[] routeData;

		//check to see if the input journey routes exist in the table.
		for(String r: routesArr)
			if(!routeTableMap.containsKey(r)){
				System.out.println("    ERROR: input route " + r + " does not exist in table.");
				valid = false;
			}
		//checking to see if first route sets out from the right location
		routeData = routeTableMap.get(routesArr[0]);
		if(getShortestPath(origin,routeData[0]).length > 4){
			System.out.println("    ERROR: input route " + routesArr[0] + routeData[0] + " does not begin at origin " + origin + ".");
			valid = false;
		}

		//checking to make sure the routes are in order
		for(int i =0; i<routesArr.length-1; i++)
			for(int j = i+1; j<routesArr.length; j++){
				String[] first = routeTableMap.get(routesArr[i]);
				String[] second = routeTableMap.get(routesArr[j]);
				if(!first[1].equals(second[0])){
					System.out.println("    ERROR: input route " + routesArr[i] + " is not connected to " + routesArr[j] + ".");
					valid = false;
				}
		}

		//checking to see if last route arrives to the right location
		routeData = routeTableMap.get(routesArr[routesArr.length-1]);
		if(getShortestPath(destination,routeData[0]).length > 4){
			System.out.println("    ERROR: input route " + routesArr[1] + " does not terminate at destination " + destination + ".");
			valid = false;
		}

		return valid;		
	}
	
	/**
	 * Builds a journey and adds it to persistent storage (CSV file)
	 * @param id
	 * @param routes
	 * @param startlocation
	 * @param endlocation
	 */
	private void saveJourney(String id,String[] routes,String startlocation, String endlocation){
		CSVFormat csvformat = null;
		FileWriter filewriter = null;
		CSVPrinter csvPrinter = null;
		try {
			File file = new File("./database/Journey.csv");
			if(file.isFile()) {
				csvformat = CSVFormat.MYSQL.withRecordSeparator("\n").withDelimiter(',');
			}
			else {
				csvformat = CSVFormat.MYSQL.withHeader("id","routes","startlocation","endlocation").withRecordSeparator("\n").withDelimiter(',');
			}
			
			filewriter = new FileWriter(file,true);
			
			csvPrinter = new CSVPrinter(filewriter,csvformat);
			
			ArrayList<String> record = new ArrayList<String>();
			
			record.add(id);
			
			String routes_toString = "";
			for(String p : routes) {
				if("".equalsIgnoreCase(routes_toString))
					routes_toString = p;
				else
					routes_toString = routes_toString + ";" + p;
			}
			record.add(routes_toString);
			
			record.add(startlocation);
			record.add(endlocation);
			
			csvPrinter.printRecord(record);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(filewriter!=null)
					filewriter.flush();
				if(filewriter!=null)
					filewriter.close();
				if(csvPrinter!=null)
					csvPrinter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
