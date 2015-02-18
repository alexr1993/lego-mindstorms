package sample.filetest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import lejos.nxt.LCD;

/**
 * 
 * Test of leJOS NXT File System.
 * 
 * The example creates a file into leJOS NXT File System. 
 * In this case the file, is a KML file used by Google Earth.
 * If you use the command nxjbrowse, you could
 * download that file and to use with Google Earth.
 * 
 * 2008/04/18
 * Current version has problems when increase the size of the file.
 * 
 * @author Juan Antonio Brenha Moral, JAB
 *
 */

public class FileTest {
	static final String fileName = "route.kml";
	static String text;
	static byte[] byteText;
	static FileOutputStream fos;
	static File f;
	static StringBuffer sb;
	static int fileVersion;
	
	/**
	 * This method convert any String into an Array of bytes
	 * 
	 * @param text to convert
	 * @return An Array of bytes.
	 * @author JAB
	 */ 
    static private byte[] getBytes(String inputText){
    	//Debug Point
        byte[] nameBytes = new byte[inputText.length()+1];
        
        for(int i=0;i<inputText.length();i++){
            nameBytes[i] = (byte) inputText.charAt(i);
        }
        nameBytes[inputText.length()] = 0;
 
        return nameBytes;
    }
    
	/**
	 * This method add data into a file
	 * 
	 * @param text to add
	 * @author JAB
	 */  
    private static void appendToFile(String text) throws IOException{
        byteText = getBytes(text);

        //Critic to add a useless character into file
        //byteText.length-1
        for(int i=0;i<byteText.length-1;i++){
            fos.write((int) byteText[i]);
        }    	
    }
    
	/**
	 * This method returns KML file
	 * 
	 * @return KML File
	 * @author JAB
	 */     
    private static String getKML(){
    	sb = new StringBuffer();
        sb.append("<?xml version='1.0' encoding='UTF-8'?>");
        sb.append("<kml xmlns='http://earth.google.com/kml/2.2'>");
        sb.append("<Document>");
        sb.append("<name>KML Sample generated by NXT Brick</name>");
        sb.append("<open>1</open>");
        sb.append("<description>NXJ Example to use leJOS NXT File System</description>");
        sb.append("<Folder>");
        sb.append("<name>Placemarks</name>");
        sb.append("<description>Placemarks generated by my NXT Brick</description>");
        sb.append("<Placemark>");
        sb.append("<name>Waypoint 1</name>");
        sb.append("<description>Waypoint 1</description>");
        sb.append("<Point><coordinates>-3.49618,40.4233</coordinates></Point>");
        sb.append("</Placemark>");
        
        /*
        sb.append("<Placemark>");
        sb.append("<name>Waypoint 2</name>");
        sb.append("<description>Waypoint 2</description>");
        sb.append("<Point><coordinates>-3.49446,40.4216</coordinates></Point>");
        sb.append("</Placemark>"); 
        sb.append("<Placemark>");
        sb.append("<name>Waypoint 3</name>");
        sb.append("<description>Waypoint 3</description>");
        sb.append("<Point><coordinates>-3.49286,40.419</coordinates></Point>");
        sb.append("</Placemark>");
        */
        sb.append("</Folder>");
        sb.append("</Document>");
        sb.append("</kml>");
        
        return sb.toString();
    }
    
   
    public static void main(String[] args)throws Exception {
    	
    	fileVersion = 1;
        LCD.drawString("Testing leJOS",0,0);
        LCD.drawString("file System",0,1);

        LCD.drawString("Creating file...",0,3);
        LCD.refresh();
        
        try{
            f = new File(fileName);
            fos = new  FileOutputStream(f);
            
            text = getKML();
            appendToFile(text);
            
            fos.close();

        }catch(IOException e){
            LCD.drawString(e.getMessage(),0,0);
        }
        
        LCD.drawString("File created",0,4);
        LCD.drawString("Test finished",0,6);
        Thread.sleep(5000);
    }
}