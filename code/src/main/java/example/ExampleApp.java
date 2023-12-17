package example;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Date;
import org.json.JSONObject;

import example.html_text_converter.ExampleConverter;
import example.html_text_converter.LogFileCreator;

public class ExampleApp {

    public static void main(String[] args) throws IOException {

        String localFiles_String = System.getenv("LOCAL_FILES");
        String htmlFileName_String = System.getenv("HTML_FILE");

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy") ;
        String curDate =dateFormat.format(date);  
        LogFileCreator logFileCreator = new LogFileCreator();
        JSONObject retObj = null;
        String convertedfilename = "";

        ExampleConverter exampleConverter = new ExampleConverter();
        try {
            String html_content = loadHTMLFromFile(localFiles_String + htmlFileName_String);
            
            // ElementsIterator
            retObj = exampleConverter.convertHTMLtoJSON_ElementsIterator(html_content);
            convertedfilename = localFiles_String + curDate + "-ExampleConverterRawDataElementsIterator.log";
            System.out.println("Convert file: " + localFiles_String + htmlFileName_String + " to " + convertedfilename +  " ");
            logFileCreator.createLogFile( convertedfilename , (String)retObj.get("text")); 

            // NodeVisitor
            retObj = exampleConverter.convertHTMLtoJSON_Visiter(html_content);
            convertedfilename = localFiles_String + curDate + "-ExampleConverterRawDataNodeVisitor.log";
            System.out.println("Convert file: " + localFiles_String + htmlFileName_String + " to " + convertedfilename +  " ");
            logFileCreator.createLogFile( convertedfilename , (String)retObj.get("text"));
        
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
    }  

    /**************************** 
     * Private methods
     ****************************/

    private static String loadHTMLFromFile(String filename) throws FileNotFoundException {
        File file = new File(filename);
        Scanner scanner = new Scanner(file);
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            String textLineString = scanner.nextLine();
            stringBuilder.append(textLineString);
        }
        scanner.close();
        return stringBuilder.toString();
    }
}
