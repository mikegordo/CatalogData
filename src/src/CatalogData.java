package src;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Catalog Data converter
 * @author Michael Gordo <m.gordo@cityads.ru>
 * 13/02/2014
 */
public class CatalogData {

	private static int catalog_id;
	private static String filename;
	private static String newfilename;
	private static LinkedHashMap<Integer, String> hm = new LinkedHashMap<Integer, String>();
	
	private static String settings = "settings.txt";
	
	/**
	 * Main method
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		if (args.length != 2) {
			System.out.println("Usage: <application> <catalog_id> <filename>");
			System.exit(0);
		}
		
		catalog_id  = Integer.parseInt(args[0]);
		filename    = args[1].toString();
		newfilename = filename + ".sql";
		loadFile();
		String insertLine = loadSettings();		
		insertLine = insertLine.replace(":1", catalog_id + "");
		saveFile(insertLine);
	}
	
	/**
	 * Load file
	 * @return
	 * @throws IOException 
	 */
	private static void loadFile() throws IOException {
		String[] lineArray;
	    BufferedReader br = new BufferedReader(new FileReader(filename));
	    
	    String line = br.readLine();
	    
	    while (null != line) {
	    	if (line.trim().equals("")) continue;
	    	lineArray = line.trim().split("\t");
	    	if (lineArray.length != 2) {
	    		System.out.println("Can't split " + line);
	    		continue;
	    	}
	    	hm.put(Integer.parseInt(lineArray[0]), (String) lineArray[1]);
	    	line = br.readLine();
	    }
	    System.out.println(hm.size() + " lines read from the file");
	    br.close();		
	}
	
	/**
	 * Load settings
	 * @return
	 * @throws Exception 
	 */
	private static String loadSettings() throws Exception {
	    BufferedReader br = new BufferedReader(new FileReader(settings));
	    String line = br.readLine();
	    br.close();
	    if (line.trim().equals("")) {
	    	throw new Exception(settings + " is empty!"); 
	    }
		return line;
	}
	
	/**
	 * Write to file sql
	 * @throws InterruptedException 
	 */
	private static void saveFile(String insertLine) throws IOException, InterruptedException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(newfilename));
		Iterator it = hm.entrySet().iterator();
		int count = 0;
		while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        int id          = (int) pairs.getKey();
	        String value    = pairs.getValue().toString(); 
	        String line     = insertLine.replace(":2", id + "");
	        line            = line.replace(":3", value);
	        line            = line.replace(":5", value);
	        line            = line.replace(":4", count + "");
	        bw.write(line + "\n");
	        bw.flush();
	        //it.remove();
	        count ++;
		}
		System.out.println(count + " lines written to the file");
		bw.close();
	}

}
