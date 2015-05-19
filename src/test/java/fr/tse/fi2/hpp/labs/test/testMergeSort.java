package fr.tse.fi2.hpp.labs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.junit.Test;

import fr.tse.fi2.hpp.labs.utils.MergeSort;



public class testMergeSort {

	@Test
	public void test() {
		
		int[] tab = new int[100000];
		int[] tab2 = new int[100000];
		
		int taille = 10000;
		Random rand = new Random();
		for(int i=0;i<tab.length;i++){
			tab[i]=rand.nextInt();
		}
				
		tab2 = tab;
		
		Arrays.sort(tab);
		
		int[] listeTrieFusion = MergeSort.sort(tab2);
		
		assertArrayEquals(tab, listeTrieFusion);
	}
	


}