
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






/**
 *
 * wraps a position
 * ie. used by distribution, takes a mean and a sd as well as an id for
 * that element, generates a number with that distribution
 * sortable, so we can then sort based on the generated normals and the
 * resulting list becomes the generated permutation
 * its all obvious
 */
public class PositionWrapper implements Comparable {

    int obj;
    int name;
    double pos;
    OrderRestriction OR;

    public PositionWrapper(int j, double m, double s, Random rnd, OrderRestriction o,int n) {
        obj = j;
        pos = m + (0.5 * s) * rnd.nextGaussian();
        OR = o;
        name=n;
    }

    public int getObj() {
        return obj;
    }

    public double getPos() {
        return pos;
    }
    public String toString(){
        return obj+"["+name+"]"+"("+pos+")";
    }
    public boolean isLessThan(PositionWrapper o){
        double oP = ((PositionWrapper) o).getPos();
        double post;
        if(!OR.isnull){
        if( OR.checkYounger(obj, (o).obj) || OR.checkOlder(obj, (o).obj)){
                post = Math.min(pos, oP-0.01);
            } else
        //we don't necessarily want to do this except if its actually going to be the place where you want to move
            if(OR.checkOlder(((PositionWrapper) o).obj, obj) || OR.checkYounger(((PositionWrapper) o).obj, obj)){
               pos = Math.max(pos, oP+0.01);
               post = pos;
            } else {
            post = pos;
            }
        } else {
            post = pos;
        }
        if(post<oP) return true;
        return false;
    }
    public int compareTo(Object o) {
        double oP = ((PositionWrapper) o).getPos();
        if (OR.isnull) {
            if (getPos() < oP) {
                return -1;
            } else if (getPos() > oP) {
                return 1;
            } else {
                return 0;
            }
        } else {
            //System.out.println("compare: " + obj + "," + ((PositionWrapper) o).obj);
            //System.out.println("younger : " + OR.checkYounger(obj, ((PositionWrapper) o).obj));
            //System.out.println("older : " + OR.checkOlder(obj, ((PositionWrapper) o).obj));
            if( OR.checkYounger(obj, ((PositionWrapper) o).obj) || OR.checkOlder(obj, ((PositionWrapper) o).obj)){
                pos = Math.min(pos, oP-0.01);
            }
            if(OR.checkOlder(((PositionWrapper) o).obj, obj) || OR.checkYounger(((PositionWrapper) o).obj, obj)){
               pos = Math.max(pos, oP+0.01);
            }
            if (getPos() < oP) {
                return -1;
            } else if (getPos() > oP) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
