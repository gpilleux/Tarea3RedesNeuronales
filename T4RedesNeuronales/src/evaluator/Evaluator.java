package evaluator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import embedding.WordEmbedding;

public class Evaluator {

	public static void main(String[] args) {
		
		Scanner reader = new Scanner(System.in);  // Reading from System.in
		System.out.println("Choose a file example (rey, pais, poem): ");
		String folder = reader.nextLine(); // Scans the next token of the input as an string.
		//once finished
		reader.close(); 
		
		//String folder = "pais";
		//String folder = "rey";
		Scanner scan = null;
		Scanner scan1 = null;
		
		WordEmbedding we = new WordEmbedding();
		
		Map<Double, String> dictionary = new HashMap<Double, String>();
		
		int n = 0;
		try{
			scan1 = new Scanner(new File("C:/Users/GPG/workspace/T4RedesNeuronales/"+folder+"/num_words.txt"));
			if(scan1.hasNextLine()){
				n = Integer.valueOf(scan1.nextLine());
			}
			System.out.println("total of " + n + " words.");
		
		}
		catch(Exception e){
			System.out.println(e);
		}
		finally{
			scan1.close();
		}
		
		
		String[] words = new String[n];
		String[] strRepVects = new String[n];
		List<Double[]> repVects = new ArrayList<Double[]>();
		double[] simArray = new double[n];
		
		//read words
		
		try{
			scan = new Scanner(new File("C:/Users/GPG/workspace/T4RedesNeuronales/"+folder+"/dictionary.txt"));
			int i = 0;
			while(scan.hasNextLine()){
				words[i] = scan.nextLine();
				//System.out.println(words[n].split(" ").length);
				i++;
			}
			System.out.println("read " + i + " from words.");
		
		}
		catch(Exception e){
			System.out.println(e);
		}
		finally{
			scan.close();
		}
		
		//read representing vectors
		try{
			scan = new Scanner(new File("C:/Users/GPG/workspace/T4RedesNeuronales/"+folder+"/vectors.txt"));
			
			int j = 0;
			while(scan.hasNextLine()){
				strRepVects[j] = scan.nextLine();
				//System.out.println(words[n].split(" ").length);
				j++;
			}
			System.out.println("read " + j + " from vectors.");
		
		}
		catch(Exception e){
			System.out.println(e);
		}
		finally{
			scan.close();
		}
		
		
		List<Double[]> offspringVecRep = new ArrayList<Double[]>();
		//read offspring vector
		try{
			scan = new Scanner(new File("C:/Users/GPG/workspace/T4RedesNeuronales/"+folder+"/offspring.txt"));
			
			int k = 0;
			if(scan.hasNextLine()){
				offspringVecRep = we.wordsToDouble(new String[]{scan.nextLine()});
				k++;
			}
			System.out.println("read " + k + " from offspring.");
		
		}
		catch(Exception e){
			System.out.println(e);
		}
		finally{
			scan.close();
		}
		
		repVects = we.wordsToDouble(strRepVects);
		
		//calculate similarity between offspringVecRep and each repVects
		for(int i=0; i<repVects.size(); i++){
			simArray[i] = we.cosineSimilarity(offspringVecRep.get(0), repVects.get(i));
		}
		
		//Maping put similarity -> words
		for(int i=0; i<words.length; i++){
			dictionary.put(simArray[i], words[i]);
		}
		
		Arrays.sort(simArray);
		
		for(double d : simArray)
			System.out.println(d + " " + dictionary.get(d));
		
		
		//write 3 words most accurate and 3 words most different
		BufferedWriter writer = null;
		try{
			File logFile = new File(folder+"/results.txt");
			writer = new BufferedWriter(new FileWriter(logFile));
			
			writer.write("5 palabras mas parecidas:");
			writer.newLine();
			for(int i=0; i<5; i++){
				writer.write(dictionary.get(simArray[simArray.length - 1 - i]));
				writer.newLine();
			}
			writer.write("------------o------------");
			writer.newLine();
			writer.write("5 palabras menos parecidas");
			writer.newLine();
			for(int i=0; i<5; i++){
				writer.write(dictionary.get(simArray[i]));
				writer.newLine();
			}
		}catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
				// Close the writer regardless of what happens...
	            writer.close();
	        } catch (Exception e) {
	        }
	    }
		
	}

}
