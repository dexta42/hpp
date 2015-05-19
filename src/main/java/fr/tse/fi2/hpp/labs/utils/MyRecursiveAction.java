package fr.tse.fi2.hpp.labs.utils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

import cern.colt.Arrays;

public class MyRecursiveAction extends RecursiveAction {

    private int[] tab={};
    int[] sup;
	int [] inf;

    public MyRecursiveAction(int[] tab) {
        this.tab = tab;
    }

    @Override
    protected void compute() {

        //if work is above threshold, break tasks up into smaller tasks
        if(this.tab.length > 2) {
            //System.out.println("Splitting workLoad : " + this.workLoad);

            List<MyRecursiveAction> subtasks =
                new ArrayList<MyRecursiveAction>();

            subtasks.addAll(createSubtasks());

            for(RecursiveAction subtask : subtasks){
                subtask.fork();
            }
            for(RecursiveAction subtask : subtasks){
                subtask.join();
            }
            //merge
            System.out.println(Arrays.toString(inf) + " " + Arrays.toString(sup));
            this.tab = merge(sup,inf);
            System.out.println(Arrays.toString(this.tab));
           
            
        } else {
            //System.out.println("Doing workLoad myself: "  );
        }
    }
    
    
    public int[] getTab() {
		return tab;
	}

	

	private List<MyRecursiveAction> createSubtasks() {
        List<MyRecursiveAction> subtasks =
            new ArrayList<MyRecursiveAction>();
        
        sup=new int[tab.length-tab.length/2];
        inf= new int[tab.length/2];
        
		for(int i=0;i<tab.length/2;i++){
			inf[i]=tab[i];
		}
		int j=0;
		for(int i=(tab.length/2);i<tab.length;i++){
			sup[j]=tab[i];
			j++;
			
		}

        MyRecursiveAction subtask1 = new MyRecursiveAction(inf);
        MyRecursiveAction subtask2 = new MyRecursiveAction(sup);

        subtasks.add(subtask1);
        subtasks.add(subtask2);

        return subtasks;
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
				tab2=insert(tab1[i], tab2);
			}
			
		}
		return tab2;
		
	}
	
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
	
	public static void main(String[] args) {
		int[] tab = {8,5,3,2,1};
		 MyRecursiveAction m = new  MyRecursiveAction(tab);
		
		m.compute();
	
		//System.out.println(Arrays.toString(m.getTab()));
		
		
		 
		 
			
		
	}

}