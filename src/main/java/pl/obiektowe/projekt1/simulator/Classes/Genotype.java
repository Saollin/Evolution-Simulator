package pl.obiektowe.projekt1.simulator.Classes;

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
        for(int i = 0; i < firstPartOfGenes.length; i++){
            genesOfChildren[i] = firstPartOfGenes[i];
        }
        for(int i = 0; i < secondPartOfGenes.length; i++){
            genesOfChildren[i + firstPartOfGenes.length] = secondPartOfGenes[i];
        }
        Genotype child = new Genotype(genesOfChildren);
        if(!isGenotypeRight(genesOfChildren)){
            child.repairGenotype();
        }
        return child;
    }

    public void repairGenotype(){
        int [] numberOfGenType = new int[numberOfGens];
        int [] resultGens = this.genes;
        for(int i = 0; i < this.genes.length; i++){
            numberOfGenType[this.genes[i]]++;
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
                resultGens[indexOfChangingGen - 1] = i;
            }
        }
        Arrays.sort(resultGens);
        this.genes = resultGens;
        if(!isGenotypeRight(resultGens)){
            this.repairGenotype();
        }
    }

    public static boolean isGenotypeRight(int [] genotype){
        if(genotype[genotype.length - 1] != numberOfGens - 1)
            return false;
        for(int i = 1; i < genotype.length; i++){
            if(genotype[i] != genotype[i-1] && genotype[i] != genotype[i-1] + 1)
                return false;
        }
        return true;
    }

    public int randomGen(){
        Random generator = new Random();
        return this.genes[generator.nextInt(size)];
    }

    public int[] getGenes() {
        return genes;
    }

    @Override
    public String toString() {
        return Arrays.toString(genes);
    }
}
