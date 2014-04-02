

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





public final class Locus {

    final int a1;
    final int a2;
    public final boolean isUnknown(){
    	return (a1==-1);
    }
    public Locus(int a_1, int a_2) {
        a1 = a_1;
        a2 = a_2;
    }

    public int choose(Random rng) {
        //picks one at random (for breeding)
        if (rng.nextFloat() < 0.5) {
            return a1;
        }
        return a2;
    }

    public String toString() {
        return "(" + strCSV() + ")";
    }

    public String strCSV(int j) {
        return "" + j + a1 + "," + j + a2;
    }

    public String strF(int j) {
        //prints the allele with some noise thing for differentiation
        return "" + j + a1 + "." + j + a2;
    }

    public String strCSV() {
        return "" + a1 + "," + a2;
    }

    private final boolean hasAllele(int a) {
        return (a ==a1) ? true : (a==a2) ? true :  false;
    }

    public final boolean hasAlleleCommon(Locus l) {
        return hasAllele(l.a1) ? true : (hasAllele(l.a2))?true: false;
    }

    public int getUncommon(Locus l) {
        //returns the allele of l that isn't shared with this
        if (hasAllele(l.a1)) {
            return l.a2;
        } else {
            return l.a1;
        }
    }

    public final boolean commonWithBoth(Locus a, Locus b) {
        //checks if this has its pair of alleles inherited from a and b
        return ((a.hasAllele(a1) && b.hasAllele(a2)) ? true :
                (a.hasAllele(a2) && b.hasAllele(a1)) ? true : false);
        //return true;
    }
    public final int numInCommon(Locus a){
    	//given that we already know it has one
    	return (a.a1==a.a2) ? 2:1;
    }
}
