package guesser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.sphx.api.SphinxException;

import embedding.WordEmbedding;

public class MainGuesser {

	public static void main(String[] args) throws SphinxException {
		
		Scanner reader = new Scanner(System.in);  // Reading from System.in
		System.out.println("Choose a file example (rey, pais, poem): ");
		String folder = reader.nextLine(); // Scans the next token of the input as an string.
		//once finished
		reader.close();
		/*
		 * read from a txt the 3 words being used to identify X
		 * parse them to double[] and give them to the WordGuesser
		 * let it run for i iterations
		 * write into txt the 5 offsprings that had the greatest fitness value
		 */
		
		System.out.println("Starting...");
		
		WordEmbedding we = new WordEmbedding();
		//String sentence = "rey";
		String[] words = new String[3];
		List<Double[]> wordVecRep = new ArrayList<Double[]>();
		
		Scanner scan = null;
		try{
			scan = new Scanner(new File("C:/Users/GPG/workspace/T4RedesNeuronales/"+folder+"/similar_words.txt"));
			
			int n = 0;
			while(scan.hasNextLine()){
				words[n] = scan.nextLine();
				//System.out.println(words[n]);
				//System.out.println(scan.nextLine());
				n++;
			}
			System.out.println(n);
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			scan.close();
		}
		//parse to Double[]
		wordVecRep = we.wordsToDouble(words);
		
		WordGuesser wg = new WordGuesser(wordVecRep, 10, 300);
		//wg.evaluateFitness(); // implemented in constructor
		
		//genetic algorithm -> call WordGuesser
		for(int i=0; i<1000000; i++){
			wg.reproduction();
			
			if(wg.everyEqualComb())
				break;
		}
		
		Word chosen = wg.chosenWord();
		 
		//write into txt chosen.repVec
		double[] repVec = chosen.repVec;
		//String s = StringUtils.join(repVec, ",");
		String strVecRep = "";
		for(double d : repVec){
			strVecRep += String.valueOf(d) + " ";
		}
		
		BufferedWriter writer = null;
		try{
			File logFile = new File(folder+"/offspring.txt");
			writer = new BufferedWriter(new FileWriter(logFile));
			writer.write(strVecRep);
			writer.newLine();
		}catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
				// Close the writer regardless of what happens...
	            writer.close();
	        } catch (Exception e) {
	        }
	    }
		
		System.out.println("...Finished");
	}

}
