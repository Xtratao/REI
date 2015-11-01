import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ReadXMLFile {
	
	protected static String DIRNAME = "lemonde-utf8";
	
	public static ArrayList<String> listDocuments;
	
	public static void getDocumentsNames() throws ParserConfigurationException, SAXException, IOException
	{
		listDocuments = new ArrayList<String>();
		
		File subIndexDir = new File("subindex/2015");
		String[] directoriesInSubindex = subIndexDir.list();
		
		for(String d : directoriesInSubindex)
		{
			String[] listXMLFiles = new File("subindex/2015/"+d).list();
			

			
			for(String XMLFile : listXMLFiles)
			{
		
				File fXmlFile = new File("subindex/2015/"+d+"/"+XMLFile);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(fXmlFile);
				
				doc.getDocumentElement().normalize();
				NodeList nList = doc.getElementsByTagName("doc");
				
				NodeList date = doc.getElementsByTagName("docs");
				String dateString = ((Element) date.item(0)).getAttribute("date");
				
				
				for (int i = 0; i < nList.getLength(); i++) 
				{
					Node nNode = nList.item(i);
					Element eElement = (Element) nNode;			
					
					listDocuments.add(dateString+"_"+eElement.getAttribute("id")+".txt");
				}
			}
		}
	}


	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		
		getDocumentsNames();
		System.out.println("Number of documents = " + listDocuments.size());

	}

}
