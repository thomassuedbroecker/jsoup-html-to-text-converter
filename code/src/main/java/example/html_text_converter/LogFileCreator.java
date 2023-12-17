package example.html_text_converter;

import java.io.FileWriter;
import java.util.logging.Logger;

public class LogFileCreator {
    private Logger logger =  java.util.logging.Logger.getLogger(this.getClass().getName());

    public LogFileCreator(){

    };

    public boolean createLogFile(String filepathString, String logcontentString ){
      try {
       FileWriter output = new FileWriter(filepathString);
       output.write(logcontentString);
       logger.info("Logfile (" + filepathString + ") is created.");
       output.close();
       return true;
      }
      catch (Exception e) {
       logger.info("Error writing file: " + e.getStackTrace().toString());
       return false;
      }
    }
    
}
