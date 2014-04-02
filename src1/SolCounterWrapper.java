


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
 * @author robertcope
 */
public class SolCounterWrapper {
    int ind;
    ChildParentsTriple rel;
    int qty;
    public SolCounterWrapper(int i, ChildParentsTriple c){
       ind = i;
       rel = c;
       qty = 1;
    }
    public void increment(){
        qty++;
    }
    public boolean isThis(ChildParentsTriple c){
        return c.Equals(rel);
    }
    public String toString(){
        return rel.toString()+" x"+qty;
    }
}
