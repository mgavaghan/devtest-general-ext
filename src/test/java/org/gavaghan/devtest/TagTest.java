package org.gavaghan.devtest;

import org.junit.Assert;
import org.junit.Test;

public class TagTest
{
	@Test
	public void testLegalNoArgs() 
	{
		Tag tag;
		
		tag = Tag.read("ABC");
		Assert.assertEquals("ABC", tag.getName());
		Assert.assertEquals(0, tag.getArguments().size());
		Assert.assertEquals(3, tag.getLength());
		
		tag = Tag.read("ABC ");
		Assert.assertEquals("ABC", tag.getName());
		Assert.assertEquals(0, tag.getArguments().size());
		Assert.assertEquals(3, tag.getLength());
		
		tag = Tag.read("ABC()");
		Assert.assertEquals("ABC", tag.getName());
		Assert.assertEquals(0, tag.getArguments().size());
		Assert.assertEquals(5, tag.getLength());
		
		tag = Tag.read("ABC() ");
		Assert.assertEquals("ABC", tag.getName());
		Assert.assertEquals(0, tag.getArguments().size());
		Assert.assertEquals(5, tag.getLength());
		
		tag = Tag.read("ABC(   ) ");
		Assert.assertEquals("ABC", tag.getName());
		Assert.assertEquals(0, tag.getArguments().size());
		Assert.assertEquals(8, tag.getLength());
	}
	
	@Test
	public void testLegalArgs() 
	{
		Tag tag;
		
		tag = Tag.read("ABC(alpha)");
		Assert.assertEquals("ABC", tag.getName());
		Assert.assertEquals(1, tag.getArguments().size());
		Assert.assertEquals(10, tag.getLength());
		
		tag = Tag.read("ABC(alpha) ");
		Assert.assertEquals("ABC", tag.getName());
		Assert.assertEquals(1, tag.getArguments().size());
		Assert.assertEquals(10, tag.getLength());
		
		tag = Tag.read("ABC(alpha,beta) ");
		Assert.assertEquals("ABC", tag.getName());
		Assert.assertEquals(2, tag.getArguments().size());
		Assert.assertEquals(15, tag.getLength());
		
		tag = Tag.read("ABC( alpha , beta ) ");
		Assert.assertEquals("ABC", tag.getName());
		Assert.assertEquals(2, tag.getArguments().size());
		Assert.assertEquals(19, tag.getLength());
	}
	
	@Test
	public void testIllegal() 
	{
		Tag tag;
		
		tag = Tag.read("");
		Assert.assertNull(tag.getName());
		Assert.assertNull(tag.getArguments());
		Assert.assertEquals(0, tag.getLength());
		
		tag = Tag.read("#ABC(alpha)");
		Assert.assertNull(tag.getName());
		Assert.assertNull(tag.getArguments());
		Assert.assertEquals(1, tag.getLength());
		
		tag = Tag.read("ABC(alpha");
		Assert.assertNull(tag.getName());
		Assert.assertNull(tag.getArguments());
		Assert.assertEquals(4, tag.getLength());
		
		tag = Tag.read("ABC(alpha, beta");
		Assert.assertNull(tag.getName());
		Assert.assertNull(tag.getArguments());
		Assert.assertEquals(4, tag.getLength());
	}
	
	@Test
	public void testChained() 
	{
		Tag tag;
		
		tag = Tag.read("ABC(alpha)   @@DEF(beta)");
		Assert.assertEquals("ABC", tag.getName());
		Assert.assertEquals(1, tag.getArguments().size());
		Assert.assertEquals(10, tag.getLength());
		
		tag = Tag.read("ABC(alpha,beta)   @@DEF(delta,gamma)");
		Assert.assertEquals("ABC", tag.getName());
		Assert.assertEquals(2, tag.getArguments().size());
		Assert.assertEquals(15, tag.getLength());
	}
}
