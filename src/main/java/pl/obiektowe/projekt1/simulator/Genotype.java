package pl.obiektowe.projekt1.simulator;

import java.util.Arrays;
import java.util.Random;

public class Genotype {
    private static int size = 32;
    private static int numberOfGens = 8;
    private int[] genes;

    Genotype(){
        this.genes = generateRandomGenes();
    }

    Genotype(int [] genes){
        this.genes = genes;
    }

    public static int[] generateRandomGenes(){
        int [] numberOfGenesOfType = new int[numberOfGens];
        for(int i = 0; i < numberOfGenesOfType.length; i++){
            numberOfGenesOfType[i] = 1; //set all types at 1
        }
        Random generator = new Random();
        int index;
        for(int i = 0; i < 24; i++){
            index = generator.nextInt(numberOfGens);
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

    public Genotype createNewGenotypeWithSecondParent(Genotype parent2){
        Random generator = new Random();
        int firstIndex = generator.nextInt(size);
        int secondIndex = generator.nextInt(size);
        while(secondIndex == firstIndex)
            secondIndex = generator.nextInt(size);
        int [] genesOfSecondParent = parent2.getGenes();
        int [] firstPartOfGenes;
        int [] secondPartOfGenes;
        if (firstIndex < secondIndex) {
            firstPartOfGenes = Arrays.copyOfRange(this.genes, 0, firstIndex);
            secondPartOfGenes = Arrays.copyOfRange(genesOfSecondParent, firstIndex + 1, size - 1);
        } else {
            firstPartOfGenes = Arrays.copyOfRange(genesOfSecondParent, 0, secondIndex);
            secondPartOfGenes = Arrays.copyOfRange(this.genes, secondIndex + 1, size - 1);
        }
        int [] genesOfChildren = new int[size];
        for(int i = 0; i < size; i++){
            if(i<firstPartOfGenes.length){
                genesOfChildren[i] = firstPartOfGenes[i];
            }
            else{
                genesOfChildren[i] = secondPartOfGenes[i - firstPartOfGenes.length];
            }
        }
        if(!isGenotypeRight(genesOfChildren)){
            genesOfChildren = repairGenotype(genesOfChildren);
        }
        return new Genotype(genesOfChildren);
    }

    public int [] repairGenotype(int [] wrongGenotype){
        int [] numberOfGenType = new int[numberOfGens];
        int [] result = wrongGenotype;
        for(int i = 0; i < wrongGenotype.length; i++){
            numberOfGenType[wrongGenotype[i]]++;
        }
        Random generator = new Random();
        for(int i = 0; i < numberOfGens; i++){
            if(numberOfGenType[i] == 0){
                //wylosuj taki rodzaj genów którego jest więcej niż jeden, więc go nie zabraknie
                int kindOfGenWithAtLeast2Elements;
                do{
                    kindOfGenWithAtLeast2Elements = generator.nextInt(numberOfGens);
                }while(numberOfGenType[kindOfGenWithAtLeast2Elements] <= 1);
                int indexOfChangingGen = 0;
                int index = 0;
                while(index <= kindOfGenWithAtLeast2Elements){
                    indexOfChangingGen += numberOfGenType[index];
                    index++;
                }
                result[indexOfChangingGen - 1] = i;
            }
        }
        Arrays.sort(result);
        if(!isGenotypeRight(result)){
            result = repairGenotype(result);
        }
        return result;
    }

    public boolean isGenotypeRight(int [] genotype){
        if(genotype[genotype.length - 1] != numberOfGens - 1)
            return false;
        for(int i = 1; i < genotype.length;){
            if(genotype[i] == genotype[i-1] || genotype[i] == genotype[i-1] + 1)
                return false;
        }
        return true;
    }

    public int[] getGenes() {
        return genes;
    }
}
