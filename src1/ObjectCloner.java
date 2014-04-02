import java.io.*;
//import java.util.*;
//import java.awt.*;




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





public class ObjectCloner
{
    //serialized cloner, needs stuff to be serializable, pretty slow for anything we might want to do
    //clone is manually implemented now
    //not currently used
   // so that nobody can accidentally create an ObjectCloner object
   private ObjectCloner(){}
   // returns a deep copy of an object
   static public Object deepCopy(Object oldObj) throws Exception
   {
      ObjectOutputStream oos = null;
      ObjectInputStream ois = null;
      try
      {
         ByteArrayOutputStream bos = 
               new ByteArrayOutputStream(); // A
         oos = new ObjectOutputStream(bos); // B
         // serialize and pass the object
         oos.writeObject(oldObj);   // C
         oos.flush();               // D
         ByteArrayInputStream bin = 
               new ByteArrayInputStream(bos.toByteArray()); // E
         ois = new ObjectInputStream(bin);                  // F
         // return the new object
         return ois.readObject(); // G
      }
      catch(Exception e)
      {
         System.out.println("Exception in ObjectCloner = " + e);
         throw(e);
      }
      finally
      {
         oos.close();
         ois.close();
      }
   }
   
}
