package example.html_text_converter;

import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeTraversor;

import java.util.List;

import java.util.Date;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.io.IOException;
import org.json.JSONObject;
import example.html_text_converter.LogFileCreator;
import example.html_text_converter.ElementIterator;

public class ExampleConverter {
    private Document doc = null;
    private Logger logger = null;

    public ExampleConverter(){
       logger =  java.util.logging.Logger.getLogger(this.getClass().getName());
    }

    public JSONObject convertHTMLtoJSON_ElementsIterator(String html_string){
        
        doc = Jsoup.parse(html_string);
        JSONObject jsonObject = new JSONObject();  
        Elements elements = doc.getAllElements();
        ElementIterator elementIterator = new ElementIterator();
        
        //debugElementsJsonObject(elements);
        // format these elements to plain text
        String text = elementIterator.buildTextByElements(elements);

        jsonObject.put("html_content", html_string);
        jsonObject.put("text", text);
            
        return jsonObject;
    }

    public JSONObject convertHTMLtoJSON_Visiter(String html_string){
        
        doc = Jsoup.parse(html_string);
        JSONObject jsonObject = new JSONObject();  
        Elements elements = doc.select("body").select("*");
        String text = "";
        
        //debugElementsJsonObject(elements);

        if(elements.size()>0){
            Element element = elements.first();
            text = this.getPlainText_Formater(element);

            jsonObject.put("html_content", html_string);
            jsonObject.put("text", text);
            
            return jsonObject;

        } else {

            jsonObject.put("html_content", html_string);
            jsonObject.put("text", "");

            return jsonObject;
        }

    }

    private String getPlainText_Formater(Element element) {
        example.html_text_converter.FormattingVisitor formatter = new example.html_text_converter.FormattingVisitor();
        // walk the DOM, and call .head() and .tail() for each node
        NodeTraversor.traverse(formatter, element); 
        return formatter.toString();
    }

    public Document convertHTMLtoDoc(String html_String) {
        doc = Jsoup.parse(html_String);
        return doc;
    }

    /**************************** 
     * Private methods
     ****************************/

    /*********************** 
     * - Debugging
     ***********************/
    private JSONObject debugElementsJsonObject(Elements elements){     

        StringBuilder logStringBuilder = new StringBuilder(); 
        JSONObject jsonObject = new JSONObject();  
        LogFileCreator logFileCreator = new LogFileCreator();
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy") ;
        String curDate =dateFormat.format(date);
        String localFiles = System.getenv("LOCAL_FILES"); 
        
        logStringBuilder.append("List elements\n");
        int i = 0;
        for (Element element : elements) {
            i++;            
            logStringBuilder.append("\n+++++++ Element: "+ i + " +++++++" + "\n");
            logStringBuilder.append("- Tag Name: " + element.tagName() + "\n");
            logStringBuilder.append("- Class name: " + element.className() + "\n");
            logStringBuilder.append("- Text:\n****Text begin****\n" + element.text()+ "\n****Text end****\n");
            logStringBuilder.append("- OwnText:\n****OwnText begin****\n" + element.ownText() + "\n****OwnText end****\n");
            logStringBuilder.append("- Child node size: " + element.childNodeSize() + "\n");
            if (element.childNodeSize() > 0){
                logStringBuilder.append("\n++ Child nodes: ++\n");
                List<Node> nodes = element.childNodes();
                for(int k=0;k<element.childNodeSize();k++){
                    Node n = nodes.get(k);
                    logStringBuilder.append("+++ Node (" + k + ") +++\n");
                    logStringBuilder.append("- Node name:" + n.nodeName() + "\n");
                    List<Attribute> attributes = n.attributes().asList();
                    int listSize = attributes.size();;
                    logStringBuilder.append("--- Attributes: " + listSize + "---" + "\n");
                    for (int l=0;l<listSize;l++){
                        Attribute attribute = attributes.get(l);
                        logStringBuilder.append("-" + l + ": Key(" + attribute.getKey() + ") : Value (" + attribute.getValue() + ")" + "\n");
                    }
                }
            }
            if (element.parents().first() != null){
                logStringBuilder.append("- First parent name: " + element.parents().first().nodeName() + "\n"); 
            } else {
                logStringBuilder.append("- First parent name: " + "NO PARENT" + "\n"); 
            }
                
            logStringBuilder.append("- Text:\n****Text begin****\n" + element.text()+ "\n****Text end****" + "\n");
            logStringBuilder.append("- OwnText:\n****OwnText begin****\n" + element.ownText() + "\n****OwnText end****" + "\n");
            
            List<Attribute> attributes = element.attributes().asList();
            int listSize = attributes.size();
            logStringBuilder.append("--- Attributes: " + listSize + "---" + "\n");
            for (int j=0;j<listSize;j++){
                  Attribute attribute = attributes.get(j);
                  logStringBuilder.append("-" + j + ": Key(" + attribute.getKey() + ") : Value (" + attribute.getValue() + ")" + "\n");
            }
        }

        logFileCreator.createLogFile(localFiles + curDate + "-debugElementsJsonObject.log", logStringBuilder.toString());

        jsonObject.put("html_content", "Debug only");
        jsonObject.put("text", "");
        
        return jsonObject;
    }

}
