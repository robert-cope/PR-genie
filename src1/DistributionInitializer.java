
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






public class DistributionInitializer {
    //used to intiailize a distribution - in this case,
    //take a list of the elite permutations
    //and generate means/variances for the position of each element

    List<Double> means;
    List<Double> variances;
    //List<List<Integer>> countsInd;
    static int N;

    public DistributionInitializer() {
        //do nothing - shouldn't really ever happen
    }

    public DistributionInitializer(List<StatePermutation> splist) {
        //take the elite states, work out means/variances
        //N = n;
        means = new ArrayList<Double>();
        variances = new ArrayList<Double>();
        //countsInd=new ArrayList<List<Integer>>();
        int ctot = 0;
        int[] positions = new int[N];
        Iterator<StatePermutation> spIt = splist.iterator();
        int cpos=0;
        while (spIt.hasNext()) {
            StatePermutation sp = spIt.next();
            Iterator<Integer> sIt = sp.getSP().iterator();
            int i = 0;
            int w = Math.max(1,5-cpos);
            while (sIt.hasNext()) {
                int p = sIt.next().intValue();
                positions[p] += i*w;
                i++;
            }
            ctot=ctot+w;
            cpos++;
        }
        for (int i = 0; i < N; i++) {
            means.add(new Double((double) positions[i] / (ctot * 1.0)));
        }
        double[] pos = new double[N];
        spIt = splist.iterator();
        while (spIt.hasNext()) {
            StatePermutation sp = spIt.next();
            Iterator<Integer> sIt = sp.getSP().iterator();
            int i = 0;
            while (sIt.hasNext()) {
                int p = sIt.next().intValue();
                pos[p] += Math.pow((i - means.get(p)), 2);
                i++;
            }
            ctot++;
        }
        for (int i = 0; i < N; i++) {
            variances.add(new Double(N+(pos[i] / (ctot - 1))));

        }
    }

    public List<Double> getMeans() {
        return means;
    }

    public List<Double> getStdDev() {
        //turn variance into SD and return
        List<Double> sd = new ArrayList<Double>();
        Iterator<Double> vIt = variances.iterator();
        while (vIt.hasNext()) {
            sd.add(new Double(Math.sqrt(vIt.next().doubleValue())));
        }
        return sd;
    }

    public int getN() {
        return N;
    }

    public void printDI() {
        System.out.println(means);
        System.out.println(variances);
    }
}
