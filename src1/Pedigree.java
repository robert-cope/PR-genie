
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
 * this is a pedigree. ideally, it will get some individuals read in
 * it will be able to take a permutation, and using said permutation
 * as an ordering, will construct the maximal pedigree.
 */
public class Pedigree {

    int N;
    HashMap<Integer, Individual> population;
    HashMap<Integer,Individual> oldPop;
    //links the id of the individual to the individuals object
    List<Integer> names;
    List<Integer> oldNames;
    //a list of id's, so that they can be sorted
    //and associated with a permutation
    HashMap<Integer, HashMap<Integer,HashMap<Integer,Double>>> cptScores;//doesnt actually do anything
    List<HashMap<Integer, Double>> aFreqs;
    //stores the allele frequencies- needs to be
    //initialized by initAFreqsList() followed by fillAlleleFreqs()

    public Pedigree() {
        //default constructor, this is the one that is actually used
        population = new HashMap<Integer, Individual>();
        cptScores = new HashMap<Integer, HashMap<Integer,HashMap<Integer,Double>>>(N);
        aFreqs = new ArrayList<HashMap<Integer, Double>>();
        names = new ArrayList<Integer>();
        //do nothing, effectively
    }
    public void storeOld(){
        oldPop=population;
        oldNames = names;
    }

    public Pedigree(int n) {
        //this constructor isnt actually used atm
        N = n;
        population = new HashMap<Integer, Individual>(n * 2);
        //cptScores = new HashMap<String, Double>((n + 1) * n * n + 1);
        aFreqs = new ArrayList<HashMap<Integer, Double>>();
        names = new ArrayList<Integer>();
    }

    public void fillAlleleFreqs() {
        //iterates through the population and calculates allele frequencies
        //needs initAFreqsList() to have happened first
        //uses the standard ideas
        //- just the proportion of that allele that appears at the locus
        //assumes complete information 
        //- every locus has 2 alleles per individual,
        //so 2N in total at each locus
        //double ep = 1.0;
        Iterator<Individual> popIt = population.values().iterator();
        while (popIt.hasNext()) {
            Individual cInd = popIt.next();
            Iterator<Locus> gIt = cInd.genes.iterator();
            Iterator<HashMap<Integer, Double>> aIt = aFreqs.iterator();
            while (gIt.hasNext()) {
                Locus l = gIt.next();
                HashMap<Integer, Double> aF = aIt.next();
                if (aF.containsKey(new Integer(l.a1))) {
                    aF.put(new Integer(l.a1), new Double(aF.get(
                            new Integer(l.a1)).doubleValue() + 1.0 / (2 * N)));
                } else {
                    aF.put(new Integer(l.a1), new Double(1.0 / (2 * N)));
                }
                if (aF.containsKey(new Integer(l.a2))) {
                    aF.put(new Integer(l.a2), new Double(aF.get(
                            new Integer(l.a2)).doubleValue() + 1.0 / (2 * N)));
                } else {
                    aF.put(new Integer(l.a2), new Double(1.0 / (2 * N)));
                }

            }

        }
    }

    public void initAFreqsList() {
        //just initializes the allele frequencies list to be the right size
        //important this happens first
        aFreqs = new ArrayList<HashMap<Integer, Double>>();
        Individual indEx = population.values().iterator().next();
        Iterator<Locus> lIt = indEx.genes.iterator();
        while (lIt.hasNext()) {
            aFreqs.add(new HashMap<Integer, Double>());
            lIt.next();
        }
    }

    public void printAlleleFreqs() {
        //prints the allele frequencies, more for testing at the moment
        Iterator<HashMap<Integer, Double>> aIt = aFreqs.iterator();
        while (aIt.hasNext()) {
            System.out.println(aIt.next().toString());
        }
    }

    public void setN(int n) {
        //sets n, and reinitializes some arrays (which probably isn't needed)
        N = n;
        population = new HashMap<Integer, Individual>(n * 2);
        names = new ArrayList<Integer>();
    }

    public void add(Individual ind) {
        //adds an individual to the population,
        //both to the hashmap and to the list of names
        population.put(new Integer(ind.getId()), ind);
        names.add(new Integer(ind.getId()));
    }

    public List<Integer> getElementPermutation(List<Integer> sp) {
        //idea: take a list of 0,...,n-1 that represents a permutation
        //replace them by the actual elements that belong to this pedigree
        // ie. their id's
        Collections.sort(names);//this probably isnt necessary
        List<Integer> EP = new ArrayList<Integer>();
        Iterator<Integer> sIt = sp.iterator();//the permutation iterator
        while (sIt.hasNext()) {
            EP.add(names.get(sIt.next().intValue()));
        }
        return EP;
    }

    public double maxPedigree(StatePermutation s, int printflag, int prN, List<List<ChildParentsTriple>> outPed) {
        /*
         * iterate through the state provided by s
         * at each point, construct all the child-parent-triples
         * put them into a list
         * take the biggest one
         * add that to a list (the list being the pedigree)
         * return the "score"
         * */
        //if (s.returnScore() != -1.0){
        //    return s.returnScore();
        //}
        
        double score = 0.0;
        List<ChildParentsTriple> pedRelations
                = new ArrayList<ChildParentsTriple>();
        //the actual pedigree
        List<Individual> current = new ArrayList<Individual>();
        //the individuals we have iterated through so far
        current.add(new Individual());
        //empty individual - like the "base case" in recursion
        List<ParentsWrapper> possibleParents = new ArrayList<ParentsWrapper>();
        //running possible parents - basically all pairs of elements
        //we have been past so far
        possibleParents.add(new ParentsWrapper());//empty set of parents
        List<Integer> perm = getElementPermutation(s.state);
        //turns the permutation into a permutation of the actual
        //element id's (rather than just consecutive numbers)
        Iterator<Integer> pIt = perm.iterator();
        Individual nInd;
        Iterator<ParentsWrapper> ppIt;
        List<ChildParentsTriple> indParents;
        ChildParentsTriple ctp;
        Double cs0;
        Iterator<Individual> cIt;
        String ts;
        ChildParentsTriple maxCPT = new ChildParentsTriple();
        //store the best so far
        double best;
        List<Integer> noMatch;
        while (pIt.hasNext()) {
            nInd = population.get(pIt.next());
            noMatch = new ArrayList<Integer>();
            int nmt = 0;
            int nmc = 0;
            double ep = nInd.getEmpProb(aFreqs); //the random prob.
            //for this particular child, ie. taken from allele freqs
            best = -1.0;
            ppIt = possibleParents.iterator();
            int currentIgnore = -1;
            //iterate over the list of possible parents
            //(already constructed, eg. base cases above)

            while (ppIt.hasNext()) {
                ParentsWrapper tpw = ppIt.next();
                if (!tpw.isNull()) {
                	//if (!tpw.mother.eligiableParent(nInd)) continue;
                	//if (!tpw.father.eligiableParent(nInd)) continue;
                	
                	// I have nfi what the below does.
                    if (currentIgnore == tpw.mother.getId()) {
                        continue;
                    }
                    if (!tpw.matchMother(nInd)) {
                        currentIgnore = tpw.mother.getId();
                        noMatch.add(new Integer(currentIgnore));
                        nmt++;
                        continue;
                    }
                    if (nmc == nmt) {
                        nmc = 0;
                    }
                    if (nmc < nmt) {
                        if (noMatch.get(nmc).intValue() == tpw.father.getId()) {
                            nmc++;
                            continue;
                        }
                    }
                }

                ctp = new ChildParentsTriple(nInd, tpw, ep);
                //if (ctp.hasParents()){
                //Integer tid = ctp.child.getId();
                //HashMap<Integer,HashMap<Integer,Double>> cMap = cptScores.get(tid);
                //if (cMap ==null){
                //cs0 = ctp.getTP(aFreqs);
                //cptScores.put(tid,new HashMap<Integer,HashMap<Integer,Double>>());
                //cMap = cptScores.get(tid);
                //System.out.println(cptScores.size());
                //}
                
                //Integer mid = ctp.parents.mother.getId();
                //HashMap<Integer,Double> mMap = cMap.get(mid);
                //if (mMap ==null){
                //cs0 = ctp.getTP(aFreqs);
                //cMap.put(mid,new HashMap<Integer,Double>());
                //mMap = cMap.get(mid);
                //System.out.println(cptScores.size());
                //}
                //Integer fid = ctp.parents.father.getId();
                //Double fVal = mMap.get(fid);
                //if (fVal ==null){
                cs0 = ctp.getTP(aFreqs);
                //if (ctp.child.getId()==9106051){
                //	System.out.println(ctp.toString());
                //}
                //System.out.println(tid+" "+mid+" "+fid+":"+cs0);
                //mMap.put(fid,cs0);
                //cMap = cptScores.get(tid);
                //System.out.println(cptScores.size());
                //} else {
                //    cs0=fVal.doubleValue();
                //    System.out.println(ctp.getTP(aFreqs));
                //    System.out.println(tid+" "+mid+" "+fid+":"+cs0);
                //}
                //} else {
                //    cs0=ctp.child.getEmpProb(aFreqs);
                //}
                //construct for this pair of possible parents a triple,
                //and get its liklihood ratio
                if (cs0 > best) {
                    //if its the best seen so far for this child, keep it
                    maxCPT = ctp;
                    best = cs0;
                }
            }
            //once we have found the best for a child, add it to the pedigree
            score += Math.log(best);//careful of this
            pedRelations.add(maxCPT);

            cIt = current.iterator();
            //iterate over all of the previously seen elements, 
            //add pairs containing the current element and each of
            //the previous to the pairs of potential parents
            while (cIt.hasNext()) {
            	ParentsWrapper newPW = new ParentsWrapper(nInd,cIt.next());
            	if (!newPW.isNull()){
                    possibleParents.add(newPW);
            	}
            	//possibleParents.add(new ParentsWrapper(nInd,cIt.next()));
            }
            //add this one to the list of seen things, so that next time
            //we add parent pairs associated with it
            current.add(nInd);
        }
        //around here, pedRelations contains the maximum liklihood pedigree
        if (printflag == 1) {
            //if we are telling the peidgree to print, print it
            //System.out.println("printing");
            printGraph(pedRelations, "graph-out" + prN + ".dot");
        }
        if (printflag ==2){
            //System.out.println(pedRelations);
            outPed.add(pedRelations);
        }
        //store the score with the statpermutation,
        //probably doesnt do anything at the moment really
        s.giveScore(score);
        return score;
    }

    public static void printGraph(List<ChildParentsTriple> popList,
            String file) {
        //generates a .dot format graph of the pedigree
        try {
            PrintWriter outGen = new PrintWriter(new FileWriter(file));
            Iterator<ChildParentsTriple> iIt = popList.iterator();
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
            while (iIt.hasNext()) { //create the nodes, and
                //if they have parents, the nodes that link children to parents
                ind = iIt.next();
                outGen.println("" + ind.getCId()
                        + " [ label=\"" + ind.getCId() + "\", shape=diamond," +
                        " width=0.000000, height=0.000000,"
                        + " fillcolor=\"black\", fontcolor=\"white\" ]");
                if (ind.hasParents()) {
                    outGen.println("p" + ind.getCId() 
                            + " [ label=\"\", shape=diamond, style=filled,"
                            + " width=0.1, height=0.1 ]");
                }
            }

            outGen.println("}");
            //now draw the connection lines
            iIt = popList.iterator();
            while (iIt.hasNext()) {
                ind = iIt.next();
                ParentsWrapper pw = ind.getP();

                if (ind.hasParents()) {
                    outGen.println("p" + ind.getCId() + " -> " 
                            + ind.getCId()
                            + "  [ color=\"#000000\" weight=2 ]");
                    outGen.println("" + pw.mother.getId() + " -> p" 
                            + ind.getCId()
                            + "  [ color=\"#000000\" weight=1 ]");
                    if (!pw.father.isEmpty()) {
                        outGen.println("" + pw.father.getId() + " -> p" 
                                + ind.getCId()
                                + "  [ color=\"#000000\" weight=1 ]");
                    }
                }
            }

            outGen.println("}");
            outGen.close();
        } catch (IOException e) {
            //printwriter objects throw these,
            //so it needs to be caught...hopefully never actually happens
        }
    }

    public void print() {
        //generic print-stuff method, just prints the population
        System.out.println(population.toString());
    }
}
