package controlroombuilding;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
	
	public ControlRoom(String filepath) {
		network = new Network(filepath);
		netGraph = network.getNetworkGraph();	
//		network.display();
	}


	/**
	 * runs the trains on the network after all journeys are set
	 * Extension of the main project.
	 */
	void runNetwork() {
		System.out.println("Run train network.");
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
	
	public void buildRoute(){
		ArrayList<Node> upSigN= new ArrayList<Node>();
		ArrayList<Node> downSigN = new ArrayList<Node>();
		HashMap<String, String[]> upFlowRoutes = new HashMap<String,String[]>();
		HashMap<String, String[]> downFlowRoutes= new HashMap<String,String[]>();
		String type = "";
		int routeCount = 0;
		
		
		APSP apsp = new APSP();
		apsp.init(netGraph);
		apsp.setDirected(false);
		apsp.compute();

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

		//Start filling in route table fields: 'source', 'destination', 'points', and 'path'

		//UP flow routes
		System.out.println("--UP SIGNALS--");
		for(int m = 0; m < upSigN.size()-1; m++){
			//Look at each upFlow signal in the list...
			Node currSig = upSigN.get(m);
			Node nextSig = upSigN.get(m);
			String nodePath = "";			
			for(int n = m+1; n<upSigN.size(); n++){
				nextSig = upSigN.get(n);
				APSPInfo c = currSig.getAttribute(APSPInfo.ATTRIBUTE_NAME);
				nodePath = c.getShortestPathTo(nextSig.getId()).toString();				
				String[] nodeParts = nodePath.substring(1, nodePath.length()-1).split(", ");
				upFlowRoutes = findRoutes(nodeParts, routeCount, upFlowRoutes);
				routeCount = upFlowRoutes.size();
			}
//			System.out.println(upFlowRoutes);
		}
				
		//DOWN flow routes
		System.out.println("--DOWN SIGNALS--");		
		for(int m = 0; m < downSigN.size()-1; m++){
			//Look at each upFlow signal in the list...
			Node currSig = downSigN.get(m);
			Node nextSig = downSigN.get(m);
			String nodePath = "";
			for(int n = m+1; n<downSigN.size(); n++){
				nextSig = downSigN.get(n);
				APSPInfo c = currSig.getAttribute(APSPInfo.ATTRIBUTE_NAME);
				nodePath = c.getShortestPathTo(nextSig.getId()).toString();				
				String[] temp = nodePath.substring(1, nodePath.length()-1).split(", ");
				String[] nodeParts = new String[temp.length];

				//flipping the order of parts in nodeParts so that the identical 
				// algorithm will work for the DOWN direction as in the UP 
				for(int k = 0; k<temp.length; k++)
					nodeParts[temp.length-1-k] = temp[k];
				downFlowRoutes = findRoutes(nodeParts, routeCount, downFlowRoutes);
				routeCount = downFlowRoutes.size()+upFlowRoutes.size();
			}
		}
		
		//now to cross-reference the UP flow and DOWN flow routes. This completes the 
		// 'points' and 'signals' fields and also fills out 'conflicts' for each route.
		
		//for each route in upflow...
		Set<String> keys = upFlowRoutes.keySet();
		
		for(String k1: keys){
			String[] first = upFlowRoutes.get(k1);
//			keys.remove(k1); //removing the key being compared prevents non-conflicting signals from being added to each others' conflict lists.

			//UP flow list compared to UP flow list
			for(String k2: keys){
				String[] second = upFlowRoutes.get(k2);
				String[] firstPath = first[4].split(";");
				String[] secondPath = second[4].split(";");
				
				//if any routes share a source and/or destination signal...
				if(first[2].equals(second[0])||first[0].equals(second[1])||first[1].equals(second[0])||first[1].equals(second[1])){
					//if a route that connects to this one is detected
					//add the two routes to each others' 'conflict' lists
					first[5] = first[5]+k2+";";
					second[5] = second[5]+k1+";";
					//add the signals from one conflicting route to the other's 'signals' field and vice versa
					String temp = first[3];
					first[3] = first[3]+second[3]+";";
					second[3] = second[3]+temp+";";
					upFlowRoutes.put(k1,first);
					upFlowRoutes.put(k2,second);					
				}

				//if any routes share Sections in their path
				if(firstPath.length>0 && secondPath.length>0){
					for(int i = 0; i <firstPath.length; i++){
						for(int j =0; j <secondPath.length; j++){
							if(firstPath[i].equals(secondPath[j])){
								//add the two routes to each others' 'conflict' lists
								first[5] = first[5] + k2 + ";";
								second[5] = second[5] + k1 + ";";
							}
						}
					}
				}
				upFlowRoutes.put(k2, second);
			}
			
			//UP flow list compared to DOWN flow list
			//compare the Points and Sections listed in the paths
			for(String k2: downFlowRoutes.keySet()){
				String[] second = downFlowRoutes.get(k2);
				String[] firstPath = first[4].split(";");
				String[] secondPath = second[4].split(";");

				//if any routes share a point or section...
				if(firstPath.length>0 && secondPath.length>0){
					for(int i = 0; i < firstPath.length; i++){
						for(int j =0; j < secondPath.length; j++){
							if(firstPath[i].equals(secondPath[j])){
								//add the two routes to each others' 'conflict' lists
								first[5] = first[5] + k2 + ";";
								second[5] = second[5] + k1 + ";";
							}
						}
					}
				}
				
				//if a source or destination signal exists in the other route's 'signals' list
				String[] sigsList = second[3].split(";");
				for(int j=0; j<sigsList.length; j++){
					if(first[0].equals(sigsList[j])||first[1].equals(sigsList[j])){
						//add the two routes to each others' 'conflict' lists
						first[5] = first[5] + k2 + ";";
						second[5] = second[5] + k1 + ";";
						
						for(int m = 0; m<firstPath.length;m++)
							for(int n=0; n<secondPath.length;n++)
								if(firstPath[m].startsWith("p"))
									if(secondPath[n].startsWith("p"))
										if(!firstPath[m].equals(secondPath[n])){ 
											// if the points that are crossed in each path are not the same
											//	add each others' points with opposite settings to 'points'.
											//TODO: fix the points settings to make sure they're opposite
											String tempStr = first[2];
											first[2] = first[2] + second[2] + ";";
											second[2] = second[2] + tempStr + ";";
										}
					}
			 	}
				downFlowRoutes.put(k2, second);
			}
			upFlowRoutes.put(k1, first);
		}

		//for each route in downflow...
		keys = downFlowRoutes.keySet();
		for(String k1: keys){
			String[] first = downFlowRoutes.get(k1);
//			keys.remove(k1); //removing the key being compared prevents non-conflicting signals from being added to each others' conflict lists.

			//DOWN flow list compared to DOWN flow list
			for(String k2: keys){
				String[] second = downFlowRoutes.get(k2);
				
				//compare the source and destination signals to the other routes in the downflow
				if(first[2].equals(second[0])||first[0].equals(second[1])||first[1].equals(second[0])||first[1].equals(second[1])){
					//if a matching signal name is detected
					//add the two routes to each others' 'conflict' lists
					first[5] = first[5]+k2+";";
					second[5] = second[5]+k1+";";
					//add the signals from one conflicting route to the other's 'signals' field and vice versa
					String temp = first[3];
					first[3] = first[3]+second[3]+";";
					second[3] = second[3]+temp+";";
					downFlowRoutes.put(k1,first);
					downFlowRoutes.put(k2,second);			
				}
				downFlowRoutes.put(k2, second);
			}
			downFlowRoutes.put(k1, first);
		}

		
		//get rid of duplicate entries in 'signals' and 'conflicts' in each route
		for(String k: upFlowRoutes.keySet()){
			String[] e = upFlowRoutes.get(k);
			String[] parts = e[3].split(";");
			Set<String> unique = new HashSet<String>(Arrays.asList(parts));
			e[3] = "";
			for(String u: unique)
				e[3] = e[3] + u + ";";
			upFlowRoutes.put(k,e);
		}
		for(String k: downFlowRoutes.keySet()){
			String[] e = downFlowRoutes.get(k);
			String[] parts = e[3].split(";");
			Set<String> unique = new HashSet<String>(Arrays.asList(parts));
			e[3] = "";
			for(String u: unique)
				e[3] = e[3] + u + ";";
			downFlowRoutes.put(k,e);
		}
		
//TODO: fix clunkiness
		for(String k: upFlowRoutes.keySet()){
			String[] e = upFlowRoutes.get(k);
			String[] parts = e[5].split(";");
			Set<String> unique = new HashSet<String>(Arrays.asList(parts));
			e[5] = "";
			for(String u: unique)
				e[5] = e[5] + u + ";";
			upFlowRoutes.put(k,e);
		}
		for(String k: downFlowRoutes.keySet()){
			String[] e = downFlowRoutes.get(k);
			String[] parts = e[5].split(";");
			Set<String> unique = new HashSet<String>(Arrays.asList(parts));
			e[5] = "";
			for(String u: unique)
				e[5] = e[5] + u + ";";
			downFlowRoutes.put(k,e);
		}

		
		
		//Save the route interlocking table to persistent storage
		for(Entry<String, String[]> ent: upFlowRoutes.entrySet()){
			String[] p = ent.getValue();
//			System.out.println(ent.getKey()+"\nsource-"+ p[0]+" dest-"+ p[1]+" points-"+ p[2]+" sigs-"+ p[3]+" path-"+ p[4]+" conf-"+ p[5]);
			try {
//				addRoute(ent.getKey(), p[0], p[1], p[2],p[3],p[4],p[5]);
				addRoute(ent.getKey(), p[0], p[1], p[2].substring(0, p[2].length()), p[3].substring(1, p[3].length()-1), p[4].substring(0, p[4].length()-1), p[5].substring(0, p[5].length()-1));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(Entry<String, String[]> ent: downFlowRoutes.entrySet()){
			String[] p = ent.getValue();
			try {
				addRoute(ent.getKey(), p[0], p[1], p[2].substring(0, p[2].length()), p[3].substring(1, p[3].length()-1), p[4].substring(0, p[4].length()-1), p[5].substring(0, p[5].length()-1));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
		

	/**
	 * This method builds the preliminary route table entry fir a given list of nodes between two signals
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
				if(nodeParts[k].startsWith("p")){
//						System.out.println(netGraph.getNode(nodeParts[k]).getAttributeKeySet());
					Point p= netGraph.getNode(nodeParts[k]).getAttribute("object");
					if(nodeParts[k-1].startsWith("b")){ 		//if moving FROM stem to plus or minus...
						if(p.getMinusLine().getName().equals(nodeParts[k+2])) //checks to see if moving to minus line...
							points = p+":m;"; 					//set: minus
						else									//otherwise, set: plus 
							points = p+":p;";						
					}
					else{										//this route is moving from plus or minus TO stem
						if(p.getMinusLine().getName().equals(nodeParts[k-1])) //checks to see if moving from minus line...
							points = p+":m;"; 					//set: minus
						else									//otherwise, set: plus 
							points = p+":p;";														
					}	
				}
			}
//			System.out.println("ROUTE " + routeCount+ "\tSource:\t" + source+ "\n\tDest:\t" + destination + "\n\tPoints:\t" + points + "\n\tSignals:"+ sigs + "\n\tPath:\t" + path);
			routeMap.put("r"+routeCount, new String[] {source, destination, points, sigs, path, ""});
//			System.out.println();
			sigs = "";
			path = "";
			points = "";
		}
		return routeMap;
	}

	/**
	 * Saves the names of the parameters to persistent storage (CSV file)
	 * TODO potential extension — save to DB
	 * TODO potential extension — use fewer parameters, and extrapolate the route from it (less chance of input/human error)
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
	
	boolean checkRouteSafety(Network n){
		return true;
	}

	/**
	 * Builds a journey and adds it to persistent storage (CSV file)
	 * @param id
	 * @param routes
	 * @param startlocation
	 * @param endlocation
	 */
	public void buildJourney(String id,String[] routes,String startlocation, String endlocation){
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
