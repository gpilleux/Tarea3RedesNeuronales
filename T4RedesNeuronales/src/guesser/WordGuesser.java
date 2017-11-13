package guesser;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import embedding.WordEmbedding;

public class WordGuesser {
	
	Word[] wordPopulation;
	Word[] newWordPopulation;
	
	List<Double[]> expressionVects;
	
	/*
	 * wordRepresentations : vectors of the expression
	 * numbPopulation: how many words in the population
	 * wordDim: dimension of each representing vector
	 */
	public WordGuesser(List<Double[]> wordRepresentations, int numbPopulation, int wordDim){
		this.expressionVects = wordRepresentations;
		this.wordPopulation = Word.generateIndividuals(numbPopulation, wordDim);
		this.newWordPopulation = new Word[numbPopulation];
		this.evaluateFitness();
	}
	
	public void evaluateFitness(){
		double totalFitness = 0;
		for(Word ind : wordPopulation){
			double tempoFitness = 1.0 / calculateSimilarity(ind.repVec);
			totalFitness += tempoFitness;
			ind.fitnessValue = tempoFitness;
			
			//System.out.println("countsameGenes: " + tempoFitness);
		}
	}
	
	//caculate similarity between word repvect and 3 other words stored in expressionVects
	public double calculateSimilarity(double[] repVec){
		return WordEmbedding.expressionSimilarity(repVec, this.expressionVects);
	}
	
	public Word selection(){
		Arrays.sort(this.wordPopulation, (a,b) -> a.fitnessValue > b.fitnessValue ? -1 : 1);
		/*
		System.out.println("Population sorted by fitness: ");/*
		for(Individual i : population)
			System.out.print(i.fitnessValue + " ");
		
		for(Individual in : this.population){
			for(int c : in.genes)
				System.out.print(c + " ");
			System.out.println("fitness: " + in.fitnessValue);
		}
		//System.out.println();
		*/
		double[] accFitness = accumulatedFitness();
		/*
		System.out.println();
		System.out.println("Accumulated Fitness: ");
		
		for(double a : accFitness)
			System.out.print(a + " ");
		System.out.println();
		*/
		double R = ThreadLocalRandom.current().nextDouble();
		
		//System.out.println("R: " + R);
		
		int index = 0;
		// the last index whose accumulated normalized value is smaller than R is index-1
		for(; index<accFitness.length-1; index++){
			if(R < accFitness[index+1] && R >= accFitness[index])
				break;
		}
		return this.wordPopulation[index];
	}
	
	public double[] accumulatedFitness(){
		Arrays.sort(this.wordPopulation, (a,b) -> a.fitnessValue > b.fitnessValue ? -1 : 1);
		double[] accFitness = new double[this.wordPopulation.length];
		accFitness[0] = this.wordPopulation[0].fitnessValue;
		for(int i=1; i<this.wordPopulation.length; i++){
			accFitness[i] = accFitness[i-1] +  this.wordPopulation[i].fitnessValue;
		}
		return accFitness;
	}
	
	public void reproduction(){
		//repeat until new population number is met (same as before's population)
		for(int i=0; i<this.wordPopulation.length; i++){
			Word p1 = this.selection();
			Word p2 = this.selection();
			/*
			System.out.println("Selected1 ");
			for(int g : p1.genes)
				System.out.print(g + " ");
			System.out.println("Selected2 ");
			for(int g : p2.genes)
				System.out.print(g + " ");
			*/
			Word offSpring = new Word(this.expressionVects.get(0).length);
			int crossOverSpot = (int)(this.expressionVects.get(0).length* ThreadLocalRandom.current().nextDouble(1));
			
			//System.out.println("crossOverSpot: " + crossOverSpot);
			
			//copy first random spotted genes into offspring
			for(int firstGenes=0; firstGenes<crossOverSpot; firstGenes++)
				offSpring.repVec[firstGenes] = p1.repVec[firstGenes];
			//copy second random spotted gened into offspring
			for(int secondGenes=crossOverSpot; secondGenes<this.expressionVects.get(0).length; secondGenes++)
				offSpring.repVec[secondGenes] = p2.repVec[secondGenes];
			//mutate
			double randomMutate = ThreadLocalRandom.current().nextDouble(1);
			if(randomMutate < 0.2)
				this.mutate(offSpring);
			//add the offspring to the new population
			this.newWordPopulation[i] = offSpring;
		}
		
		//change previous population with new population IF AND ONLY IF the new population are't all equal
		if(!this.everyEqualOffSpring())
			this.rotatePopulation();
		//this.verifyCode();
		/*
		System.out.println("Population sorted by fitness: ");
		for(Individual in : this.population){
			for(int c : in.genes)
				System.out.print(c + " ");
			System.out.println("fitness: " + in.fitnessValue);
		}
		*/
	}
	
	public void mutate(Word ind){
		int firstGene = ThreadLocalRandom.current().nextInt(0, ind.repVec.length);
		int secondGene = ThreadLocalRandom.current().nextInt(0, ind.repVec.length);
		double geneAux = ind.repVec[firstGene];
		ind.repVec[firstGene] = ind.repVec[secondGene];
		ind.repVec[secondGene] = geneAux;
	}
	
	//after population rotation, needs to be fitness evaluated AGAIN!
	public void rotatePopulation(){
		this.wordPopulation = this.newWordPopulation;
		this.newWordPopulation = new Word[this.wordPopulation.length];
		this.evaluateFitness();
	}
	
	//checks if members of the NEW population are all the same
	public boolean everyEqualOffSpring(){
		int countSameCombs = 0;
		double[] firstComb = this.newWordPopulation[0].repVec;
		for(Word ind : this.newWordPopulation){
			if(isSameComb(firstComb, ind.repVec))
				countSameCombs++;
			else
				return false;
		}
		return countSameCombs == this.newWordPopulation.length;
	}
	//checks if the codes are the same
	public boolean isSameComb(double[] firstComb, double[] secondComb){
		int sameCode = 0;
		for(int i=0; i<firstComb.length; i++){
			if(firstComb[i] == secondComb[i])
				sameCode++;
		}
		return sameCode == this.expressionVects.get(0).length;
	}
	
	public Word chosenWord(){
		Arrays.sort(this.wordPopulation, (a,b) -> a.fitnessValue > b.fitnessValue ? -1 : 1);
		for(Word w : this.wordPopulation)
			System.out.println(w.fitnessValue);
		return this.wordPopulation[0];
	}
	
	//checks if members of the population are all the same
	public boolean everyEqualComb(){
		int countSameCombs = 0;
		double[] firstComb = this.wordPopulation[0].repVec;
		for(Word ind : this.wordPopulation){
			if(isSameComb(firstComb, ind.repVec))
				countSameCombs++;
			else
				return false;
		}
		return countSameCombs == this.wordPopulation.length;
	}
}
