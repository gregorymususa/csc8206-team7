

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

/**
 * 
 * @author Team 7
 *
 */
public class ControlRoom {

	public ControlRoom() {
		
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
	 * @throws IOException 
	 */
	public void buildRoute(String route_name,Signal source,Signal destination,Point[] points,Signal[] signals,Block[] path) throws IOException {
		File file = new File("./database/Route.csv");
		
		CSVFormat csvformat = null;
		if(file.isFile()) {
			csvformat = CSVFormat.MYSQL.withRecordSeparator("\n").withDelimiter(',');
		}
		else {
			csvformat = CSVFormat.MYSQL.withHeader("id","source_signal","dest_signal","points","signals","path").withRecordSeparator("\n").withDelimiter(',');
		}
		
		FileWriter filewriter = new FileWriter(file,true);
		
		
		
		CSVPrinter csvPrinter = new CSVPrinter(filewriter,csvformat);
		
		ArrayList<String> record = new ArrayList<String>();
		
		record.add(route_name);
		record.add(source.toString());
		record.add(destination.toString());

		String points_toString = "";
		for(Point p : points) {
			if("".equalsIgnoreCase(points_toString))
				points_toString = p.toString();
			else
				points_toString = points_toString + ";" + p;
		}
		record.add(points_toString);
		
		String signal_toString = "";
		for(Signal s : signals) {
			if("".equalsIgnoreCase(signal_toString))
				signal_toString = s.toString();
			else
				signal_toString = signal_toString + ";" + s;
		}
		record.add(signal_toString);
		
		String path_toString = "";
		for(Block b : path) {
			if("".equalsIgnoreCase(path_toString))
				path_toString = b.toString();
			else
				path_toString = path_toString + ";" + b;
		}
		record.add(path_toString);
		
		csvPrinter.printRecord(record);
		
		filewriter.flush();
		filewriter.close();
		csvPrinter.close();
		
//		System.out.println(System.getProperty("user.dir"));
	}
	
}
