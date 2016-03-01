package rethink;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceFactory;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;

public class Network 
{
	private static Graph graph;
	private FileSource fs; 
	
	public Network(String filePath)
	{
		graph = new SingleGraph("Network");
		
		//readFile(filePath);
		
		//graph.display();
	}

	public static void main(String [] args)
	{
		Network network = new Network("path");
		//Example Code
		graph.addNode("A");
		graph.addNode("b1");
		graph.addNode ("b2");
		graph.addNode ("p1");
		graph.addNode("b3");
		graph.addNode("b4");
		graph.addNode ("p2");
		graph.addNode("b5");
		graph.addNode("b6");
		graph.addNode("B");
		
		graph.addEdge("A0B1", "A", "b1");
		graph.addEdge("B1B2", "b1", "b2");
		graph.addEdge("B2P1", "b2", "p1");
		graph.addEdge("P1B3", "p1", "b3");
		graph.addEdge("P1B4", "p1", "b4");
		graph.addEdge("B3P2", "b3", "p2");
		graph.addEdge("B4P2", "b4", "p2");
		graph.addEdge("P2B5", "p2", "b5");
		graph.addEdge("B5B6", "b5", "b6");
		graph.addEdge("B6B0", "b6", "B");
		
		
		//Testing sprite
		//SpriteManager sman = new SpriteManager(graph);
		//Sprite s = sman.addSprite("S1");
	
		
		graph.display();
		
		//Testing assigning objects to nodes and retrieving information
		Node A = graph.getNode("A");
		A.addAttribute("signal", new Signal("s1",Signal.DOWN));
//		for(Edge e:A.getEachEdge())
//			{
			 Signal s1 =A.getAttribute("signal");
			System.out.println( s1.getDirection());
//			}
			
	}
	
	/**
	 * TODO Read File
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public boolean readFile(String filePath) throws IOException
	{
		File csvFile = new File(filePath);
		CSVParser csvParser = CSVParser.parse(csvFile,Charset.forName("UTF-8"),CSVFormat.EXCEL.withHeader("Path_id","Type","Name","Settings","isFirstSection", "isLastSection").withSkipHeaderRecord(true));
		
		ArrayList<Block> blocks = new ArrayList<Block>();
		
		List<CSVRecord> csvRecords = csvParser.getRecords();
		
		for(int i = 0; i < csvRecords.size(); i+=1) {
			int id = Integer.valueOf(csvRecords.get(i).get("Path_id")).intValue();//
			String type = csvRecords.get(i).get("Type");
			String name = csvRecords.get(i).get("Name");
			String settings = csvRecords.get(i).get("Settings");
			boolean isFirstSection = Boolean.valueOf(csvRecords.get(i).get("isFirstSection"));
			boolean isLastSection = Boolean.valueOf(csvRecords.get(i).get("isLastSection"));
			
			if(("section".equalsIgnoreCase(type)) && (true == isFirstSection)) {
				Section s = new Section(name, null, null);
				System.out.println("Previous entry was a location: " + csvRecords.get(i-1).get("Name"));
			}
			else if("section".equalsIgnoreCase(type)) {
				Section s = new Section(name, null, null);
				System.out.println("Generate previous and later signal");
				
			}
			if(("section".equalsIgnoreCase(type)) && (true == isLastSection)) {
				Section s = new Section(name, null, null);
				System.out.println("Next entry is a location: " + csvRecords.get(i+1).get("Name"));
			}
			
		}
		
		
//		try {
//			fs = FileSourceFactory.sourceFor(filePath);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//			
//		fs.addSink(graph);
		
		return false;
	}
}
