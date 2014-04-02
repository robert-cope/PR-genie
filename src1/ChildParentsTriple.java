
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
 * this is, funnily enough, a child-parents triple
 * must implement comparable, since we will be using collections.sort to
 * sort them. will almost certainly need to be fast, since it will be called
 * a lot.
 */
public class ChildParentsTriple implements Comparable {
    static int N;
    final Individual child;
    final ParentsWrapper parents;
    final int htId;
    final double errorRate = 0.01;
    private double TP = -1.0;//transition prob
    private double eP;
    //empty prob (ie. of the child from the population allele frequencies)

    public ChildParentsTriple(Individual c, ParentsWrapper p, double empProb) {
        //this is the constructor that really gets used
        child = c;
        parents = p;
        eP = empProb;
        if (parents.isNull()) {
            htId= child.id;
        } else {
            htId= (int)(child.id+parents.mother.id*(N+1)+parents.father.id*Math.pow((N+1),2));
        }
    }

    public ChildParentsTriple() {
        //empty = bad
        child = new Individual();
        parents = new ParentsWrapper();
        htId=0;
    }

    public ChildParentsTriple(Individual c, ParentsWrapper p, double sc,
            double empProb) {
        //dont think we use this since it appears really slow
        //to save stuff in a hash function
        child = c;
        parents = p;
        eP = empProb;
        TP = sc;
        if (parents.isNull()) {
            htId= child.id;
        } else {
            htId= (int)(child.id*Math.pow((N+1),2)+parents.mother.id*(N+1)+parents.father.id);
        }
    }

    public boolean hasParents() {
        //
        return !parents.isNull();
    }

    public String getCId() {
        return "" + child.getId();
    }

    public ParentsWrapper getP() {
        return parents;
    }

    public void giveTP(double s) {
        TP = s;
    }

    public int compareTo(Object o) {
        //compare based on the TP - this is, hoping that all of the TPs
        //have already been calculated
        //dodgey
        if (TP < ((ChildParentsTriple) o).TP) {
            return 1;
        } else if (TP == ((ChildParentsTriple) o).TP) {
            return 0;
        } else {
            return -1;
        }
    }

    public String toString() {
        if (parents.isNull()) {
            return "(" + child.getIdS() + ")";
        } else {
            //return "(" + child.getIdS() + "," + "(" 
            //+ parents.mother.getIdS() + ","
            //+ parents.father.getIdS() + "))[" + getTP() + "]";
            return "(" + child.getIdS() + "," + "(" + parents.mother.getIdS()
                    + "," + parents.father.getIdS() + "))[" + TP + "][" + child.getSex()+""+parents.mother.getSex()+""+parents.father.getSex() + "]";// + parents.eligiableParents(child)+ "";
        }
    }

    public String toShortString() {
        //basically tostring without the getTP() which was
        //taking up most of the time
        if (parents.isNull()) {
            return "(" + child.getIdS() + ")";
        } else {
            return "(" + child.getIdS() + "," + "(" + parents.mother.getIdS()
                    + "," + parents.father.getIdS() + "))";
        }
    }
    public Integer toIntKey(){
        return htId;
    }

    public double getTP(List<HashMap<Integer, Double>> aFreqs) {
        double cP = 1.0;
        List<Locus> cG = child.genes;
        int errors = 0;
        if (TP == -1.0) {
            Iterator<HashMap<Integer, Double>> aIt = aFreqs.iterator();
            //if (!parents.isNull()) {
            if ((!parents.isNull()) && parents.eligiableParents(child)) {
                        //System.out.println(toString());
                if (!parents.mother.isEmpty()) {
                    if (!parents.father.isEmpty()) {
                        //ie. if both parents exist in this childparentstriple
                        List<Locus> mG = parents.mother.genes;
                        List<Locus> fG = parents.father.genes;
                        Iterator<Locus> cIt = cG.iterator();
                        Iterator<Locus> mIt = mG.iterator();
                        Iterator<Locus> fIt = fG.iterator();
                        while (cIt.hasNext()) {
                            Locus l = cIt.next();
                            Locus lm = mIt.next();
                            Locus lf = fIt.next();
                            HashMap<Integer, Double> h = aIt.next();
                            if (l.isUnknown()) {
                            	continue;
                            }
                            if (lm.isUnknown()&& lf.isUnknown()){
                            	continue;
                            }
                            if(lm.isUnknown()&& !lf.isUnknown()){
                                if (lf.hasAlleleCommon(l)) {
                                    cP *= 0.5 *l.numInCommon(lf)* h.get(
                                            new Integer(lf.getUncommon(l)));
                                } else {
                                	if (errors < 3){
                                		errors++;
                                		cP*= 0.5*errorRate;
                                	} else {
                                        cP = 0.0;
                                        break;
                                	}
                                }
                                continue;
                            }
                            if (lf.isUnknown() && !lm.isUnknown()){
                                if (lm.hasAlleleCommon(l)) {
                                    cP *= 0.5 * l.numInCommon(lm)* h.get(
                                            new Integer(lm.getUncommon(l)));
                                } else {
                                	if (errors < 3){
                                		errors++;
                                		cP*= 0.5*errorRate;
                                	} else {
                                        cP = 0.0;
                                        break;
                                	}
                                }
                                continue;
                            }
                            if (l.commonWithBoth(lm, lf)) {
                                cP *= 0.5 * 0.5*l.numInCommon(lm)*l.numInCommon(lf);
                            } else {
                            	if (lm.hasAlleleCommon(l) || lf.hasAlleleCommon(l)){
                                	if (errors < 3){
                                		errors++;
                                		cP*= 0.5*0.5*errorRate;
                                	} else {
                                        cP = 0.0;
                                        break;
                                	}
                            	} else {
                                	if (errors < 3){
                                		errors++;
                                		cP*= 0.5*0.5*errorRate*errorRate;
                                	} else {
                                        cP = 0.0;
                                        break;
                                	}
                            	}

                            }
                        }
                        TP = cP / eP;
                    } else {
                        List<Locus> mG = parents.mother.genes;
                        Iterator<Locus> cIt = cG.iterator();
                        Iterator<Locus> mIt = mG.iterator();
                        while (cIt.hasNext()) {
                            Locus l = cIt.next();
                            Locus lm = mIt.next();
                            HashMap<Integer, Double> h = aIt.next();
                            if (l.isUnknown() || lm.isUnknown()){
                                continue;	
                            }
                            
                            if (lm.hasAlleleCommon(l)) {
                                cP *= 0.5 * l.numInCommon(lm)* h.get(
                                        new Integer(lm.getUncommon(l)));
                            } else {
                            	if (errors < 3){
                            		errors++;
                            		cP*= 0.5*errorRate;
                            	} else {
                                    cP = 0.0;
                                    break;
                            	}
                            }
                        }
                        TP = cP / eP;
                    }
                    //return TP;
                } else {
                    if (!parents.father.isEmpty()) {
                        List<Locus> fG = parents.father.genes;
                        Iterator<Locus> cIt = cG.iterator();
                        Iterator<Locus> fIt = fG.iterator();
                        while (cIt.hasNext()) {
                            Locus l = cIt.next();
                            Locus lf = fIt.next();
                            HashMap<Integer, Double> h = aIt.next();
                            if(l.isUnknown()||lf.isUnknown()){
                            	continue;
                            }
                            
                            if (lf.hasAlleleCommon(l)) {
                                cP *= 0.5 * l.numInCommon(lf)*h.get(
                                        new Integer(lf.getUncommon(l)));
                            } else {
                            	if (errors < 3){
                            		errors++;
                            		cP*= 0.5*errorRate;
                            	} else {
                                    cP = 0.0;
                                    break;
                            	}
                            }
                        }
                        TP = cP / eP; //we return the liklihood ratio
                        //if  (cP>0.0){
                        //System.out.println("survival(father): "+(cP/eP));}
                    }
                }
                //System.out.println(toString());
                //System.out.println(TP);
            } else {
                TP = 1.0;//liklihood ratio, hence just ratio of two equal
            }
        }
        //System.out.println(TP);
        return TP;
    }

    private double initEmptyProb(List<HashMap<Integer, Double>> aFreqs) {
        //this shouldnt actually happen anymore,
        //since we gave responsibility for this to someone else
        double cP = 1.0;
        List<Locus> cG = child.genes;
        Iterator<Locus> cIt = cG.iterator();
        Iterator<HashMap<Integer, Double>> aIt = aFreqs.iterator();

        while (cIt.hasNext()) {
            Locus c = cIt.next();
            HashMap<Integer, Double> h = aIt.next();
            cP *= h.get(new Integer(c.a1)).doubleValue()
                    * h.get(new Integer(c.a2)).doubleValue();//dodgey
            }
        //return Math.pow(0.2*0.2, cG.size());
        return cP;//believe it or not, iteration is faster
    }
    public boolean Equals(Object o){
        if(child.Equals(((ChildParentsTriple)o).child)){
            //System.out.println(parents.isNull());

            if((parents.isNull())){
                if(((ChildParentsTriple)o).parents.isNull()){
                //System.out.println(parents.isNull());
                return true;
            } else {
                    return false;
            }
            } else if(((ChildParentsTriple)o).parents.isNull()){
                return false;
            }else {
            if ((parents.Equals(((ChildParentsTriple)o).parents))) {
                return true;
            } else {

                return false;
            }
            }
        } else {
        return false;
        }
    }
}
