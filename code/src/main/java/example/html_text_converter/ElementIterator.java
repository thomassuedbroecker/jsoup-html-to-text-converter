package example.html_text_converter;

import java.util.List;

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
import org.jsoup.select.Elements;

public class ElementIterator {
    
    private StringBuilder plainTextBuilder = new StringBuilder();
    private boolean appendTextVerification = true;

    public ElementIterator (){

    }

    public String getCurrentPlainText (){
           return  plainTextBuilder.toString();            
    }

    public String buildTextByElements (Elements elements){
        
        int i = 0;
        String text = "";
        boolean list_exists = false;
        
        for (Element element : elements) {
            i = i + 1;
            System.out.println("****** " + i + " ******");
            System.out.println("Tag: " + element.tagName());
            // Headers
            String[] headers = {"h1", "h2"};
            String tagTypeString = verifyTagTypes(element, headers);
            if (!tagTypeString.isEmpty()){
                text = getElementOwnTextByTagType(element,tagTypeString);
                if (!text.isEmpty()){
                    appendText("\n\n" + text + "\n\n");
                }
            // Ordered list
            } else if (verifyTagType(element, "ol")){
                text = getElementOwnTextByTagType(element,"ol");
                if (!text.isEmpty()){
                    appendText("\n\n" + text + "\n\n");
                    list_exists = true;
                } else {
                    appendText("\n");
                }
            // List element
            } else if (verifyTagType(element, "li")){
                text = getElementOwnTextByTagType(element,"li");
                if (!text.isEmpty()){
                    appendText("\n* " + text );
                    
                } else {
                    appendText("\n* ");
                }
            // Link a
            } else if (verifyTagType(element, "a")){
                if (matchElementAttribute("class", "xref", element)){
                    String return_value = getElementKeyValue("href", element);
                    if (return_value != ""){
                        appendText( "\n[" + element.text() + "](" + return_value + ")\n");
                    }
                }
            // Image
            } else if (verifyTagType(element, "img")){
                String src_value = getElementKeyValue("src", element);
                String title_value = getElementKeyValue("title", element);
                if (src_value != ""){
                    appendText("\n![" + title_value + "](" + src_value + ")\n");
                }
            // Description list
            } else if (verifyTagType(element, "dl")){
                text = getElementOwnTextByTagType(element,"dl");
                if (!text.isEmpty()){
                    appendText("\n" + text + "\n\n");
                }
            } else if (verifyTagType(element, "dt")){
                text = getElementOwnTextByTagType(element,"dt");
                if (!text.isEmpty()){
                    appendText("\n" + text + "\n: ");
                }
            // table
            } else if (verifyTagType(element, "caption")){
                text = getElementOwnTextByTagType(element,"caption");
                if (!text.isEmpty()){
                    appendText("\n" + text + " ");
                }
            } else if (verifyTagType(element, "tr")){
                text = getElementOwnTextByTagType(element,"tr");
                if (!text.isEmpty()){
                    appendText("\n" + text + " ");
                } else {
                    appendText("\n" + text + " ");
                }
            } else if (verifyTagType(element, "th")){
                text = getElementOwnTextByTagType(element,"th");
                if (!text.isEmpty()){
                    appendText(" " + text + " ");
                }
            } else if (verifyTagType(element, "td")){
                text = getElementOwnTextByTagType(element,"td");
                if (!text.isEmpty()){
                    appendText(" " + text + " ");
                }
            // The Content Division element <div>
            } else if (verifyTagType(element, "div")){
                if ( verifyElementClassName(element, "p")){
                    text = getElementTextByTagType(element,"div");
                    if (!text.isEmpty()){
                        appendText(" " + text + "\n");
                    }
                } else if (matchElementAttribute("class", "title", element)){
                    text = getElementTextByTagType(element,"div");
                    if (!text.isEmpty()){
                        appendText(" " + text + "\n");
                    }
                } else {
                    text = getElementOwnTextByTagType(element,"div");
                    if (!text.isEmpty()){
                        appendText(" " + text + "\n");
                    }
                }
            // Paragraph <p>
            } else if (verifyTagType(element, "p")){
                text = getElementTextByTagType(element,"p");
                if (!text.isEmpty() && !text.isBlank() && !list_exists){
                    appendText(" " + text + "\n");
                } else if (list_exists){
                    list_exists = false;
                }
            }           
        }
        return getCurrentPlainText();
    }

    /**************************** 
     * Private methods
     ****************************/
    
    private boolean verifyElementClassName (Element element, String className){
        if (element.className().equals(className)){
            return true;
        } else {
            return false;
        }
    }

    private String verifyTagTypes(Element element, String[] tagTypesStrings){
        for (String tagType: tagTypesStrings) {           
            if (element.tagName().equals(tagType)){
                return tagType;
            }
        }
        return "";
    }

    private boolean verifyTagType(Element element, String tagType){
        if (element.tagName().equals(tagType)){
            return true;
        } else {
            return false;
        }
    }

    private int childNodeSize(Element element){
        return element.childNodes().size();
    }

    private boolean inspectChildNodes(Element element, String nodeName){
        int size = element.childNodes().size();
        if (size>0){
            List <Node> nodeList = element.childNodes();
            for (int i=0;i<size;i++){
                Node node = nodeList.get(i);
                System.out.println("Find: "+ nodeName + " : current:" + node.nodeName());
                if(node.nodeName().equals(nodeName)){
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    private boolean removeChildNodes(Element element, String nodeName){
        int size = element.childNodes().size();
        boolean removed = false;
        if (size>0){
            List <Node> nodeList = element.childNodes();
            for (int i=0;i<size;i++){
                Node node = nodeList.get(i);
                System.out.println("Delete: "+ nodeName + " : current:" + node.nodeName());
                if(node.nodeName().equals(nodeName)){
                    node.remove();
                    removed = true;
                }
            }
            return removed;
        } else{
            return false;
        }
    }
    
    private String getElementTextByTagType(Element element, String tagType){
        if (element.tagName().equals(tagType)){
            return element.text();
        } else {
            return "";
        }
    }

    private String getElementOwnTextByTagType(Element element, String tagType){
        if (element.tagName().equals(tagType)){
            return element.ownText();
        } else {
            return "";
        }
    }

    private String appendText(String text){
        if(appendTextVerification){
            return plainTextBuilder.append(text).toString();
        } else {
            return "";
        }
    }

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

    private boolean matchElementAttribute(String key, String value, Element e){
        
        List<Attribute> as = e.attributes().asList();
        
        if (e.attributes().size() > 0){
            for (int i=0 ; i< as.size() ; i++){

                    Attribute a = as.get(i);
                    String a_key = a.getKey();
                    String a_value = a.getValue();
                    if ( key.contains(a_key) && a_value.contains(value)){
                        return true;
                    } 
            }
            return false;
        } else {
            return false;
        }
    }

    private String getElementKeyValue(String key, Element e){
        
        List<Attribute> as = e.attributes().asList();
        if (as.size()!=0){
            for (int i=0 ; i<as.size() ; i++){
                    Attribute a = as.get(i);
                    String a_key = a.getKey();
                    String a_value = a.getValue();
                    if ( key.contains(a_key) ) {
                        return a_value;
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

}
