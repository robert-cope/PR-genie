
import java.util.*;





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





/*
 * object that stores a state permutation - ie. a list of 0,...,n-1
 (for a population with n total individuals) rearranged in some ordering
 * the Pedigree object has means of transferring from these numberings
 to actual individual id's, so that doesnt really matter here
 * also stores the score, and the distribution intended to create it
 * */
public class StatePermutation implements Comparable {

    Distribution dist;
    static int N=0;
    List<Integer> state;
    Random rnd;
    double score;

    public StatePermutation(Distribution D, Random R, List<Integer> names) {
        //a constructor, based on a distribution - which is then used
        //to generate the permutation
        //N = n;
        rnd = R;
        dist = D;
        score = -1.0;
        state = new ArrayList<Integer>();
        genState(names);
    }
    public StatePermutation(List<Integer> s, Random R) {
        //a constructor, based on a distribution - which is then used
        //to generate the permutation
        //N = n;
        rnd = R;
        //dist = D;
        score = -1.0;
        state = s;
        //genState();
    }
    public void refresh(List<Integer> s){
        state = s;
        score = -1.0;
    }

    public StatePermutation(int n) {
        //a constructor, with no distribution - just makes the state
        //the numbers seqentially (ie. the no-modification permutation)
        //N = n;
        state = new ArrayList<Integer>();
        for (int i = 0; i < N; i++) {
            state.add(new Integer(i));
        }
        score = -1.0;
    }

    public void genState(List<Integer> names) {
        //generates a state, by just asking the distribution to give it one
        // this used to be more exciting than it is now
        state = dist.getState(names);
    }

    public double getScore() {
        //some fake score function here
        //
        if (score == -1.0) {
            //relic of a more civilized age - pray this never occurrs
            double s = 0.0;
//		Iterator<Integer> sIt = state.iterator();
//		int j = 0;
//		int a = 0;
//		int b;
//		while (sIt.hasNext()) {
//			b=sIt.next().intValue();
//			s += Math.abs(b-a);
//			a=b;
//			//System.out.println(s);
//			j++;
//		}
//		score = s;
            return s;
        } else {
            return score;
        }
    }

    public double returnScore() {
        return score;
    }

    public void giveScore(double s) {
        //gives a score calculated somewhere else (like Pedigree.MaxPedigree())
        //- this is what should really happen
        score = s;
    }

    public int compareTo(Object otherState) throws ClassCastException {
        //comparator - so that we can sort the stuff when it needs to be sorted
        //required by Collections.sort
        if (!(otherState instanceof StatePermutation)) {
            throw new ClassCastException("A StatePermutation object expected.");
        }
        double diff = getScore() - ((StatePermutation) otherState).getScore();
        if (diff == 0.0) {
            return 0;
        }
        return -(int) Math.round(diff / Math.abs(diff));
        //make it return -1/0/1 appropriately
    }

    public List<Integer> getSP() {
        //returns the permutation
        return state;
    }
    
}
