import java.io.*;
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



public class CEsort {
	  private CEsort(){}
	   static public int sort(List list, int k, int left, int right)// throws Exception
	   { // not stable
		   int r=k;
		   if(right > left){
		    int pivot = (int)Math.floor(left+(right-left)/2.0);
		    int newPivot = partition(left,right,pivot,list);
		    if (newPivot==k) {//do nothing
		    	double scK = ((StatePermutation)list.get(k)).getScore();
		    	int f=0;
		    	while(f==0 & (r+1)<list.size()){
		    		if(scK == ((StatePermutation)list.get(r+1)).getScore()){
		    			r++;
		    		} else {
		    			f=1;
		    		}
		    	}
		    	return r;
		    } else if (newPivot < k){
		    	return sort(list,k,newPivot+1,right);
		    } else {
		    	return sort(list, k,left, newPivot-1);
		    }
		   }
		   return k; //ok this is broken
	   }
	   static private int partition(int left, int right, int pivot, List<StatePermutation> list) {
//		   left;
//		   right;
		   int storeIndex = left;
//		   storeIndex=left;
		   StatePermutation pivotObj = list.get(pivot);
		   
//		   pivotVal = s(pivot);
//		   pivotI=I(pivot);
//		   temp=s(pivot);
		   StatePermutation tempObj = list.get(pivot);
		   list.set(pivot,list.get(right));
//		   s(pivot)=s(right);
		   list.set(right, tempObj);
//		   s(right)=temp;
//		   tempI=I(pivot);
//		   I(pivot)=I(right);
//		   I(right)=tempI;
		   for (int i=left; i<right;i++){
			   if(pivotObj.compareTo(list.get(i))==1){
				   tempObj = list.get(storeIndex);
				   list.set(storeIndex, list.get(i));
				   list.set(i, tempObj);
				   storeIndex++;
			   }
		   }
//		   for i=left:(right-1)
//		       if(s(i)<pivotVal),
//		           temp=s(storeIndex);
//		           s(storeIndex)=s(i);
//		           s(i)=temp;
//		           tempI=I(storeIndex);
//		           I(storeIndex)=I(i);
//		           I(i)=tempI;
//		           storeIndex++;
//		       end
//		   end
		   tempObj = list.get(storeIndex);
		   list.set(storeIndex, list.get(right));
		   list.set(right, tempObj);
//		   temp=s(storeIndex);
//		   s(storeIndex)=s(right);
//		   s(right)=temp;
//		   tempI=I(storeIndex);
//		   I(storeIndex)=I(right);
//		   I(right)=tempI;
//		   p=storeIndex;
//		   x=s;
//		   y=I;
		   
		   return storeIndex;
	   }
	   
	   static public void sort(List list, int left, int right)// throws Exception
	   { // not stable
		   if(right > left){
		    int pivot = (int)Math.floor(left+(right-left)/2.0);
		    int newPivot = partition(left,right,pivot,list);
		    	sort(list,newPivot+1,right);
		    	sort(list,left,newPivot-1);
		   }
	   }

}
