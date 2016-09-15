package com.akuacom.pss2.data.usage.calcimpl;
 
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
 
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
 
public class AkuaCollectionUtils extends CollectionUtils {
        
         /** 
     * Selects all elements from input collection which match the given predicate
     * into an output collection.
     * <p>
     * A <code>null</code> predicate matches no elements.
     * 
     * @param inputCollection  the collection to get the input from, may not be null
     * @param predicate  the predicate to use, may be null
     * @return the elements matching the predicate (new list)
     * @throws NullPointerException if the input collection is null
     */
   public static Collection select(Collection inputCollection, Predicate predicate, int size) {
       ArrayList answer = new ArrayList(size);
       select(inputCollection, predicate, answer, size);
       return answer;
   }
   
   /** 
     * Selects all elements from input collection which match the given predicate
     * and adds them to outputCollection.
     * <p>
     * If the input collection or predicate is null, there is no change to the 
     * output collection.
     * 
     * @param inputCollection  the collection to get the input from, may be null
     * @param predicate  the predicate to use, may be null
     * @param outputCollection  the collection to output into, may not be null
     */
   public static void select(Collection inputCollection, Predicate predicate, Collection outputCollection, int size) {
       if (inputCollection != null && predicate != null) {
           for (Iterator iter = inputCollection.iterator(); iter.hasNext();) {
               if(outputCollection.size()>=size){
                       break;
               }
               Object item = iter.next();
               if (predicate.evaluate(item)) {
                   outputCollection.add(item);
               }
           }
       }
   }
 
}