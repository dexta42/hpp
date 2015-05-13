package fr.tse.fi2.hpp.labs.utils;

import java.util.ArrayList;
import java.util.Random;

import cern.colt.Arrays;

public class MergeSort {
	
	public static int[]  insert(int element, int[] séquence ){
		int[] tab = new int[séquence.length+1];
		if(séquence.length==0){
			tab[0]= element;
			for(int j=0;j<séquence.length;j++){
				tab[j+1]=séquence[j];
			}
			return tab;
			
		}
		
		for(int i =0;i<séquence.length;i++){
			if(element<=séquence[i]){
				tab[i]=element;
				for(int j=i;j<séquence.length;j++){
					tab[j+1]=séquence[j];
				}
				
				
				return tab ;
			}
			else{
				tab[i]=séquence[i];
			}
		}
		return tab;
		
	}
	
	public static int[] merge(int [] tab1, int [] tab2 ){
		if(tab1.length==0){
			return tab2;
		}
		else if (tab2.length==0){
			return tab1;
		}
		else{
			for(int i=0;i<tab1.length;i++){
				tab2=MergeSort.insert(tab1[i], tab2);
			}
			
		}
		return tab2;
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] tab = new int[5] ;
		
		int[] tab2 = new int[5] ;
		Random rand = new Random();
		
		for(int i =0; i<5;i++){
			
			tab[i]= rand.nextInt(101);
			tab2[i]= rand.nextInt(101);
			
		}
		
		System.out.println(Arrays.toString(tab));
		System.out.println(Arrays.toString(tab2));
		
		System.out.println(Arrays.toString(MergeSort.merge(tab, tab2)));
		 
		 
			
		
	}

}
