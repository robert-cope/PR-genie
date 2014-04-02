
import java.util.*;
//import java.io.*;




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





public class Distribution implements Cloneable {
    //a distribution
    //basically stores means and std. dev's
    //when asked, creates position wrappers which
    //generate a score based on the mean/sd
    //sorts these to return the permutation

    static int N=0;
    final Random rnd;
    List<Double> means;
    List<Double> stdDev;
    List<Integer> isOld;
    Double zD;
    OrderRestriction OR = new OrderRestriction();

    public Distribution(Random r,Pedigree P) {
        //this is the default - used in the first iteration
        //when nothing has happened yet
        //just sets all the means and sd's the same so that
        //calculation can be random
        //N = n;
        rnd = r;
        //rnd = R;
        zD = new Double(0.0);
        means = new ArrayList<Double>();
        stdDev = new ArrayList<Double>();
        isOld=new ArrayList<Integer>();
        for (int i = 0; i < N; i++) {
            isOld.add(new Integer(0));
            //means.add(new Double(P.population.get(P.names.get(i)).sizeClass*10.0));
            means.add(new Double(0.0));
            stdDev.add(new Double(N/2.0));
        }
    }

    public Distribution(DistributionInitializer DI, Random r) {
        //gets the stuff from a distributioninitializer
        //rnd=R;
        zD = new Double(0.0);
        //N = DI.getN();
        means = DI.getMeans();
        stdDev = DI.getStdDev();
        isOld=new ArrayList<Integer>();
        for (int i = 0; i < N; i++) {
            isOld.add(new Integer(0));
        }
        rnd = r;

    }
    public void giveOR(OrderRestriction o,List<Integer> oldNames,List<Integer> newNames){
        OR = o;
        Iterator<Integer> oIt = oldNames.iterator();
        while(oIt.hasNext()){
            int pos = newNames.indexOf(oIt.next().intValue());
            isOld.set(pos, new Integer(1));
        }
    }
    public void printDist() {
        //prints the distribution
        //ie. really just the means
        Iterator<Double> mIt = means.iterator();
        String l;
        while (mIt.hasNext()) {
            l = "[ ";
            l += "" + mIt.next().doubleValue() + " ";
            l += "]";
            System.out.println(l);
        }
    }

    public List<Integer> getState(List<Integer> names) {
        //create the wrappers, generate, sort, get the permutation, return
        List<PositionWrapper> pWo = new ArrayList<PositionWrapper>();
        List<PositionWrapper> pWn = new ArrayList<PositionWrapper>();
        List<Integer> sR = new ArrayList<Integer>();
        Iterator<Double> mIt = means.iterator();
        Iterator<Double> sIt = stdDev.iterator();
        Iterator<Integer> oIt = isOld.iterator();
        Iterator<Integer> nIt = names.iterator();
        int j = 0;
        while (mIt.hasNext()) {
            if(oIt.next().intValue()==1){
            pWo.add(new PositionWrapper(j, mIt.next().doubleValue(),
                    sIt.next().doubleValue(), rnd,OR,nIt.next().intValue()));
            } else {
            pWn.add(new PositionWrapper(j, mIt.next().doubleValue(),
                    sIt.next().doubleValue(), rnd,OR,nIt.next().intValue()));
            }
            j++;
        }
        //Collections.sort(pW);
        //TestRestriction.warpedInsertionSort((ArrayList<PositionWrapper>)pWo);
        Collections.sort(pWo);
        //System.out.println("pWo: "+pWo);
        Collections.sort(pWn);
        //System.out.println("pWn: "+pWn);
        List<PositionWrapper> pW = new ArrayList<PositionWrapper>();
        int s = 0;
        int t = 0;
        while(s+t<N){//mergesort
            if(!(s<pWo.size())){
                pW.add(pWn.get(t));
                t++;
                continue;
            }
            if(!(t<pWn.size())){
                pW.add(pWo.get(s));
                s++;
                continue;
            }
            if(pWo.get(s).pos<pWn.get(t).pos){
                pW.add(pWo.get(s));
                s++;
            } else {
                pW.add(pWn.get(t));
                t++;
            }
        }
        Iterator<PositionWrapper> pIt = pW.iterator();
        //System.out.println("pW: "+pW);
        while (pIt.hasNext()) {
            sR.add(new Integer(pIt.next().getObj()));
        }
        return sR;
    }
    public List<Integer> getRandomState(List<Integer> names) {
        //create the wrappers, generate, sort, get the permutation, return
        List<PositionWrapper> pWo = new ArrayList<PositionWrapper>();
        List<PositionWrapper> pWn = new ArrayList<PositionWrapper>();
        List<Integer> sR = new ArrayList<Integer>();
        //Iterator<Double> mIt = means.iterator();
        //Iterator<Double> sIt = stdDev.iterator();
        Iterator<Integer> oIt = isOld.iterator();
        Iterator<Integer> nIt = names.iterator();
        int j = 0;
        while (nIt.hasNext()) {
            if(oIt.next().intValue()==1){
            pWo.add(new PositionWrapper(j, 0.0,
                    1.0, rnd,OR,nIt.next().intValue()));
            } else {
            pWn.add(new PositionWrapper(j, 0.0,
                    1.0, rnd,OR,nIt.next().intValue()));
            }
            j++;
        }
        //Collections.sort(pW);
        //TestRestriction.warpedInsertionSort((ArrayList<PositionWrapper>)pWo);
        Collections.sort(pWo);
        //System.out.println("pWo: "+pWo);
        Collections.sort(pWn);
        //System.out.println("pWn: "+pWn);
        List<PositionWrapper> pW = new ArrayList<PositionWrapper>();
        int s = 0;
        int t = 0;
        while(s+t<N){//mergesort
            if(!(s<pWo.size())){
                pW.add(pWn.get(t));
                t++;
                continue;
            }
            if(!(t<pWn.size())){
                pW.add(pWo.get(s));
                s++;
                continue;
            }
            if(pWo.get(s).pos<pWn.get(t).pos){
                pW.add(pWo.get(s));
                s++;
            } else {
                pW.add(pWn.get(t));
                t++;
            }
        }
        Iterator<PositionWrapper> pIt = pW.iterator();
        //System.out.println("pW: "+pW);
        while (pIt.hasNext()) {
            sR.add(new Integer(pIt.next().getObj()));
        }
        return sR;
    }
    public double distDistance(Distribution other) {
        //gets the distance between two distribution
        //the distance in this case is basically the taxi-driver metric
        //they should be the same size. if not, thats bad
        double d = 0.0;
        Iterator<Double> mIt = means.iterator();
        Iterator<Double> oIt = other.means.iterator();
        while (mIt.hasNext()) {
            d += Math.abs(mIt.next().doubleValue() - oIt.next().doubleValue());

        }
        return d;
    }

    public Object clone() {
        //clone - because serialization is rather slow
        try {
            Distribution D = (Distribution) super.clone();
            D.means = new ArrayList<Double>();
            Iterator<Double> iIt = means.iterator();
            while (iIt.hasNext()) {
                D.means.add(new Double(iIt.next().doubleValue()));
            }

            return D;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e.toString());
        }
    }
}
