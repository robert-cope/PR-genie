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





public class OrderRestrictionWrapper {
    int id;
    List<OrderRestrictionWrapper> younger;
    List<OrderRestrictionWrapper> older;
    public OrderRestrictionWrapper(int i){
        id = i;
        younger = new ArrayList<OrderRestrictionWrapper>();
        older = new ArrayList<OrderRestrictionWrapper>();
    }
    public int getId(){
        return id;
    }
    public void addYounger(OrderRestrictionWrapper y){
        younger.add(y);
        //System.out.println(id+" "+younger);
    }
    public void addOlder(OrderRestrictionWrapper o){
        older.add(o);
        //System.out.println(id+" "+older);
    }
    public boolean checkOlder(int ch){
        //System.out.println(id+": "+ch);
        //System.out.println("old "+ older);
        if(older.isEmpty()){
            return false;
        } else {
            Iterator<OrderRestrictionWrapper> oIt = older.iterator();
            boolean istrue = false;
            while(oIt.hasNext()){
                OrderRestrictionWrapper orw = oIt.next();
                if(orw.id==ch){
                    ////System.out.println("inside");
                    return true;
                }
                istrue = (istrue ||orw.checkOlder(ch));
            }
            return istrue;
        }
        //return false;
    }
    public boolean checkYounger(int ch){
        //System.out.println(id+": "+ch);
        //System.out.println("yng "+ younger);
        if(younger.isEmpty()){
            return false;
        } else {
            Iterator<OrderRestrictionWrapper> oIt = younger.iterator();
            boolean istrue = false;
            while(oIt.hasNext()){
                OrderRestrictionWrapper orw = oIt.next();
                if(orw.id==ch){
                    //System.out.println(orw.id);
                    return true;
                }
                istrue = (istrue ||orw.checkYounger(ch));
            }
            return istrue;
        }
        //return false;
    }
    public String toString(){
        return (new Integer(id)).toString();
    }
    public void printORW(){
        String s = "("+younger.toString()+"("+toString()+")"+older.toString()+")";
        System.out.println(s);
    }
    public void printORW(List<Integer> names){
        String s = "(";
        Iterator<OrderRestrictionWrapper> yIt = younger.iterator();
        while(yIt.hasNext()){
            s+=names.get(yIt.next().id)+" ";
        }
        s +="("+names.get(id)+")";
        Iterator<OrderRestrictionWrapper> oIt = older.iterator();
        while(oIt.hasNext()){
            s+=" "+names.get(oIt.next().id);
        }
        s+=")";
        System.out.println(s);
    }
}
