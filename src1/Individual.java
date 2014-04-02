

import java.util.*;
//import java.io.PrintWriter;
//import java.lang.Math.*;




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





public class Individual {
    static int N;
    int birth; //these are obvious
    int gender;
    int k;
    final int id;
    List<Locus> genes;
    Individual mother;
    Individual father;
    int lastchild;
    int death;
    List<Individual> children;
    boolean empty = false;
    double eprob = -1.0;
    int sizeClass = 0;
    int obsTime;

    public Individual() {
        id = N;
        empty = true;
        //empty constructor
    }

    public final boolean isEmpty() {
        return empty;
    }

    public Individual(Individual m, Individual f, Random rnd, int nInd,
            int t) {
        /*
         * constructor:
         * takes two parents m and f, as well as a Random,
         * the id it is going to get, and the time of birth
         */
        id = nInd;
        //k = Generate.K;
        birth = t;
        gender = rnd.nextInt(2);
        genes = new ArrayList<Locus>(k);
        for (int i = 0; i < k; i++) {
            //at each loci, get one at random from each parent
            genes.add(new Locus(f.genes.get(i).choose(rnd),
                    m.genes.get(i).choose(rnd)));
        }
        mother = f;
        father = m;
        lastchild = -10;

        double prob2 = 0.23;
        int nb = 0;
        int r = 12;
        for (int j = 0; j < r; j++) {
            double seed2 = rnd.nextDouble();
            nb = nb + (int) Math.ceil(Math.log((double) seed2)
                    / Math.log(1.0 - prob2));
        }

        System.out.println("age: " + nb);
        death = birth + nb;
        children = new ArrayList<Individual>();
        //System.out.println(m.genes);
        //System.out.println(f.genes);
        //System.out.println(genes);
    }

    public Individual(Random rnd, int nInd) {
        /*
         * constructor:
         * takes just a Random and the id
         * this is for founders / unrelated individuals
         */
        id = nInd;
        //k = Generate.K;
        double seed = rnd.nextDouble();
        double prob = 1.0 / 20;
        int geo = (int) Math.ceil(Math.log((double) seed)
                / Math.log(1.0 - prob));
        double prob2 = 0.23;
        int nb = 0;
        int r = 12;
        for (int j = 0; j < r; j++) {
            double seed2 = rnd.nextDouble();
            nb = nb + (int) Math.ceil(Math.log((double) seed2)
                    / Math.log(1.0 - prob2));
        }
        //geometric RV
        birth = (-1) * geo;
        death = Math.max(1, birth + nb);
        //say this individual was born in year -Geom(20)
        gender = rnd.nextInt(2);
        genes = new ArrayList<Locus>(k);
        for (int i = 0; i < k; i++) {
            genes.add(new Locus(rnd.nextInt(6), rnd.nextInt(6)));
        }
        mother = null;
        father = null;
        lastchild = -10;
        children = new ArrayList<Individual>();
    }

    public Individual(String iId, String sex, String b, String obs, String sizeC, String[] gen) {
        id = Integer.parseInt(iId);
        if (sex.equalsIgnoreCase("M")||sex.equalsIgnoreCase("1")) {
            gender = 1;
        } else if (sex.equalsIgnoreCase("F")||sex.equalsIgnoreCase("0")) {
            gender = -1;
        } else {
        	gender = 0;
        }
        if (!b.contains("?")) {
            System.out.println(b);
            birth = Integer.parseInt(b);
        }
        if(obs!=""&& !obs.equalsIgnoreCase("?")){
        	obsTime = Integer.parseInt(obs);
        }
        if(sizeC!=""){
        	sizeClass = Integer.parseInt(sizeC);
        }
        mother = null;
        father = null;
        k = gen.length;
        genes = new ArrayList<Locus>(k);

        for (int i = 0; i < k; i++) {
            String[] l = gen[i].split("[.]");
            int firAl=0;
            int secAl = 0;
            if(l[0].equalsIgnoreCase("?")){
            	firAl = -1;
            } else {
            	firAl = Integer.parseInt(l[0]);
            }
            if(l[1].equalsIgnoreCase("?")){
            	secAl = -1;
            } else {
            	secAl = Integer.parseInt(l[1]);
            }
            genes.add(new Locus(firAl,
                    secAl));
        }
    }

    public final void print() {
        // generic print some stuff about the individual
        System.out.println("birth: " + birth);
        System.out.println("gender: " + gender);
        System.out.println("genes: " + genes.toString());
    }

    public final String toString() {
        //toString
        //prints the id of this individual and those of its parents,
        //if they exist
        if (mother != null) {
            if (father != null) {
                return "(" + id + "," + "(" + mother.getId() + ","
                        + father.getId() + "))" + " [" + birth +" "+gender+ "]";
            } else {
                return "(" + id + "," + "(" + mother.getId() + ",-))"
                        + " [" + birth +" "+gender+ "]";
            }
        } else {
            if (father != null) {
                return "(" + id + "," + "(-," + father.getId() + "))"
                        + " [" + birth +" "+gender+ "]";
            } else {
                return "(" + id + ")" + " [" + birth +" "+gender+ "]";
            }
        }
    }

    public final int getId() {
        return id;
    }

    public final String idStr() {
        //prints an id string of at least 3 digits
        int add = 10 - ("" + id).length();
        StringBuffer str = new StringBuffer("" + id);
        char[] ch = new char[add];
        Arrays.fill(ch, ' ');

        str.insert(0, ch);
        return str.toString();
    }

    public final String getIdS() {
        if (!empty) {
            return "" + id;
        } else {
            return "";
        }
    }

    public final int getGender() {
        return gender;
    }

    public final String getSex() {
        if (gender == 1) {
            return "M";
        } else if (gender == -1){
        return "F";
        } else if (empty==true){
        	return "-";
        } else {
        	return "U";
        }
        
    }

    public final boolean hasParents() {
        return (mother != null || father != null);
    }

    public double getEmpProb(List<HashMap<Integer, Double>> aFreqs) {
        //works out based on the allele frequencies in the population
        //the liklihood of seeing this configuration
        // "at random"
        if(eprob!=-1.0){
            return eprob;
        }
        double cP = 1.0;
        List<Locus> cG = genes;
        Iterator<Locus> cIt = cG.iterator();
        Iterator<HashMap<Integer, Double>> aIt = aFreqs.iterator();

        while (cIt.hasNext()) {
            Locus c = cIt.next();
            HashMap<Integer, Double> h = aIt.next();
            cP *= h.get(new Integer(c.a1)).doubleValue()
                    * h.get(new Integer(c.a2)).doubleValue();
        }
        //return Math.pow(0.2*0.2, cG.size());
        eprob = cP;
        return cP;//believe it or not, iteration is faster
    }
    public boolean Equals(Object o){
        //System.out.println(isEmpty());
        //System.out.println(((Individual)o).isEmpty());
        if((isEmpty()&&((Individual)o).isEmpty())||id ==((Individual)o).id){
            return true;
        } else {
            return false;
        }
    }
    public boolean eligiableParent(Individual ind){
    	//if *this* is eligiable to be a parent of *other*
        //System.out.println(""+obsTime + " "+ ind.obsTime + " " +sizeClass + " " + ind.sizeClass+ " " +((sizeClass + (2- ind.sizeClass)*((sizeClass != 0)?1:0))*4 <= (ind.obsTime-obsTime)));
    	if (empty == true) return true;
    	return ((sizeClass + (2- ind.sizeClass)*((sizeClass != 0)?1:0))*3 <= (ind.obsTime-obsTime));
    	
    	
    }
    
}
