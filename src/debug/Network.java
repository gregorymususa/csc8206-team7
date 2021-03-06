package debug;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

/**
 * This class represents the Railway Network
 * 
 * @author Team 7
 *
 */
public class Network 
{
	private Graph graph;
	
	public Network(String filePath)
	{
		graph = new SingleGraph("Network");
		try {
			readFile(filePath);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the Network, represented by GraphStream's Graph
	 * @return a Graph representation of the Network (Graph by GraphStream)
	 */
	public Graph getNetworkGraph() {
		Graph g = this.graph;
		return g;
	}
	
	/**
	 * Display a graph as a Network
	 */
	public void display() {
		graph.display();
	}

	/**
	 * Reads in Network from File
	 * @param filePath a CSV file, containing a representation of the Network
	 * @return <code>true</code> if Network built successfully
	 * @throws IOException
	 */
	public boolean readFile(String filePath) throws IOException
	{
		File csvFile = new File(filePath);
		CSVParser csvParser = CSVParser.parse(csvFile,Charset.forName("UTF-8"),CSVFormat.EXCEL.withHeader("Path_id","Type","Name","Settings","EdgeFrom","EdgeTo").withSkipHeaderRecord(true));
		
		Map<String, Section> hm = new HashMap<String,Section>();
		
		List<CSVRecord> csvRecords = csvParser.getRecords();
		
		for(int i = 0; i < csvRecords.size(); i+=1) {
			
			if("".equals(csvRecords.get(i).get("Path_id"))) {
				continue;
			}
			int id = Integer.valueOf(csvRecords.get(i).get("Path_id")).intValue();
			String type = csvRecords.get(i).get("Type");
			String name = csvRecords.get(i).get("Name");
			String settings = csvRecords.get(i).get("Settings");
			String[] split= settings.split(";");
			
			if("Signal".equalsIgnoreCase(type))
			{
				graph.addNode(name);
				Node n = graph.getNode(name);
				n.addAttribute("path_id", id);
				n.addAttribute("type", type);
				n.addAttribute("signal", settings);
				
				Section sctn = new Section(split[1]);
				
				if(split.length == 2)
				{
					n.addAttribute("SignalObject", new Signal(name, split[0], sctn));
				}
				else
				{
					n.addAttribute("SignalObject", new Signal(name, split[0], sctn,split[2]));
				}
				 n.addAttribute("ui.label", name+" "+split[0]);
				hm.put(sctn.getName(), sctn);
			
			}
			
			else if("Location".equalsIgnoreCase(type))
			{
				if(graph.getNode(name) == null) {
					graph.addNode(name);
					Node n = graph.getNode(name);
					n.addAttribute("path_id", id);
					n.addAttribute("type", type);
					n.addAttribute("object", new Location(name));
					n.addAttribute("ui.label", name);
				}
			}

		}
		for(int i = 0; i < csvRecords.size(); i+=1) {
			
			if("".equals(csvRecords.get(i).get("Path_id"))) {
				continue;
			}
			
			int id = Integer.valueOf(csvRecords.get(i).get("Path_id")).intValue();//
			String type = csvRecords.get(i).get("Type");
			String name = csvRecords.get(i).get("Name");
			
			if("Section".equalsIgnoreCase(type))
			{
	
				graph.addNode(name);
				Node n = graph.getNode(name);
				n.addAttribute("path_id", id);
				n.addAttribute("type", type);
				 n.addAttribute("ui.label", name);
				if(hm.containsKey(name))
				{
					n.addAttribute("object", hm.get(name));
				}
				else
				{
					n.addAttribute("object", new Section(name));
				}
		
			}
		}
	
		for(int i = 0; i < csvRecords.size(); i+=1) {
			
			if("".equals(csvRecords.get(i).get("Path_id"))) {
				continue;
			}
			
			int id = Integer.valueOf(csvRecords.get(i).get("Path_id")).intValue();//
			String type = csvRecords.get(i).get("Type");
			String name = csvRecords.get(i).get("Name");
			String settings = csvRecords.get(i).get("Settings");
			String[] split = settings.split(";");
				
			if("point".equalsIgnoreCase(type))
				{
					graph.addNode(name);
					Node n = graph.getNode(name);
					n.addAttribute("path_id", id);
					n.addAttribute("type", type);
					 n.addAttribute("ui.label", name);
					Node n1 = graph.getNode(split[0]);
					Node n2 = graph.getNode(split[1]);
					Node n3 = graph.getNode(split[2]);
					
					n.addAttribute("main", split[0]);
					n.addAttribute("plus", split[1]);
					n.addAttribute("minus", split[2]);
					
					n.addAttribute("object", new Point(name,(Section) n1.getAttribute("object"),(Section)n2.getAttribute("object"),(Section)n3.getAttribute("object")));
					
				}
					
			}
				
		for(int i = 0; i < csvRecords.size(); i+=1) 
		{
			
			String edgeTo = csvRecords.get(i).get("EdgeTo");
			String edgeFrom = csvRecords.get(i).get("EdgeFrom");
			
			if(!(edgeTo.equals("")&&edgeTo.equals("")))
			{
			String name = edgeTo+edgeFrom;
				
			
			Node n1 = graph.getNode(edgeTo);
			Node n2 = graph.getNode(edgeFrom);
			
			graph.addEdge(name,n2,n1);
			Edge n = graph.getEdge(name);
			n.addAttribute("to", edgeTo);
			n.addAttribute("from", edgeFrom);
			}				if("".equals(csvRecords.get(i).get("Path_id"))) {
				continue;
			}
		}
		
		if(!(this.isValid(graph,csvRecords))) {
			throw new RuntimeException("Network is not valid!" + "\nMake sure the Network file adheres to the following rules:" +
					"\nA Network File must start with Location, and end with Location" +
					"\nA Location must have a Section neighbour" + 
					"\nA Point has three stemline, plusline, and minusline neighbours that must be Sections" + 
					"\nSections that are connected to a Location must have a pair of signals" + 
					"\nSections that are connected on both ends to a Point must have a pair of signals");
		}
		
		return true;
	}
	
	/**
	 * Validates the Network file, to ensure that an invalid Network is not built.
	 * 
	 * Network validation rules:
	 * A Network File must start with Location, and end with Location
	 * A Location must have a Section neighbour
	 * A Point has three stems mainline, plusline, and minusline neighbours that must be Sections
	 * Sections that are connected to a Location must have a pair of signals
	 * Sections that are connected on both ends to a Point must have a pair of signals
	 * 
	 * @param graph that we will validate
	 * @param csvRecords imported from the Network file (also validated)
	 * @return <code>true</code> if Network adheres to the "Network Validity rules"; <code>false</code> otherwise
	 */
	private boolean isValid(Graph graph,List<CSVRecord> csvRecords) {
		boolean b = true;
		
		//Network File must start with Location, and end with Location
		if(!(("Location".equalsIgnoreCase(csvRecords.get(0).get("Type"))) && ("Location".equalsIgnoreCase(csvRecords.get(csvRecords.size()-1).get("Type"))))) {
			return false;
		}
		
		ArrayList<Node> al = new ArrayList<Node>();
		Iterator<Node> n = graph.iterator();
		while(n.hasNext()) {
			al.add(n.next());
		}
		ListIterator<Node> nodes = al.listIterator();
		
		while(nodes.hasNext()) {
			Node currNode = nodes.next();			
			
			if("Location".equalsIgnoreCase(currNode.getAttribute("type"))) { //A Location must have a Section neighbour
				Iterator<Edge> edges = currNode.getEdgeIterator();
				while(edges.hasNext()) {
					Edge edge = edges.next();
					Node oppositeNode = edge.getOpposite(currNode);
					
					if(!("Section".equalsIgnoreCase(oppositeNode.getAttribute("type")))) {
						return false;
					}
					else { //Sections that are connected to a Location must have a pair of signals
						Iterator<Edge> ei = oppositeNode.getEdgeIterator();
						boolean hasSignalPair = false;
						while(ei.hasNext()) {
							Node oppN = ei.next().getOpposite(oppositeNode);
							if("Signal".equalsIgnoreCase(oppN.getAttribute("type"))) {
								Iterator<Edge> ei2 = oppN.getEdgeIterator();
								while(ei2.hasNext()) {
									Node oppN2 = ei2.next().getOpposite(oppN);
									if("Signal".equalsIgnoreCase(oppN2.getAttribute("type"))) {
										hasSignalPair = true;
									}
								}
							}
						}
						if(hasSignalPair == false) {
							return false;
						}
					}
				}
			}
			else if("Point".equalsIgnoreCase(currNode.getAttribute("type"))) { //A Point has three stems mainline, plusline, and minusline neighbours that must be Sections THEN Sections that are connected on both ends to a Point must have a pair of signals
				String main = currNode.getAttribute("main");
				String plus = currNode.getAttribute("plus");
				String minus = currNode.getAttribute("minus");
				
				if(!("Section".equalsIgnoreCase(graph.getNode(main).getAttribute("type")) &&
				   "Section".equalsIgnoreCase(graph.getNode(plus).getAttribute("type")) &&
				   "Section".equalsIgnoreCase(graph.getNode(minus).getAttribute("type")))) {
					return false;
				}
				
				Node plusNode = graph.getNode(plus);
				Node minusNode = graph.getNode(minus);
				
				Iterator<Edge> ei = plusNode.getEdgeIterator();
				while(ei.hasNext()) {
					Node np = ei.next().getOpposite(plusNode);
					if(!("Signal".equalsIgnoreCase(np.getAttribute("type")))) {
						return false;
					}
				}
				
				Iterator<Edge> ei2 = minusNode.getEdgeIterator();
				while(ei2.hasNext()) {
					Node nm = ei2.next().getOpposite(minusNode);
					if(!("Signal".equalsIgnoreCase(nm.getAttribute("type")))) {
						return false;
					}
				}
			}

		}
		return b;
	}
}

