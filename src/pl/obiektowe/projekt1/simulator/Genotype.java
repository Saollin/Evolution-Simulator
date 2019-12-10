package pl.obiektowe.projekt1.simulator;

import java.util.Random;

public class Genotype {
    private static int numberOfGenes = 32;

    private int[] genes = new int[numberOfGenes];
    private int indexEndingFirstGenesGroup;
    private int indexEndingSecondGenesGroup;

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
}
