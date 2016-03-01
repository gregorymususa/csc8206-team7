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
		
		ArrayList<Block> blocks = new ArrayList<Block>();
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
				
				hm.put(sctn.getName(), sctn);
			
			}
			
			else if("Location".equalsIgnoreCase(type))
			{
				graph.addNode(name);
				Node n = graph.getNode(name);
				n.addAttribute("path_id", id);
				n.addAttribute("type", type);
				n.addAttribute("object", new Location(name));
			}

		}
		for(int i = 0; i < csvRecords.size(); i+=1) {
			int id = Integer.valueOf(csvRecords.get(i).get("Path_id")).intValue();//
			String type = csvRecords.get(i).get("Type");
			String name = csvRecords.get(i).get("Name");
			String settings = csvRecords.get(i).get("Settings");
			String[] split = settings.split(";");
			String edgeTo = (csvRecords.get(i).get("EdgeTo"));
			String edgeFrom = (csvRecords.get(i).get("EdgeFrom"));
			
			if("Section".equalsIgnoreCase(type))
			{
	
				graph.addEdge(name, edgeTo, edgeFrom);
				Edge e = graph.getEdge(name);
				e.addAttribute("path_id", id);
				e.addAttribute("type", type);
	
				if(hm.containsKey(name))
				{
					e.addAttribute("object", hm.get(name));
				}
				else
				{
					e.addAttribute("object", new Section(name));
				}
		
			}
		}
	
	for(int i = 0; i < csvRecords.size(); i+=1) {
		int id = Integer.valueOf(csvRecords.get(i).get("Path_id")).intValue();//
		String type = csvRecords.get(i).get("Type");
		String name = csvRecords.get(i).get("Name");
		String settings = csvRecords.get(i).get("Settings");
		String[] split = settings.split(";");
		String edgeTo = (csvRecords.get(i).get("EdgeTo"));
		String edgeFrom = (csvRecords.get(i).get("EdgeFrom"));
			
		if("point".equalsIgnoreCase(type))
			{
				graph.addEdge(name, edgeTo, edgeFrom);
				Edge e = graph.getEdge(name);
				e.addAttribute("path_id", id);
				e.addAttribute("type", type);
				
				Edge e1 = graph.getEdge(split[0]);
				Edge e2 = graph.getEdge(split[1]);
				Edge e3 = graph.getEdge(split[2]);
				
				e.addAttribute("object", new Point(name,(Section) e1.getAttribute("object"),(Section)e2.getAttribute("object"),(Section)e3.getAttribute("object")));
				
			}
				
		}
			
//			if(("section".equalsIgnoreCase(type)) && (true == isFirstSection)) {
//				Section s = new Section(name, null, null);
//				System.out.println("Previous entry was a location: " + csvRecords.get(i-1).get("Name"));
//			}
//			else if("section".equalsIgnoreCase(type)) {
//				Section s = new Section(name, null, null);
//				System.out.println("Generate previous and later signal");
//				
//			}
//			if(("section".equalsIgnoreCase(type)) && (true == isLastSection)) {
//				Section s = new Section(name, null, null);
//				System.out.println("Next entry is a location: " + csvRecords.get(i+1).get("Name"));
//			}
			
		
		
		
//		try {
//			fs = FileSourceFactory.sourceFor(filePath);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//			
//		fs.addSink(graph);
		graph.display();
		return false;
	}
}
