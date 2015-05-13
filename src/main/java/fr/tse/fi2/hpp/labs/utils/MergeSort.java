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
			tab [i+1]= element;
		}
		return tab;
		
	}
	
	public static int[] merge(int [] tab1, int [] tab2 ){
	
		//int[] result =new int[tab1.length+tab2.length];
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

	public static int[] sort(int[] tab){
		if(tab.length==1||tab.length==0){
			return tab;
		}
		else{
			int[] sup=new int[tab.length-tab.length/2];
			int [] inf= new int[tab.length/2];
			for(int i=0;i<tab.length/2;i++){
				inf[i]=tab[i];
			}
			int j=0;
			for(int i=(tab.length/2);i<tab.length;i++){
				sup[j]=tab[i];
				j++;
				
			}
			
			return merge(sort(inf),sort(sup));
		}
	}
	
	public static void main(String[] args) {
		int[] tab = {8,5,3,2,1};
		
	
		System.out.println(Arrays.toString(tab));
		//sort(tab);
		System.out.println(Arrays.toString(sort(tab)));
		
		 
		 
			
		
	}

}
