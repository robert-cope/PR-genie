//import java.math.*;
import java.util.*;
import java.io.*;





//
//    Copyright 2013 Robert Cope cope.robert.c@gmail.com
//
//    This file is part of PR-genie.
//
//    PR-genie is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    PR-genie is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with PR-genie.  If not, see <http://www.gnu.org/licenses/>.
//






/**
 *
 * The overarching framework of the program - initiates, runs, etc.
 */
public class PrGenie {
	public static void main(String[] args){
		try{
	    	String direct = "";
        System.out.println(args[0]);
        System.out.println(args[1]);
        System.out.println(args[2]);	
	    	String first = args[0]+args[1]+args[2];
	    	String second = args[0]+args[1]+args[2];
	    	System.out.println(first);
	    	//System.exit(0);
	    	System.out.println(direct+first+"-out-reduced.txt");

		File file = new File(direct+first+"-out-reduced.txt");
		FileOutputStream fos = new FileOutputStream(file);
		PrintStream ps = new PrintStream(fos);
		File fileOther = new File(direct+first+"-currentPos.txt");
		FileOutputStream fosOther = new FileOutputStream(fileOther);
		PrintStream psO = new PrintStream(fosOther);
		System.setOut(ps);


		mainRun(first,second);
		psO.println(first + " "+ second + "\n");
		} catch (Exception e) {
			
		}
	}
    public static void mainRun(String first, String second) {
    	String direct = "";
    	long start = System.currentTimeMillis();
        try {
            //initialization - first a pedigree reader to read in the test
            //pedigree
            List<List<ChildParentsTriple>> finalPedigrees = new ArrayList<List<ChildParentsTriple>>();
            PedigreeReader PR = new PedigreeReader(direct+
                    first +
                    ".xml");
            System.out.println("pedigree reader created");
            Pedigree P = new Pedigree(); //create a pedigree object
            System.out.println("pedigree created");
            PR.getPedigree(P); //reads the data from the reader into the
            //pedigree
            System.out.println("end");
            //OrderRestriction OR = new OrderRestriction(P.N);
            //OR.addRestriction(P.names.indexOf(17),P.names.indexOf(29));
            //OR.addRestriction(P.names.indexOf(43),P.names.indexOf(80));
            P.initAFreqsList();//important - initializes the allele frequency
            P.fillAlleleFreqs();
            //P.printAlleleFreqs();
            Collections.sort(P.names);
            
            
            Random rnd = new Random(19372); //intitializes a RNG
            int nsamples = 300; //change these to determine how deep the search is
            int numMaxCEsteps = 50;
            
            
            System.out.println("samples: "+nsamples);
            System.out.println("maxSteps: "+numMaxCEsteps);
            Distribution.N = P.N;
            Individual.N = P.N;
            ChildParentsTriple.N = P.N;
            DistributionInitializer.N = P.N;
            StatePermutation.N = P.N;
            Distribution D = new Distribution(rnd, P);
            //D.giveOR(OR);
            //creates a distribution, defaultly (ie. everything mean 0.0)
            //D.printDist();

            //intitializes the CE population
            List<StatePermutation> splist = new ArrayList<StatePermutation>();
            for (int i = 0; i < nsamples; i++) {
                try {
                    StatePermutation sp = new StatePermutation(
                            D.getState(P.names), rnd);
                    sp.giveScore(P.maxPedigree(sp, 0, 0,finalPedigrees));
                    splist.add(sp); //adds to the CE population from the
                    //distribution, gets scores
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            int howManyMax = CEsort.sort(splist,(int)Math.floor(nsamples*0.15),0,splist.size()-1);

            Iterator<StatePermutation> sIt = splist.iterator();
            int i = 0;
            List<StatePermutation> splistElite =
                    new ArrayList<StatePermutation>();
            while (sIt.hasNext() && i < howManyMax) {
                StatePermutation s = sIt.next();
                splistElite.add(s);                i++;
            }
            /////////////////////////////////////////
            DistributionInitializer dI =
                    new DistributionInitializer(splistElite);
            //intiailize the distribution based on the elite samples
            //from the previous step
            dI.printDI();

            double dist = 10.0;
            Distribution oldDist = D;
            //save the old distribution for comparison
            int numCESteps = 0;
            //while (numCESteps < 50 && dist > 0.1) {
            while (numCESteps < numMaxCEsteps) {
                //splist = new ArrayList<StatePermutation>();//create a new
                //list of permutations based on the new distribution
                D = new Distribution(dI, rnd);
                dist = oldDist.distDistance(D);
                //System.out.println(dist);
                //splist = splist.subList(,nsamples);
                Iterator<StatePermutation> spIt = splist.listIterator((int)Math.floor(nsamples*0.1*0.5));
                while(spIt.hasNext()){
                //for (int j = 0; i < nsamples; i++) {
                    try {
                        StatePermutation sp = spIt.next();
                        if(rnd.nextDouble()<0.9){
                        sp.refresh(D.getState(P.names));
                        } else {
                        	sp.refresh(D.getRandomState(P.names));	
                        	//sp.refresh(D.getState(P.names));	
                        }
                        //StatePermutation sp = new StatePermutation(
                        //         D.getState(), rnd);
                        sp.giveScore(P.maxPedigree(sp, 0, 0,finalPedigrees));//get the scores
                        //splist.add(sp);
                    } catch (Exception e) {
                    }
                }
                Collections.sort(splist);
                //System.out.println(splist.size());
                //Collections.reverse(splist);
                sIt = splist.iterator();
                i = 0;
                splistElite = new ArrayList<StatePermutation>();
                while (sIt.hasNext() && i < howManyMax) {
                    StatePermutation s = sIt.next();
                    splistElite.add(s);
                    i++;
                }
                    System.out.println("best("+numCESteps+"): "+P.maxPedigree(splistElite.get(0),0,0,finalPedigrees));
                    System.out.println("worst("+numCESteps+"): "+P.maxPedigree(splistElite.get(howManyMax-1),0,0,finalPedigrees));
                ////////////////////////////////////////////////
                //refresh the distribution etc. for the next step
                dI = new DistributionInitializer(splistElite);
                dI.printDI();
                //prints incremental pedigrees
//                if(numCESteps % 5 == 0){
//                	System.out.println("###########################");
//                	System.out.println("###########################");
//                	System.out.println("step:" + numCESteps);
//                	System.out.println("###########################");
//                	System.out.println("###########################");
//                	finalPedigrees = new ArrayList<List<ChildParentsTriple>>();
//                	for (int j=0;j<splistElite.size();j++){
//                        //System.out.println(P.maxPedigree(splistElite.get(j),2,j,finalPedigrees));
//                    	
//                        P.maxPedigree(splistElite.get(j),2,j,finalPedigrees);
//                    }
//                    //printValues(P.population.values(),finalPedigrees);
//                }
                numCESteps++;
                //oldDist = (Distribution) D.clone();
                oldDist = D;
            }

            //CE iteration finished, print / output stuff
            D = new Distribution(dI, rnd);
            P.maxPedigree(splistElite.get(0), 1, 999,finalPedigrees);
            finalPedigrees = new ArrayList<List<ChildParentsTriple>>();
            for (int j=0;j<splistElite.size();j++){
                //System.out.println(P.maxPedigree(splistElite.get(j),2,j,finalPedigrees));
                P.maxPedigree(splistElite.get(j),2,j,finalPedigrees);
            }
            StatePermutation spcorrect = new StatePermutation(P.N);
            
            //############
            //not used
            if(!first.equalsIgnoreCase(second)){
            /*//printValues(P.population.values(),finalPedigrees);
            //printCombGraph(P.population.values(),finalPedigrees,first+"x.dot",0);
            P.maxPedigree(spcorrect, 1, 0,finalPedigrees);
            
            System.out.println(splistElite.get(0).getScore());
            System.out.println(spcorrect.getScore());
            PR = new PedigreeReader(direct+
                    second +
                    ".xml");
            P.storeOld();
            PR.getPedigree(P);
            Collections.sort(P.names);
            System.out.println("###################################");
            //P.print();
            OrderRestriction OR = new OrderRestriction(P.N,finalPedigrees.get(0),P.names);
            System.out.println("###################################");
            OR.printOR();
            System.out.println(P.names);
            OR.printOR(P.names);
            //from here we need to reinitialize everything, and run it again with the bigger pedigree
            //probably we should shift stuff to a different method and pass files / OR objects to it
            finalPedigrees = new ArrayList<List<ChildParentsTriple>>();
            P.initAFreqsList();
            P.fillAlleleFreqs();

            Distribution.N = P.N;
            Individual.N = P.N;
            ChildParentsTriple.N = P.N;
            DistributionInitializer.N = P.N;
            StatePermutation.N = P.N;
            D = new Distribution(rnd, P);
            D.giveOR(OR,P.oldNames, P.names);
            //creates a distribution, defaultly (ie. everything mean 0.0)
            //D.printDist();

            //intitializes the CE population
            splist = new ArrayList<StatePermutation>();
            for (i = 0; i < nsamples; i++) {
                try {
                    StatePermutation sp = new StatePermutation(
                            D.getState(P.names), rnd);
                    sp.giveScore(P.maxPedigree(sp, 0, 0,finalPedigrees));
                    splist.add(sp); //adds to the CE population from the
                    //distribution, gets scores
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //Collections.sort(splist); //sort it so we can find elite stuff
            howManyMax = CEsort.sort(splist,(int)Math.floor(nsamples*0.15),0,splist.size()-1);
            //- their sort is much better than ours
            //probably worth checking this comparator is good

            sIt = splist.iterator();
            i = 0;
            splistElite =
                    new ArrayList<StatePermutation>();
            //List<StatePermutation> superElite =
            //        new ArrayList<StatePermutation>();
            //while (sIt.hasNext() && i < nsamples * 0.15) {
            while (sIt.hasNext() && i < howManyMax) {
                //saves the good ones as elite and the really good ones
                //as super elite
                StatePermutation s = sIt.next();
                splistElite.add(s);
                //if (i < nsamples * 0.1 * 0.5) {
                //    superElite.add(s);
                //}
                i++;
            }
            /////////////////////////////////////////
            dI =
                    new DistributionInitializer(splistElite);
            //intiailize the distribution based on the elite sampes
            //from the previous step
            dI.printDI();

            dist = 10.0;//the distance between this distribution
            //and the previous one - initially just a random number,
            //this isn't currently used
            //Distribution oldDist = (Distribution) D.clone();
            oldDist = D;
            //save the old distribution for comparison
            numCESteps = 0;
            //while (numCESteps < 50 && dist > 0.1) {
            System.out.println("second step");
            while (numCESteps < numMaxCEsteps) {
                //splist = new ArrayList<StatePermutation>();//create a new
                //list of permutations based on the new distribution
                D = new Distribution(dI, rnd);
                D.giveOR(OR,P.oldNames, P.names);
                dist = oldDist.distDistance(D);
                //System.out.println(dist);
                //splist = splist.subList(,nsamples);
                Iterator<StatePermutation> spIt = splist.listIterator((int)Math.floor(nsamples*0.1*0.5));
                while(spIt.hasNext()){
                //for (int j = 0; i < nsamples; i++) {
                    try {
                        StatePermutation sp = spIt.next();
                        sp.refresh(D.getState(P.names));
                        //StatePermutation sp = new StatePermutation(
                        //         D.getState(), rnd);
                        sp.giveScore(P.maxPedigree(sp, 0, 0,finalPedigrees));//get the scores
                        //splist.add(sp);
                    } catch (Exception e) {
                    }
                }
                //splist.addAll(superElite); //add the super elite ones
                //from the previous step to the population so we know
                //that there are some good elements there at least
                //Collections.sort(splist);
                howManyMax = CEsort.sort(splist,(int)Math.floor(nsamples*0.15),0,splist.size()-1);
                //System.out.println(splist.size());
                //Collections.reverse(splist);
                sIt = splist.iterator();
                i = 0;
                splistElite = new ArrayList<StatePermutation>();
                //superElite = new ArrayList<StatePermutation>();
                //while (sIt.hasNext() && i < nsamples * 0.15) 
                while (sIt.hasNext() && i < howManyMax) {
                    //get the new good/really good elements
                    StatePermutation s = sIt.next();
                    splistElite.add(s);
                    //if (i < nsamples * 0.1 * 0.5) {
                    //    superElite.add(s);
                    //}
                    i++;
                }
                    System.out.println("best: "+P.maxPedigree(splistElite.get(0),0,0,finalPedigrees));
                    System.out.println("worst: "+P.maxPedigree(splistElite.get(howManyMax-1),0,0,finalPedigrees));
                    //System.out.println("***");
                    //for (int sillything = 0; sillything<howManyMax-1; sillything++){
                    //	System.out.println(""+P.maxPedigree(splistElite.get(sillything),0,0,finalPedigrees));
                    //}
                    //System.out.println("***");
                ////////////////////////////////////////////////
                //refresh the distribution etc. for the next step
                dI = new DistributionInitializer(splistElite);
                dI.printDI();
                numCESteps++;
                //oldDist = (Distribution) D.clone();
                oldDist = D;
                
//                if(numCESteps % 5 == 0){
//                	System.out.println("###########################");
//                	System.out.println("###########################");
//                	System.out.println("step:" + numCESteps);
//                	System.out.println("###########################");
//                	System.out.println("###########################");
//                	finalPedigrees = new ArrayList<List<ChildParentsTriple>>();
//                    for (int j=0;j<splistElite.size();j++){
//                        //System.out.println(P.maxPedigree(splistElite.get(j),2,j,finalPedigrees));
//                        P.maxPedigree(splistElite.get(j),2,j,finalPedigrees);
//                    }
//                    //printValues(P.population.values(),finalPedigrees);
//                }
            }
            
            //CE iteration finished, print / output stuff
            D = new Distribution(dI, rnd);
            D.giveOR(OR,P.oldNames, P.names);*/
            }
            //#######################
            
            //System.out.println(splistElite.get(0).getSP());
            //System.out.println(P.maxPedigree(splistElite.get(0), 1, 999,finalPedigrees));
            P.maxPedigree(splistElite.get(0), 1, 999,finalPedigrees);
            finalPedigrees = new ArrayList<List<ChildParentsTriple>>();
            for (int j=0;j<splistElite.size();j++){
                //System.out.println(P.maxPedigree(splistElite.get(j),2,j,finalPedigrees));
                P.maxPedigree(splistElite.get(j),2,j,finalPedigrees);
            }
            //printValues(P.population.values(),finalPedigrees);
            //printCombGraph(P.population.values(),finalPedigrees,second+".dot",1);
            
            System.out.println(P.getElementPermutation(splistElite.get(0).state));
            finalPedigrees = new ArrayList<List<ChildParentsTriple>>();
            for (int j=0;j<1;j++){
                //System.out.println(P.maxPedigree(splistElite.get(j),2,j,finalPedigrees));
                P.maxPedigree(splistElite.get(j),2,j,finalPedigrees);
            }
            printValues(P.population.values(),finalPedigrees);
            
            spcorrect = new StatePermutation(P.N);
            //P.maxPedigree(spcorrect, 1, 0,finalPedigrees);
            System.out.println(splistElite.get(0).getScore()); // best calculated
            //System.out.println(spcorrect.getScore()); // best possible score
            P.printAlleleFreqs();
            //P.print();
        } catch (Exception e) {
            //exceptions are bad :(
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("Execution time was "+(end-start)+" ms.");
    }
    
    public static void printValues(Collection<Individual> pop, List<List<ChildParentsTriple>> outPed){
        //takes the population and a list of the best pedigrees
        //counts the relationships that are common between them
        //ie. for each individual, finds all the parent pairs assigned to it in "good" pedigrees
        Iterator<Individual> pIt = pop.iterator();
        while(pIt.hasNext()){
            int iId = pIt.next().getId();
            List<SolCounterWrapper> iSolList = new ArrayList<SolCounterWrapper>();
            System.out.println(" ");
            System.out.println(iId);
            Iterator<List<ChildParentsTriple>> solIt = outPed.iterator();
            while(solIt.hasNext()){
            	//int countThing = 0;
                List<ChildParentsTriple> cSol = solIt.next();
                Iterator<ChildParentsTriple> cIt = cSol.iterator();
                while(cIt.hasNext()){
                	//countThing++;
                    ChildParentsTriple c = cIt.next();
                    //System.out.println(c.child.getId());
                    if(c.child.getId()==iId){
                        //System.out.println(c.toString());
                        Iterator<SolCounterWrapper> scwIt = iSolList.iterator();
                        int f=0;
                        while(scwIt.hasNext()){
                            SolCounterWrapper s = scwIt.next();
                            //System.out.println("___"+s);
                            if(s.isThis(c)){
                                //System.out.println("match");
                                s.increment();
                                f=1;
                                break;
                            }
                        }
                        if (f==0){
                            iSolList.add(new SolCounterWrapper(iId,c));
                            //System.out.println("added "+c);
                        }
                    }
                }
                //System.out.println("countthing: "+ countThing);
            }
            System.out.println(iSolList.toString());
        }

    }


    public static void printCombGraph(Collection<Individual> pop, List<List<ChildParentsTriple>> outPed, String file,int fflag){
        //takes the population and a list of the best pedigrees
        //counts the relationships that are common between them
        //ie. for each individual, finds all the parent pairs assigned to it in "good" pedigrees
        Iterator<Individual> pIt = pop.iterator();
        List<List<SolCounterWrapper>> combSolList = new ArrayList<List<SolCounterWrapper>>();
        while(pIt.hasNext()){
            int iId = pIt.next().getId();
            List<SolCounterWrapper> iSolList = new ArrayList<SolCounterWrapper>();
            System.out.println(" ");
            System.out.println(iId);
            Iterator<List<ChildParentsTriple>> solIt = outPed.iterator();
            while(solIt.hasNext()){
                List<ChildParentsTriple> cSol = solIt.next();
                Iterator<ChildParentsTriple> cIt = cSol.iterator();
                while(cIt.hasNext()){
                    ChildParentsTriple c = cIt.next();
                    if(c.child.getId()==iId){
                        //System.out.println(c.toString());
                        Iterator<SolCounterWrapper> scwIt = iSolList.iterator();
                        int f=0;
                        while(scwIt.hasNext()){
                            SolCounterWrapper s = scwIt.next();
                            //System.out.println("___"+s);
                            if(s.isThis(c)){
                                //System.out.println("match");
                                s.increment();
                                f=1;
                                break;
                            }
                        }
                        if (f==0){
                            iSolList.add(new SolCounterWrapper(iId,c));
                            //System.out.println("added "+c);
                        }
                    }
                }
            }
            System.out.println(iSolList.toString());
            combSolList.add(iSolList);
        }
                            try {
            PrintWriter outGen = new PrintWriter(new FileWriter(file));
            ChildParentsTriple ind;
            //the stuff at the start
            outGen.println("digraph GRAPH_0 {");
            outGen.println("edge [ dir=none];");
            outGen.println("graph [ rankdir=TB ];");
            outGen.println("ratio=auto;");
            outGen.println("mincross=2.0;");
            outGen.println("node [");
            outGen.println("	fontsize=11,");
            outGen.println("	fillcolor=white,");
            outGen.println("	style=filled,");
            outGen.println("];");
            outGen.println("subgraph cluster_0 {");
            outGen.println("label = \"\"");
            Iterator<List<SolCounterWrapper>> cslIt = combSolList.iterator();

            while (cslIt.hasNext()) { //create the nodes, and
                //if they have parents, the nodes that link children to parents
                int counter = 0;
                List<SolCounterWrapper> cswC = cslIt.next();
                Iterator<SolCounterWrapper> csIt = cswC.iterator();
                while(csIt.hasNext()){
                    SolCounterWrapper tWrS = csIt.next();
                    ChildParentsTriple tWr = tWrS.rel;
               if(fflag==1){
            	   if(tWr.child.getGender()==1){
                outGen.println("" + tWr.getCId()
                        + " [ label=\"" + tWr.getCId() + "\", shape=diamond," +
                        " width=0.000000, height=0.000000,"
                        + " fillcolor=\"black\", fontcolor=\"white\" ]");
                } else {
                    outGen.println("" + tWr.getCId()
                            + " [ label=\"" + tWr.getCId() + "\", shape=circle," +
                            " width=0.000000, height=0.000000,"
                            + " fillcolor=\"black\", fontcolor=\"white\" ]");
                }
               } else {
                     //outGen.println("" + tWr.getCId()
                     //   + " [ label=\"" + tWr.getCId() + "\", shape=diamond," +
                     //   " width=0.000000, height=0.000000,"
                     //   + " fillcolor=\"black\", fontcolor=\"green\" ]");
              	   if(tWr.child.getGender()==1){
                       outGen.println("" + tWr.getCId()
                               + " [ label=\"" + tWr.getCId() + "\", shape=diamond," +
                               " width=0.000000, height=0.000000,"
                               + " fillcolor=\"black\", fontcolor=\"green\" ]");
                       } else {
                           outGen.println("" + tWr.getCId()
                                   + " [ label=\"" + tWr.getCId() + "\", shape=circle," +
                                   " width=0.000000, height=0.000000,"
                                   + " fillcolor=\"black\", fontcolor=\"green\" ]");
                       }
               }
                if (tWr.hasParents()) {
                    outGen.println("p" + tWr.getCId()+"_"+counter
                            + " [ label=\""+tWrS.qty+"\", shape=circle, style=filled,"
                            + " width=0.1, height=0.1 ]");
                }
                counter++;
                }
            }

            outGen.println("}");
            //now draw the connection lines
            cslIt = combSolList.iterator();
            while (cslIt.hasNext()) {
                List<SolCounterWrapper> cswC = cslIt.next();
                Iterator<SolCounterWrapper> csIt = cswC.iterator();
                int counter = 0;
                while(csIt.hasNext()){
                    ChildParentsTriple tWr = csIt.next().rel;
                //ind = iIt.next();
                ParentsWrapper pw = tWr.getP();

                if (tWr.hasParents()) {
                    outGen.println("p" + tWr.getCId()+"_"+counter + " -> "
                            + tWr.getCId()
                            + "  [ color=\"#000000\" weight=2 ]");
                    outGen.println("" + pw.mother.getId() + " -> p"
                            + tWr.getCId()+"_"+counter
                            + "  [ color=\"#000000\" weight=1 ]");
                    if (!pw.father.isEmpty()) {
                        outGen.println("" + pw.father.getId() + " -> p"
                                + tWr.getCId()+"_"+counter
                                + "  [ color=\"#000000\" weight=1 ]");
                    }
                }
                counter++;
            }
            }

            outGen.println("}");
            outGen.close();
        } catch (IOException e) {
            //printwriter objects throw these,
            //so it needs to be caught...hopefully never actually happens
        }

    }
}
