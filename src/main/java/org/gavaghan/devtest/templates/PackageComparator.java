package org.gavaghan.devtest.templates;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Compare one package name to another.
 * 
 * java/javax/org/com
 * 
 * @author <a href="mailto:mike.gavaghan@ca.com">Mike Gavaghan</a>
 */
class PackageComparator implements Comparator<String>
{
	/** First component ordering precedence. */
	static private final List<String> sPrecedence = new ArrayList<String>();

	static
	{
		sPrecedence.add("java");
		sPrecedence.add("javax");
		sPrecedence.add("org");
		sPrecedence.add("com");
	}

	/**
	 * Get first component of a package name,
	 * 
	 * @param pack
	 * @return
	 */
	private String getFirstComponent(String pack)
	{
		int dot = pack.indexOf('.');
		if (dot < 0) return pack;
		return pack.substring(0, dot);
	}

	/**
	 * Find the precendence of a component.
	 * 
	 * @param comp
	 * @return
	 */
	private int findPrecedence(String comp)
	{
		int i;

		for (i = 0; i < sPrecedence.size(); i++)
		{
			if (sPrecedence.get(i).equals(comp)) break;
		}

		return i;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(String left, String right)
	{
		if (left == null)
		{
			return (right == null) ? 0 : +1;
		}

		if (right == null) return -1;

		// get first component
		String leftFirst = getFirstComponent(left);
		String rightFirst = getFirstComponent(right);
		
		// get precedence
		int leftPre = findPrecedence(leftFirst);
		int rightPre = findPrecedence(rightFirst);
		
		if (leftPre < rightPre)  return -1;
		if (leftPre > rightPre)  return +1;

		return left.compareToIgnoreCase(right);
	}
}
