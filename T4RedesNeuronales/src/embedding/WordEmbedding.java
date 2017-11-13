package embedding;

import java.util.ArrayList;
import java.util.List;

import embedding.word.sphinx.util.WordEmbeddingSphinxAdapter;

public class WordEmbedding {
	
	//private dictionary cache;
	
	public WordEmbedding(){
		//use in static way
		//maybe with the cache, not static and need to be initialized
	}
	/*
	//returns a list with the representation of each word in Double[] format
	public List<Double[]> wordsToRepresentation(String[] words){
		//get the words from the DB
		List<String> wordsFromDB = this.consultWordsDB(words);
		//parse the words from String[] to Double[]
		List<Double[]> vectorReps = this.wordsToDouble(wordsFromDB);
		
		return vectorReps;
	}
	*/
	
	//query the DB for the words needed. Uses the SphinxAdapter
	public List<String> consultWordsDB(String[] words){
		List<String> wordsFromDB = null;
		try{
			wordsFromDB = WordEmbeddingSphinxAdapter.getWordsVectorList(words);
			
		} catch (Exception e) {
				e.printStackTrace();
		}
		
		return wordsFromDB;
	}
	
	public List<Double[]> wordsToDouble(String[] wordsFromDB) {
		List<Double[]> vectorReps = new ArrayList<Double[]>();
		//for every vectorRep in wordsFromDB as String, parse to Double[]
		if(wordsFromDB.length > 0){
			for(String s : wordsFromDB){
				//System.out.println(s);
				String[] separatedWords = s.split(" ");
				Double[] eachWordRep = new Double[separatedWords.length];
				for(int i=0; i<separatedWords.length; i++){
					eachWordRep[i] = Double.parseDouble(separatedWords[i]);
				}
				vectorReps.add(eachWordRep);
				//System.out.println(separatedWords.length);
			}
		}
		//System.out.println(vectorReps.size());
		return vectorReps;
	}
	
	public static double expressionSimilarity(double[] xVec, List<Double[]> expressionVec){
		if(expressionVec.size() == 3){
			double[] resultVect = new double[xVec.length];
			//calculates relation [X - w1 = w2 - w3] => X - w1 - w2 + w3 = 0
			for(int i=0; i<xVec.length; i++){
				resultVect[i] = xVec[i] - expressionVec.get(0)[i] - expressionVec.get(1)[i] + expressionVec.get(2)[i];
			}
			double result = 0;
			//calculates norm of resultVect
			for(int i=0; i<xVec.length; i++)
				result += Math.pow(resultVect[i], xVec.length);
			// raiz '300'-ava
			result = Math.pow(result, 1.0/xVec.length);
			
			return result;
			
		}else{
			return 0;
		}
	}
	
	public double cosineSimilarity(Double[] vectorA, Double[] vectorB) {
	    double dotProduct = 0.0;
	    double normA = 0.0;
	    double normB = 0.0;
	    for (int i = 0; i < vectorA.length; i++) {
	        dotProduct += vectorA[i] * vectorB[i];
	        normA += Math.pow(vectorA[i], 2);
	        normB += Math.pow(vectorB[i], 2);
	    }   
	    return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
	}

}
