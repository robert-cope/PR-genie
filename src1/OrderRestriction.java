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





public class OrderRestriction {
    List<OrderRestrictionWrapper> restrictions;
    boolean isnull;
    public OrderRestriction(int N){
        restrictions = new ArrayList<OrderRestrictionWrapper>();
        for(int i=0;i<N;i++){
            restrictions.add(new OrderRestrictionWrapper(i));
        }
        isnull = false;
    }
    public OrderRestriction(int N, List<ChildParentsTriple> soln, List<Integer> names){
        isnull = false;
        restrictions = new ArrayList<OrderRestrictionWrapper>();
        System.out.println(N);
        System.out.println(soln.size());
        System.out.println(names.size());
        for(int i=0;i<N;i++){
            restrictions.add(new OrderRestrictionWrapper(i));
        }
        Iterator<ChildParentsTriple> cIt = soln.iterator();
        while(cIt.hasNext()){
            ChildParentsTriple ctp = cIt.next();
            if (ctp.hasParents()){
            addRestriction(names.indexOf(ctp.parents.mother.getId()),names.indexOf(ctp.child.getId()));
            if(!ctp.parents.father.isEmpty()){
                addRestriction(names.indexOf(ctp.parents.father.getId()),names.indexOf(ctp.child.getId()));
            }
            }
        }
    }
    public OrderRestriction(){
        isnull = true;
    }
    public void addRestriction(int y, int o){
        restrictions.get(y).addOlder(restrictions.get(o));
        restrictions.get(o).addYounger(restrictions.get(y));
    }
    public boolean checkOlder(int y, int o){
        return restrictions.get(y).checkOlder(o);
    }
    public boolean checkYounger(int y, int o){
        return restrictions.get(o).checkYounger(y);
    }
    public void printOR(){
        Iterator<OrderRestrictionWrapper> oIt = restrictions.iterator();
        while (oIt.hasNext()){
            oIt.next().printORW();
        }
    }
    public void printOR(List<Integer> names){
        Iterator<OrderRestrictionWrapper> oIt = restrictions.iterator();
        while (oIt.hasNext()){
            oIt.next().printORW(names);
        }
    }
}
