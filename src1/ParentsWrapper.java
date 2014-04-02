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




public class ParentsWrapper {

    Individual mother;
    Individual father;
    boolean isNull;

    public ParentsWrapper() {
        isNull = true;
    }

    public ParentsWrapper(Individual m, Individual f) {
        mother = m;
        father = f;
        isNull = false;
        if (mother.isEmpty() && father.isEmpty()) isNull=true;
        if (Math.abs(m.getGender()+f.getGender())>1){
        	isNull=true;
        }
    }
    public boolean eligiableParents (Individual child){
    	//return true;
    	if (isNull) return true;
    	return (mother.eligiableParent(child) && father.eligiableParent(child));
    }

    public boolean isNull() {
        return isNull;
    }
    public boolean Equals(Object o){
        return (mother.Equals(((ParentsWrapper)o).mother) && father.Equals(((ParentsWrapper)o).father))||(mother.Equals(((ParentsWrapper)o).father) && father.Equals(((ParentsWrapper)o).mother));
    }
    public boolean matchMother(Individual ind){
        List<Locus> cGenes = ind.genes;
        List<Locus> mGenes = mother.genes;
        Iterator<Locus> cIt = cGenes.iterator();
        Iterator<Locus> mIt = mGenes.iterator();
        while(cIt.hasNext()){
            if(!cIt.next().hasAlleleCommon(mIt.next())){
                return false;
            }
        }
        return true;
    }
}
