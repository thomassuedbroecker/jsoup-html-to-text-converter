package example.html_text_converter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.PseudoTextElement;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.nodes.Attribute;
import org.jsoup.select.NodeVisitor;

/**
 * FormattingVisitor
 */
public class FormattingVisitor implements NodeVisitor {

    private StringBuilder accum = new StringBuilder(); // holds the accumulated text
    private boolean append_text = true;
    Logger logger =  java.util.logging.Logger.getLogger(this.getClass().getName());

    /* ********************* 
     * Public methods
     ***********************/

    // hit when the node is first seen
    public void head(Node node, int depth) {

        String name = node.nodeName();
        String nodetypeString = getNodeType(node);
        
        System.out.println("*** DEBUG head: (" + nodetypeString + ") : (" + depth + ")");

        //System.out.println("*** DEBUG head: ***START****");
        //debugNodeElement(node,depth,"head");
        //System.out.println("*** DEBUG head: ***END****\n");

        if (nodetypeString.equals("Element") && name.equals("head") )
           { System.out.println("*** title-head (exclude):: (" + node.nodeName() + ")");
             append_text = false; 
           }
        else if (nodetypeString.equals("Element") && name.equals("title") )
           { System.out.println("*** title-head (exclude):: (" + node.nodeName() + ")");
             append_text = false; 
           }
        else if (name.equals("div")){
              //append_text = true; 
              //append( "<div>");
              System.out.println("*** div-head (exclude): (" + node.nodeName() + ")(" + nodetypeString + ")");
            }
        else if (name.equals("li"))
           { 
             //append_text = true; 
             //append( "<li>");
             if (((Element) node).ownText().isEmpty()){
                System.out.println("*** li-head (exclude):: (" + node.nodeName() + ")");
                append( "\n* ");
             } else {
                append( "\n* " + ((Element) node).ownText());
             }            
           }
        else if (name.equals("table"))
           { 
             //append_text = true; 
             //append( "<table>");
             if (((Element) node).ownText().isEmpty()){
                System.out.println("*** table (exclude):: (" + node.nodeName() + ")");               
             }    
           }
        else if (name.equals("caption"))
           { 
             //append_text = true; 
             //append( "<caption>");
             if (((Element) node).ownText().isEmpty()){
                System.out.println("*** caption (exclude):: (" + node.nodeName() + ")");               
             }    
           }
        else if (name.equals("tr"))
           { 
             //append_text = true; 
             //append( "<tr>");
             if (((Element) node).ownText().isEmpty()){
                System.out.println("*** table (exclude):: (" + node.nodeName() + ")");
                append( "\n");
             } else {
                append( ((Element) node).ownText() + "\n");
             }          
           }
        else if (name.equals("th"))
           { 
             //append_text = true; 
             //append( "<th>");
             if (!((Element) node).ownText().isEmpty()){
                System.out.println("*** table (exclude):: (" + node.nodeName() + ")");               
                append( " " + ((Element) node).ownText() + " ");
             }           
           }
        else if (name.equals("td"))
           { 
             //append_text = true;  
             //append( "<td>");
             if (!((Element) node).ownText().isEmpty()){
                System.out.println("*** td (exclude):: (" + node.nodeName() + ")");
                //append( " " +  ((Element) node).ownText() + " ");
             }           
           }
        else if (name.equals("img")){
              //append_text = true; 
              //append( "<img>");
              System.out.println("*** img-head (exclude): (" + node.nodeName() + ")(" + nodetypeString + ")");
            }
        else if (name.equals("a")){
              //append_text = true;  
              //append( "<a>");
              System.out.println("*** a-head (exclude): (" + node.nodeName() + ")(" + nodetypeString + ")");        
            }
        else if (StringUtil.in(name, "p")){
              //append_text = true;
              //append( "<p>");
              System.out.println("*** p-head (exclude): (" + node.nodeName() + ")(" + nodetypeString + ")");
            }
        else if (StringUtil.in(name, "div")){
              //append_text = true;
              //append( "<div>");
              System.out.println("*** div-head (exclude): (" + node.nodeName() + ")(" + nodetypeString + ")");
            }
        else if (StringUtil.in(name, "h1")){
            //append_text = true;
            //append( "<h1>");  
            System.out.println("*** h1-head (exclude): (" + node.nodeName() + ")(" + nodetypeString + ")");
        }         
    }

    // hit when all of the node's children (if any) have been visited
    public void tail(Node node, int depth) {
        String name = node.nodeName();
        String nodetypeString = getNodeType(node);

        System.out.println("*** DEBUG tail: (" + nodetypeString + ") : (" + depth + ")");
        
        //System.out.println("*** DEBUG tail: ***START****");
        //debugNodeElement(node,depth, "tail");
        //System.out.println("*** DEBUG tail: ***END****\n");
        
        if (nodetypeString.equals("Element") && name.equals("title") )
           { System.out.println("*** Tail: title: (" + node.nodeName() + ")");
             append_text = false; 
           }
        else if (name.equals("caption"))
           { 
             //append_text = true; 
             //append( "</caption>");
             if (!((Element) node).ownText().isEmpty()){
                System.out.println("*** caption (exclude):: (" + node.nodeName() + ")");
                append( "\n" +  ((Element) node).ownText() + "\n");
             }   
           }
        else if (name.equals("table"))
           { 
             if (!((Element) node).ownText().isEmpty()){
                System.out.println("*** table (exclude):: (" + node.nodeName() + ")");            
             }
             //append_text = true;
             //append( "</table>");  
           }
        else if (name.equals("tr"))
           { 
             if (((Element) node).ownText().isEmpty()){
                System.out.println("*** table (exclude):: (" + node.nodeName() + ")");
                append( "\n");
             } 
             //append_text = true;
             //append( "</tr>");          
           }
        else if (name.equals("th"))
           { 
             if (((Element) node).ownText().isEmpty()){
                System.out.println("*** table (exclude):: (" + node.nodeName() + ")");
                append( " " + ((Element) node).ownText() + " ");
             }
             //append_text = true;
             //append( "</th>");           
           }
        else if (name.equals("td"))
           { 
             if (!((Element) node).ownText().isEmpty()){
                System.out.println("*** table (exclude):: (" + node.nodeName() + ")");
                append( " " +  ((Element) node).ownText() + " ");
             }
             //append_text = true;
             //append( "</td>");              
           }
        else if (name.equals("div"))
           { 
             if (!((Element) node).ownText().isEmpty()){
                System.out.println("*** table (exclude):: (" + node.nodeName() + ")");
                append_text = true;
                append( "\n" +  ((Element) node).ownText() + "\n");
             }
             //append_text = true;
             //append( "\n</div>");              
           }
        else if (name.equals("li")){
             System.out.println("*** Tail: li: (" + node.nodeName() + ")");
             //append_text = true;
             //append( "\n</li>");
        }
        else if (name.equals("img")){
                nodetypeString = getNodeType(node);
                if(nodetypeString.equals("Element")){
                    System.out.println("*** Tail: find img");
                    String src_value = getKeyValue("src", node);
                    String title_value = getKeyValue("title", node);
                    if (src_value != ""){
                        append_text = true;
                        append("\n\n![" + title_value + "](" + src_value + ")\n\n" );
                    } else {
                        append_text = false;
                    }
                }
                //append_text = true;
                //append( "\n</img>");
            }
        else if (name.equals("a")){   
            System.out.println("*** DEBUG a tail");     
            if(nodetypeString.equals("Element")){              
                System.out.println("*** Tail: find class xref");
                if (matchAttribute("class", "xref", node)){
                        String return_value = getKeyValue("href", node);
                        if (return_value != ""){
                            append_text = true;
                            append( "[" + ((Element) node).text() + "](" + return_value + ")");
                       }
                }
            }
            //append_text = true;
            //append( "\n</a>"); 
        } else if (StringUtil.in(name, "p")){
            System.out.println("*** Tail: \"p\"");
            System.out.println("*** Node.Name: (" + node.nodeName() + ")");
            String text = getElementTextByTagType(node,"p");
            if((text != "") && (!text.isEmpty())) {
                  append_text = true;
                  System.out.println("*** append text (" + append_text + ")"); 
                  append(text);            
            }
            //append_text = true;
            //append( "\n</p>"); 
        }
        else if (StringUtil.in(name,"h1")){
            String text = getElementOwnTextByTagType(node,"h1");
            if((text != "") && (!text.isEmpty())) {
                  append_text = true;        
                  append(text + "\n\n");
                  System.out.println("*** append text (" + append_text + ")");             
            }
            //append_text = true;
            //append( "</h1>"); 
        } 
    }

    @Override
    public String toString() {
        return accum.toString();
    }

    /**************************** 
     * Private methods
     ****************************/

    /*********************** 
     * - Formating strings
     ***********************/

    // appends text to the string builder
    private void append(String text) {

        System.out.println("*** Append - text input: ("+ text +")(" + append_text + ")");
        if(append_text){
            if(!text.isEmpty() || !text.isBlank())
                System.out.println("*** Append - text ADDED: ("+ text +")(" + append_text + ")");
                accum.append(text);
        }
        else {
            System.out.println("*** Append - text - NOT ADDED: ("+ text +")(" + append_text + ")");
            append_text = true;
        }
    }

    // Verifies if string matches a value of strings in the array 
    // Example { "", " ", "  ", "\n ", " \n", "\n  \n", "\n \n"}
    private boolean matchStringFilters(String text, String[] existsFiltersArray){
        boolean checkFilter = false;
        for (String existsFilter : existsFiltersArray){              
            if(text.isEmpty() || text.equals(existsFilter)){
                    System.out.println("*** Contains existsFilter: ("+ text +")(" + existsFilter + ")");
                    checkFilter = true;
                    return checkFilter;
            } else {
                    System.out.println("*** Doesn't contain existsFilter: ("+ text +")(" + existsFilter + ")");
                    checkFilter = false;
            }         
       }
       return  checkFilter;
    }

    private String getElementTextByTagType(Node node, String tagType){
        if (((Element)node).tagName().equals(tagType)){
            return ((Element)node).text();
        } else {
            return "";
        }
    }

    private String getElementOwnTextByTagType(Node node, String tagType){
        if (((Element)node).tagName().equals(tagType)){
            return ((Element)node).ownText();
        } else {
            return "";
        }
    }

    /*********************** 
     * - Node and navigation
     ***********************/

    private String getNodeType(Node node){
        if (node instanceof TextNode)
            return "TextNode";
        else if (node instanceof CDataNode)
            return "CDataNode";
        else if (node instanceof Comment)
            return "Comment";
        else if (node instanceof DataNode)
            return "DataNode";
        else if (node instanceof Document)
            return "Document";
        else if (node instanceof DocumentType)
            return "DocumentType";
        else if (node instanceof Element)
            return "Element";
        else if (node instanceof FormElement)
            return "FormElement";
        else if (node instanceof PseudoTextElement)
            return "PseudoTextElement";
        else if (node instanceof XmlDeclaration)
            return "XmlDeclaration";
        else return "unknown";
    }

    private boolean matchAttribute(String key, String value, Node node){

        Element e = ((Element) node).getAllElements().get(0);

        System.out.println("*** DEBUG Element node name:(" + e.nodeName() + ")");
        System.out.println("*** DEBUG text:(" + e.text() + ")");
        
        List<Attribute> as = e.attributes().asList();
        
        if (e.attributes().size() > 0){
            for (int i=0 ; i< as.size() ; i++){

                    Attribute a = as.get(i);
                    String a_key = a.getKey();
                    String a_value = a.getValue();
                    if ( key.contains(a_key) && a_value.contains(value)){
                        System.out.println("*** DEBUG matchAttribute (true): key (" + a_key + ") value (" + a_value + ")");
                        return true;
                    } else {
                        System.out.println("*** DEBUG matchAttribute (false): key (" + a_key + ") value (" + a_value + ")");
                    }    
            }
            System.out.println("*** DEBUG matchAttribute (false): key (" + key + ") value (" + value + ")");
            return false;
        } else {
            System.out.println("*** DEBUG matchAttribute (false): key (" + key + ") value (" + value + ")");
            return false;
        }
    }

    private String getKeyValue(String key, Node node){

        Element e = ((Element) node).getAllElements().get(0);

        System.out.println("*** DEBUG Element node name:(" + e.nodeName() + ")");
        System.out.println("*** DEBUG text:(" + e.text() + ")");
        
        List<Attribute> attributes = e.attributes().asList();
        if (attributes.size()!=0){
            for (int i=0 ; i<attributes.size() ; i++){
                    Attribute a = attributes.get(i);
                    String a_key = a.getKey();
                    String a_value = a.getValue();
                    if ( key.contains(a_key) ) {
                        System.out.println("*** DEBUG getValueKey (true): key (" + a_key + ") value (" + a_value + ")");
                        return a_value;
                    } else {
                        System.out.println("*** DEBUG getValueKey (false): key (" +a_key + ") value (" + a_value + ")");
                    }    
            }
            return "";
        } else {
            if (!e.text().isEmpty()){
                return  e.text();
            } else {
                return "";
            }
        }
    }

    private String getNodeNameParent(Node node){
        Node parent = node.parent();
        if (parent != null ) {
            String parentString = parent.nodeName();
            System.out.println("*** getNodeNameParent:(" + parentString + ")");
            return parentString;
        } else {
            System.out.println("*** getNodeNameParent:()");
            return "";
        }
    }

    private String getNodeNameFirstChild(Node node){
        Node firstChild = node.firstChild();
        if (firstChild  != null ) {
            String childString = firstChild.nodeName();
            return childString;
        } else {
            return "";
        }
    }

    /*********************** 
     * - Debug
     ***********************/
    private boolean debugElement (int i, Node node, List<Attribute> attributes){
            System.out.println("*** **** start *****");
            System.out.println("*** `Node.Name`: " + node.nodeName());
            System.out.println("*** `Element`: artibute (" + i + "/" + attributes.size() + "): Key(" + attributes.get(i).getKey() + "):Value (" + attributes.get(i).getValue() + ")");
            System.out.println("*** `Tag` (" + ((Element) node).tagName() + ")");
            System.out.println("*** **** end *****");
            return true;
    }
    
    private boolean debugNodeElement(Node node, int depth, String info){
        
        StringBuilder logStringBuilder = new StringBuilder();   
        LogFileCreator logFileCreator = new LogFileCreator();
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy") ;
        String curDate =dateFormat.format(date);
        String localFiles = System.getenv("LOCAL_FILES");  

        String nodetypeString = getNodeType(node);
        String nodeName = node.nodeName();

        System.out.println("*** NodeType (" + nodetypeString + ")");
        System.out.println("*** Node.Name (" + nodeName + ")");
        System.out.println("*** depth (" + depth + ")");
        System.out.println("*** **** start *****");

        logStringBuilder.append("\n+++Node (" + depth + ")++++\n");
        logStringBuilder.append("\nNodeType: " + nodetypeString + "\n");
        logStringBuilder.append("\nNodeName: " + nodeName + "\n");
        logStringBuilder.append("\nFirstChildName: "+ getNodeNameFirstChild(node) + "\n");
        logStringBuilder.append("\nParentName: "+ getNodeNameParent(node) + "\n");        
        logStringBuilder.append("\n**** start *****\n");

        if(nodetypeString.equals("Element")){
               List<Attribute> attributes = ((Element) node).attributes().asList();
               System.out.println("*** DEBUG - Tag(" + ((Element) node).tagName() + ")");
               System.out.println("*** DEBUG - Text \n****Text begin****\n(" + ((Element) node).text() + ")\n****Text end****\n");
               
               logStringBuilder.append("\n+++Element+++\n");
               logStringBuilder.append("- Tag: " + ((Element) node).tagName() + "\n");               
               logStringBuilder.append("- Text:\n****Text begin****\n" + ((Element) node).text() + "\n****Text end****\n");

               for (int i=0;i<attributes.size();i++){
                  Attribute attribute = attributes.get(i);
                  System.out.println("*** DEBUG - Element artibute\n (" + i + "): Key(" + attribute.getKey() + "):Value (" + attribute.getValue() + ")");
                  logStringBuilder.append("- Artibute " + i + ": Key(" + attribute.getKey() + "):Value (" + attribute.getValue() + ")\n");
               }
               System.out.println("*** **** end *****");
               logStringBuilder.append("**** end *****\n");
               logFileCreator.createLogFile(localFiles + curDate + "-" + info + "-" + depth  + "-debugNodeElement.log", logStringBuilder.toString());
               
               return true;
        } else {
            if(nodetypeString.equals("Document")){
                 System.out.println("*** DEBUG - DocumentType (" + ((Document) node).documentType() + ")");
                 System.out.println("*** DEBUG - Text (" + ((Document) node).text() + ")");

                 logStringBuilder.append("- DocumentType: (" + ((Document) node).documentType() + ")");
                 logStringBuilder.append("- Text: (" + ((Document) node).text() + ")");
            }
            System.out.println("*** Not an element");
            System.out.println("*** **** end *****");

            logStringBuilder.append("Not an element\n");
            logStringBuilder.append("**** end *****\n");
            logFileCreator.createLogFile(localFiles + curDate + "-" + info + "-" + depth  + "-debugNodeElement.log", logStringBuilder.toString());
            return false;
        }
    }

    private boolean debugNodeText(Node node){
        String nodetypeString = getNodeType(node);
        String nodeName = node.nodeName();
        System.out.println("*** NodeType\n (" + nodetypeString + ")"); 
        System.out.println("*** NodeName\n (" + nodeName + ")");
        System.out.println("*** NodeText\n (" + ((TextNode) node).text() + ")");
        System.out.println("*** **** start *****");
        
        if(nodetypeString.equals("TextNode")){
               
               List<Attribute> attributes = ((TextNode) node).attributes().asList();
               for (int i=0;i<attributes.size();i++){
                  Attribute attribute = attributes.get(i);
                  System.out.println("*** DEBUG - artibute: (" + i + "): Key(" + attribute.getKey() + "):Value (" + attribute.getValue() + ")");
               }
               System.out.println("*** **** end *****");
               return true;
        } else {
            System.out.println("*** **** end *****");
            return false;
        }
    }

}