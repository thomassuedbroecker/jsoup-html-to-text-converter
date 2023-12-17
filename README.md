# Writing an `HTML`-to-text  converter can be the first task in an AI pipeline with the `JSOUP Java Libary`

This project is about the powerful [JSOUP Java Libary](https://jsoup.org/), which gives you the freedom to convert an [`HTML`](https://developer.mozilla.org/en-US/docs/Learn/Getting_started_with_the_web/HTML_basics) to `plain` formatted text, based on your requirements by extracting and inspecting `HTML` elements in various ways. We check two methods to do this in this example.

**Content**

1. [Introduction](#1-introduction)
2. [Objectives](#2-objectives)
3. [Basic information about `JSOUP`](#3-basic-information-about-jsoup)
4. [Simplified architecture of the example](#4-simplified-architecture-of-the-example)
5. [Input and output examples](#5-input-and-output-format-of-the-example-application)
6. [Run the example Application](#6-run-the-example-application)
7. [Summary](#7-summary)
8. [Additional resources for JSOUP](#8-additional-resources-jsoup)

## 1. Introduction

Writing an `HTML-to-text` converter can be one of the first tasks in an [AI pipeline](https://squarkai.com/whats-an-ai-pipeline/#:~:text=An%20AI%20Pipeline%20is%20an,parameters%2C%20and%20other%20prediction%20outputs.), because, for the majority of AI Systems, you need to input data in the correct format.

In [generative AI](https://research.ibm.com/blog/what-is-generative-AI) you are even more dependent on the right formatted input data to the AI when you use prompts.

With that in mind, we are now taking a closer look at the [JSOUP Java Libary](https://jsoup.org/) and how to convert an [`HTML`](https://developer.mozilla.org/en-US/docs/Learn/Getting_started_with_the_web/HTML_basics) to `plain` formatted text.

Let us start a bit with `HTML`([HyperText Markup Language](https://www.w3schools.com/html/default.asp)).
An HTML page contains [TAGs](TAG](https://developer.mozilla.org/en-US/docs/Web/HTML/Element/dt)) with a **start** `<TAG>` and an **end** `</TAG>` that [TAG](https://developer.mozilla.org/en-US/docs/Web/HTML/Element/dt) can contain attributes. The TAG can contain nested `TAGs`.

Here is an example code for an `HTML` page we want to use as input for your generative AI prompt text.

```html
<!DOCTYPE html>
<html>
<body>

<h1>My First Heading</h1>
<p>My first <strong>paragraph strong text.</strong></p>

</body>
</html>
```

The following text below is an example, of how you want to format the text input for your generative AI model.

```text
My First Heading

My first paragraph strong text.
```

The document of the example above contains five tags (elements) the document can be represented as a list of [elements](https://jsoup.org/apidocs/org/jsoup/nodes/Element.html) or in [node](https://jsoup.org/apidocs/org/jsoup/nodes/Node.html) lists later in the [JSOUP framework](https://jsoup.org/). In the context of converting HTML to text, it can be helpful to take a short look into [Anatomy on an HTML element](https://developer.mozilla.org/en-US/docs/Learn/Getting_started_with_the_web/HTML_basics#anatomy_of_an_html_element).

The list below shows the elements from different perspectives. Each element can contain a list of elements surrounded by a Tag, which can include additional attributes 

1. `<html>` contains the tags/elements
    * `<body>`
    * `<h1>`
    * `<p>` and `<strong>`.
2. `<body>` contains the tags/elements 
    * `<h1>`
    * `<p>`
    * `<strong>`.
3. `<h1>` has no nested tags/elements.
4. `<p>` contains the tags/elements 
    * `<strong>`.
5. `<p>` has no nested tags/elements.

## 2. Objectives

The goal is to build `HTML-to-text` conversion.

* `HTML to TEXT conversion`
    
    * Load HTML from a file
    * Convert HTML String Byte Array to JSON Text using to approaches

       1. Convert it using  an [`Elements`](https://jsoup.org/apidocs/org/jsoup/select/Elements.html) list
       2. Convert it using a [`NodeVisitor`](https://jsoup.org/apidocs/org/jsoup/select/NodeVisitor.html)

## 3. Basic information about `JSOUP`

Basic information about `JSOUP`. With `JSOUP`, it is possible to iterate a list of elements or traverse nodes by visiting each node's `head <>` and the `tail </>`.

Example `HTML`:

```html
<div>
  <p></p>
<div>
```

* Element list iteration

    * The first element contains the `<div>` and the nested `<p>`.
    * The second element contains `<p>`.

* Traverse nodes by visiting the `head <>` and the `tail </>`
  
  In this case, you need to implement a visitor.

  * The first node contains the ```<div>``` and the nested ```<p>```.
  * The second node contains ```<p>```.

* Simplified diagram of the main dependencies for `JSOUP`

The following diagram shows the main dependencies for `JSOUP`. In the diagram (_which is not UML conform_) you see that a [`Document`](https://jsoup.org/apidocs/org/jsoup/nodes/Document.html) contains [`Elements`](https://jsoup.org/apidocs/org/jsoup/select/Elements.html) and each [Element](https://jsoup.org/apidocs/org/jsoup/nodes/Element.html) can contain a list of [`Nodes`](https://jsoup.org/apidocs/org/jsoup/nodes/Node.html) and [Attributes](https://jsoup.org/apidocs/org/jsoup/nodes/Attributes.html).

![](/images/ExampleConverter-2023-11-23-Select%20and%20Nodes%20dependencies.drawio.png)


### 3.1 Example implementation `Element list iteration`


Here is a code extract from the example implementation for a table row. If we find an element of the tag type `tr` and the row is empty, we just insert a `\n`; otherwise, we add the `ownText`, and an additional `\n`.

  * Input

  ```html
  <tr>Own text 
      <td>Not own text</td>
  </tr>
  ```

  * Output

  ```text
  
  OwnText

  ```

  ```java
  public String buildTextByElements (Elements elements)
  ...
  else if (verifyTagType(element, "tr")){
                  text = getElementOwnTextByTagType(element,"tr");
                  if (!text.isEmpty()){
                      appendText("\n" + text + "\n");
                  } else {
                      appendText("\n");
                  }
  ...
  ```

### 3.2 Example implementation for the `head <>` and the `tail </>`

Here is a code extract from the example implementation for a table row. When we find in the `<head>` a table row and the text for that row is empty, we insert a `\n`; otherwise, we at the `ownText`, which is the text that belongs only to the `<tr>` tag. We only add a line break in the `<head>`.
  
  * Input

  ```html
  <tr>Own text 
      <td>Not own text</td>
  </tr>
  ```

  * Output

  ```text
  
  OwnText

  ```
* `<head>` implementation

  ```java
  public void head(Node node, int depth) {
          else if (name.equals("tr"))
           { 
             if (((Element) node).ownText().isEmpty()){
                append( "\n");
             } else {
                append( ((Element) node).ownText() + "\n");
             }          
           }
  ```
* `<tail>` implementation

  ```java
  public void tail(Node node, int depth) {
          ...
          else if (name.equals("tr"))
           { 
              append( "\n");       
           }
  ```

## 4. Simplified architecture of the example

The `ExampleConverter` provides two methods to convert the `html_string` to a `JSON` object that provides the source HTML code and the converted plain text as a return value.

```java
public JSONObject convertHTMLtoJSON_ElementsIterator(String html_string)
public JSONObject convertHTMLtoJSON_Visiter(String html_string)
```

* Example return value for the `JSONObject` of each method

```json
{ "html_content":"<div>Text</div>",
  "text":"Text" }
```

The diagram below shows the simplified dependencies. The example only depends on `jsoup` and `json`.

![](/images/ExampleConverter-2023-11-23-Overview.drawio.png)

The following simplified UML diagram shows the five classes implemented in the example.

* `ExampleApp`
  This class uses the two possibilities to convert the HTML text and write the result info files.
* `ExampleConverter`,
  The `ExampleConverter` provides the two approaches to convert:

  * Using the `Elements` list in the ELementIterator class
  * Using the <HEAD> and <TAIL> in the `FormatingVistor` class

![](/images/UML-01-DetailedOverview.png)

The source code below shows an extract of the [Maven Project Object Model `.pom` file](https://maven.apache.org/pom.html) [`dependencies`](https://maven.apache.org/pom.html#dependencies) implementation.

```xml
    <dependencies>
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-compress</artifactId>
            <version>1.24.0</version>
        </dependency>
        <dependency>
            <groupId>org.apache.logging.log4j</groupId>
            <artifactId>log4j-slf4j-impl</artifactId>
            <version>2.18.0</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.jsoup/jsoup -->
        <dependency>
            <groupId>org.jsoup</groupId>
            <artifactId>jsoup</artifactId>
            <version>1.16.2</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.json/json -->
        <dependency>
            <groupId>org.json</groupId>
            <artifactId>json</artifactId>
            <version>20231013</version>
        </dependency>
    </dependencies>
```

## 5. Input and output format of the example application

* Example `HTML input`:

```html
<!DOCTYPE html\n SYSTEM \"about:legacy-compat\">
<html>

<head>
  <title>head title</title>
</head>

<body>
  <div>
    <h1>header 1</h1>
    <div>
      <p class="p">p-text-1_2 <span>span text</span> p-text-2_2.</p>
      <ul class="ul">
        <li class="li">
          <p class="p">p-text-in-li-1.</p>
        </li>
        <li class="li">li-text</li>
      </ul>
    </div>
    <div>
      <table>
        <caption>
          table caption
        </caption>
        <tr>
          <th scope="col">table header 1</th>
          <th scope="col">table header 2</th>
          <th scope="col">table header 3</th>
        </tr>
        <tr>
          <th scope="row">table header 4</th>
          <td>table data 4.1</td>
          <td>table data 4.2</td>
        </tr>
        <tr>
          <th scope="row">table header 5</th>
          <td>table data 5.1</td>
          <td>table data 5.2</td>
        </tr>
        <tr>
          <th scope="row">table header 6</th>
          <td>table data 6.1</td>
          <td>table data 6.2</td>
        </tr>
      </table>
    </div>
    <div>
      <dl>
        description list 1
        <dt>description term element 1</dt>
      </dl>
    </div>
    <div>
      <div>div text 1</div>
      <div><a class=\"xref\"
          href=\"/href 1\"
          target=\"_blank\" rel=\"noopener\"
          alt=\"alt 1\"
          title=\"href title 1">link text 1<img
            src=\"path_to_image.png" class=\"link\" alt=\"alt information\" title=\"title information\" border=\"0\"></a>
      </div>
    </div>
</body>

</html>
```

* Example text output

```sh
header 1

 p-text-1_2 span text p-text-2_2.

*  p-text-in-li-1.

* li-text
table caption 
  table header 1  table header 2  table header 3 
  table header 4  table data 4.1  table data 4.2 
  table header 5  table data 5.1  table data 5.2 
  table header 6  table data 6.1  table data 6.2 
description list 1


description term element 1
:  div text 1

[link text 1](\"/href)

![\"title](\"path_to_image.png")
```

## 6. Run the example Application

The gif below shows the debug execution of the example application.

![](images/2023-12-17_20-44-03-Debug.gif)

### Step 1: Clone the project to your local machine

```sh
git clone git clone https://github.com/thomassuedbroecker/jsoup-html-to-text-converter.git
```

### Step 2: Create an `.env` file

```sh
cd jsoup-html-to-text-converter
cat .env_template > .env
```
### Step 3: Edit the environment file

The folder `data` in this project contains an example HTML file and a file containing the files' filenames to be converted to text.

```sh
export LOCAL_FILES=/Users/YOUR_PATH/jsoup-html-to-text/code/data/
export HTML_FILE=nested-content.html
```

### Step 4: Edit the `.vscode/launch.json` to specify where the `.env` file is located.

Just replace your `YOUR_PATH` with your path.

```json
{
    // Use IntelliSense to learn about possible attributes.
    // Hover to view descriptions of existing attributes.
    // For more information, visit: https://go.microsoft.com/fwlink/?linkid=830387
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "ExampleApp",
            "request": "launch",
            "mainClass": "example.ExampleApp",
            "envFile": "/Users/YOUR_PATH/dev/jsoup-html-to-text/code/.env"

        },
        {
            "type": "java",
            "name": "ExampleApp",
            "request": "launch",
            "mainClass": "example.ExampleApp",
            "projectName": "jsoup-html-to-text",
            "envFile": "/Users/YOUR_PATH/jsoup-html-to-text/code/.env"

        },
        {
            "type": "java",
            "name": "Current File",
            "request": "launch",
            "mainClass": "${file}",
            "envFile": "/Users/YOUR_PATH/jsoup-html-to-text/code/.env"
        }
    ]
}
```

### Step 5: Open a new terminal from the code folder of the project

### Step 6: Load the environment variables

```sh
cd code
source ./.env
```

### Step 6: In VSCode select the `ExampleApp.java` file and press the run button inside the file.

## 7. Summary

It was interesting to see how text information can be extracted from HTML and converted to text differently.

The [JSOUP Java Libary](https://jsoup.org/) is powerful and gives you the freedom to convert an [`HTML`](https://developer.mozilla.org/en-US/docs/Learn/Getting_started_with_the_web/HTML_basics) to `plain` formatted text, based on your requirements by extracting and inspecting `HTML` elements in various ways.

There is also a [`jsoup` cookbook](https://jsoup.org/cookbook/) that contains guides.

## 8. Additional resources

* [Iterate all documents](https://stackoverflow.com/questions/7036332/jsoup-select-and-iterate-all-elements)
* [Dom Navigation](https://jsoup.org/cookbook/extracting-data/dom-navigation)
* [Html Tags definition](https://developer.mozilla.org/en-US/docs/Web/HTML/Element/dt)
* [Remove node](https://www.tabnine.com/code/java/methods/org.jsoup.nodes.Node/remove)
* [CSS Selectors](https://www.w3schools.com/cssref/css_selectors.php) 

Where does AI start? One AI explanation in one of the [IBM Design pages](https://www.ibm.com/design/ai/basics/ai/#:~:text=Narrow%20AI%20focuses%20on%20one,it%20examples%20to%20learn%20from.) says:

_"Narrow AI focuses on one, narrow task of human intelligence, and within it, there are two branches: rule-based _AI and _example-based_ AI._ The former involves giving the machine rules to follow while _the latter _gives_ it examples_ to learn from."_

Source [IBM learning](https://www.ibm.com/design/ai/basics/ai/#:~:text=Narrow%20AI%20focuses%20on%20one,it%20examples%20to%20learn%20from.)