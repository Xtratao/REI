
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import tools.FrenchStemmer;
import tools.Normalizer;

public class GetDocumentsFromInvertedFile {
	
	private static TreeMap<String, TreeMap<String,Integer>> invertedFile;

	public static void loadInvertedFile(Normalizer normalizer) throws IOException {
		invertedFile = Indexation.getInvertedFileWithTfs(ReadXMLFile.listDocuments, normalizer);		
	}

	public static ArrayList<TreeSet<String>> getListOfDocumentsLists(File requete, Normalizer normalizer) throws IOException
	{
		loadInvertedFile(normalizer);
		ArrayList<TreeSet<String>> listOfDocumentsLists = new ArrayList<TreeSet<String>>();
		ArrayList<String> motsRequete = normalizer.normalize(requete);
		Map<String, Integer> map = new HashMap<String, Integer>();
		
		
		TreeSet<String> documentList=null; 
		for (String mot : motsRequete)
		{
			if (invertedFile.containsKey(mot))
				{
					documentList = new TreeSet<String>();
					for (Map.Entry<String, Integer> elt : invertedFile.get(mot).entrySet())
						documentList.add(elt.getKey());					
				}
			
			if(documentList!=null)			
				listOfDocumentsLists.add(documentList);
		}
		
		return listOfDocumentsLists;
		
	}

	
	
	
	public static TreeSet<String> inter(ArrayList<TreeSet<String>> listOfTreeSets) {
		
		TreeSet<String> tree = new TreeSet<String>();
		
		Map<Integer, String> tmp = new HashMap<Integer, String>();

		int i = 0;
		for (TreeSet<String> treeSet : listOfTreeSets)
			if(!treeSet.isEmpty())
				tmp.put(i++, treeSet.first());
		
		Map<Integer, String> sortedMap = new HashMap<Integer, String>();
		
		Map.Entry<Integer, String> entry = tmp.entrySet().iterator().next();
		
		int lenMap = 0;
		String[] values = null;
		int j =0;
		boolean bool = true;
		while (bool) {// while lists are not Empty			
		
			sortedMap = SortByValue.sortByComparatorString(tmp, SortByValue.DESC);
			lenMap = sortedMap.size();
			values = sortedMap.values().toArray(new String[lenMap]);
			if(values[0].equals(values[lenMap-1])){
				tree.add(values[0]);
				i = 0;
				tmp.clear();
				for (TreeSet<String> treeSet : listOfTreeSets) {
					
					treeSet.remove(treeSet.first());
					
					if(!treeSet.isEmpty()){
						tmp.put(i++, treeSet.first());
					}else{
						bool = false;
						break;
					}	
				}
				
			}else{

				i = 0;
				for (TreeSet<String> treeSet : listOfTreeSets) {
					if(!treeSet.first().equals(values[0])){
						treeSet.remove(treeSet.first());
						tmp.remove(i);
						if(!treeSet.isEmpty()){
							tmp.put(i, treeSet.first());
						}else{
							bool = false;
							break;
						}	
					}
					i++;
				}			
			}
		}

		return tree;
	}

		

}
