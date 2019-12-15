package pl.obiektowe.projekt1.simulator;

import java.util.Arrays;
import java.util.Random;

public class Genotype {
    private static int numberOfGenes = 32;

    private int[] genes;

    Genotype(){
        this.genes = generateRandomGenes();
    }

    Genotype(int [] genes){
        this.genes = genes;
    }

    public static int[] generateRandomGenes(){
        int [] numberOfGenesOfType = new int[8];
        for(int i = 0; i < numberOfGenesOfType.length; i++){
            numberOfGenesOfType[i] = 1; //set all types at 1
        }
        Random generator = new Random();
        int index;
        for(int i = 0; i < 24; i++){
            index = generator.nextInt(8);
            numberOfGenesOfType[index]++;
        }
        int [] result = new int[32];
        for(int i = 0, j = 0; i < numberOfGenesOfType.length; i++){
            while(numberOfGenesOfType[i]-- > 0){
                result[j] = i;
                j++;
            }
        }
        return result;
    }

    public int [] createNewGenotypeWithSecondParent(Genotype parent2){
        Random generator = new Random();
        int firstIndex = generator.nextInt(numberOfGenes);
        int secondIndex = generator.nextInt(numberOfGenes);
        while(secondIndex == firstIndex)
            secondIndex = generator.nextInt(numberOfGenes);
        int [] genesOfSecondParent = parent2.getGenes();
        int [] firstPartOfGenes;
        int [] secondPartOfGenes;
        if (firstIndex < secondIndex) {
            firstPartOfGenes = Arrays.copyOfRange(this.genes, 0, firstIndex);
            secondPartOfGenes = Arrays.copyOfRange(genesOfSecondParent, firstIndex + 1, numberOfGenes - 1);
        } else {
            firstPartOfGenes = Arrays.copyOfRange(genesOfSecondParent, 0, secondIndex);
            secondPartOfGenes = Arrays.copyOfRange(this.genes, secondIndex + 1, numberOfGenes - 1);
        }
        int [] genesOfChildren = new int[numberOfGenes];
        for(int i = 0; i < numberOfGenes; i++){
            if(i<firstPartOfGenes.length){
                genesOfChildren[i] = firstPartOfGenes[i];
            }
            else{
                genesOfChildren[i] = secondPartOfGenes[i - firstPartOfGenes.length];
            }
        }
        return genesOfChildren;
    }

    public int[] getGenes() {
        return genes;
    }
}
