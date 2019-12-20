package pl.obiektowe.projekt1.simulator.Classes;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import pl.obiektowe.projekt1.simulator.Classes.Genotype;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GenotypeTest {

    @Test
    void isGenotypeRightShouldReturnFalseForGenotypeWithoutAllKindsOfGens(){
        int [] nonRightGenotype = new int[32];
        for(int i = 0; i < 32; i++){
            nonRightGenotype[i]=1;
        }
        assertFalse(Genotype.isGenotypeRight(nonRightGenotype));
    }

    @Test
    void isGenotypeRightShouldReturnTrueForGenotypeWithAllKindsOfGens(){
        int [] rightGenotype = new int[32];
        for(int i = 0; i < 32; i++){
            rightGenotype[i] = i % 8;
        }
        Arrays.sort(rightGenotype);
        System.out.println(Arrays.toString(rightGenotype));
        assertTrue(Genotype.isGenotypeRight(rightGenotype));
    }

    @RepeatedTest(10)
    void randomlyCreatedGenotypeContainsAllTypesOfGenes(){
        Genotype gens = new Genotype();
        assertTrue(gens.isGenotypeRight(gens.getGenes()));
    }

    @Test
    void nonRightGenotypeShouldBeRepairedByRepairGenotype(){
        int [] nonRightGenotype = {0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2,2,2,2,4,6,6,7}; //have no 3 and 5
        Genotype withNonRightGenotyp = new Genotype(nonRightGenotype);
        withNonRightGenotyp.repairGenotype();
        assertTrue(Genotype.isGenotypeRight(withNonRightGenotyp.getGenes()));
    }

    @RepeatedTest(10)
    void methodForReproductionCreateRightGenotypeForChild() {
        //given
        Genotype parent1 = new Genotype();
        Genotype parent2 = new Genotype();
        //when
        Genotype child = parent1.createNewGenotypeWithSecondParent(parent2);
        System.out.println(Arrays.toString(child.getGenes()));
        //then
        assertTrue(Genotype.isGenotypeRight(child.getGenes()));
    }
}