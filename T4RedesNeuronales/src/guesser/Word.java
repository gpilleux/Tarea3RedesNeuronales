package guesser;

import java.util.concurrent.ThreadLocalRandom;

public class Word {
	
	double fitnessValue;
	double[] repVec;
	
	public Word(int numberCells){
		this.fitnessValue = 0;
		this.repVec = new double[numberCells];
	}
	
	public static Word[] generateIndividuals(int population, int numberGenes){
		Word[] ind = new Word[population];
		//initialize individuals
		for(int i=0; i<population; i++){
			ind[i] = new Word(numberGenes);
		}
		//initialize genes for each individual, random ints [0,9]
		for(Word i : ind){
			for(int g=0; g<numberGenes; g++){
				i.repVec[g] = ThreadLocalRandom.current().nextDouble(-1, 2);
			}
		}
		return ind;
	}
	
	
}
