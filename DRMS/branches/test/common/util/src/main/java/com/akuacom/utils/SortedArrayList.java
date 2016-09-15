/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.utils.SortedArrayList.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * The Class SortedArrayList.
 */
public class SortedArrayList<E extends Comparable<? super E>> extends ArrayList<E>
{
	
	/* (non-Javadoc)
	 * @see java.util.ArrayList#add(java.lang.Object)
	 */
	public boolean add(E o)
	{
	    int index = Collections.binarySearch(this, o);
	    
	    if (index < 0) 
	    {
	        super.add(-index-1, o);
	    }
	    else
	    {
	    	super.add(index, o);
	    }
	    
	    return true;
	}

	/* (non-Javadoc)
	 * @see java.util.ArrayList#add(int, java.lang.Object)
	 */
	public void add(int index, E o)
	{
	   throw new UnsupportedOperationException();
	}

	/**
	 * Contains.
	 * 
	 * @param o the o
	 * 
	 * @return true, if successful
	 */
	public boolean contains(E o)
	{
	    int index = Collections.binarySearch(this, o);
	    if(index >= 0)
	    {
	    	return true;
	    }
	    else
	    {
	    	return false;
	    }
	}
	
	/**
	 * Index of.
	 * 
	 * @param o the o
	 * 
	 * @return the int
	 */
	public int indexOf(E o)
	{
	    int index = Collections.binarySearch(this, o);
	    if(index >= 0)
	    {
	    	// walk backwards until we find the first one
	    	while(index > 0)
	    	{
	    		if(get(index -1).equals(o))
	    		{
	    			index--;
	    		}
	    		else
	    		{
	    			break;
	    		}
	    	}
	    	return index;
	    }
	    else
	    {
	    	return -1;
	    }
	}
	
	/**
	 * Last index of.
	 * 
	 * @param o the o
	 * 
	 * @return the int
	 */
	public int lastIndexOf(E o)
	{
	    int index = Collections.binarySearch(this, o);
	    if(index >= 0)
	    {
	    	// walk forewards until we find the last one
	    	int size = size();
	    	while(index < size - 1)
	    	{
	    		if(get(index+1).equals(o))
	    		{
	    			index++;
	    		}
	    		else
	    		{
	    			break;
	    		}
	    	}
	    	return index;
	    }
	    else
	    {
	    	return -1;
	    }
	}

	/* (non-Javadoc)
	 * @see java.util.ArrayList#addAll(java.util.Collection)
	 */
	public boolean addAll(Collection<? extends E> c)
	{
		boolean rv = false;
		for(E e: c)
		{
			add(e);
			rv = true;
		}
		return rv;
	}

	/* (non-Javadoc)
	 * @see java.util.ArrayList#addAll(int, java.util.Collection)
	 */
	public boolean addAll(int index, Collection<? extends E> c)
	{
		throw new UnsupportedOperationException();
	}
}
