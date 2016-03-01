package rethink;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	/**
	 * TODO Read File
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public boolean readFile(String filePath) throws IOException
	{
		File csvFile = new File(filePath);
		CSVParser csvParser = CSVParser.parse(csvFile,Charset.forName("UTF-8"),CSVFormat.EXCEL.withHeader("Path_id","Type","Name","Settings","isFirstSection", "isLastSection","EdgeFrom","EdgeTo").withSkipHeaderRecord(true));
		
		Map<String, Section> hm = new HashMap<String,Section>();
		
		List<CSVRecord> csvRecords = csvParser.getRecords();
		
		for(int i = 0; i < csvRecords.size(); i+=1) {
			int id = Integer.valueOf(csvRecords.get(i).get("Path_id")).intValue();//
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
				n.addAttribute("SignalObject", new Signal(name, split[0], sctn));
				 n.addAttribute("ui.label", name);
				hm.put(sctn.getName(), sctn);
			
			}
			
			else if("Location".equalsIgnoreCase(type))
			{
				graph.addNode(name);
				Node n = graph.getNode(name);
				n.addAttribute("path_id", id);
				n.addAttribute("type", type);
				n.addAttribute("object", new Location(name));
				 n.addAttribute("ui.label", name);
			}

		}
		for(int i = 0; i < csvRecords.size(); i+=1) {
			int id = Integer.valueOf(csvRecords.get(i).get("Path_id")).intValue();//
			String type = csvRecords.get(i).get("Type");
			String name = csvRecords.get(i).get("Name");
			
			if("Section".equalsIgnoreCase(type))
			{
	
				graph.addNode(name);
				Node n = graph.getNode(name);
				System.out.println("Trace: " + id);
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
		
		System.out.println("Node"+n1);
		graph.addEdge(name,n2,n1);
		Edge n = graph.getEdge(name);
		//n.addAttribute("to", edgeTo);
		//n.addAttribute("from", edgeFrom);
		}				
		}

		graph.display();
		return false;
	}
}
