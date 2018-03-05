package org.gavaghan.devtest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A specialized TaggedDocument that implements the ITagFragment interface. This
 * allows LIST tags to convert contained tags into ListDocument substructure.
 * 
 * @author <a href="mailto:mike@gavaghan.org">Mike Gavaghan</a>
 */
public class ListDocument extends TaggedDocument implements ITagFragment
{
	/** The list of tag names a list document understands. */
	static private final Set<String> sUnderstood = new HashSet<String>();

	static
	{
		sUnderstood.add("LIST");
		sUnderstood.add("ENDLIST");
	}

	/** The list tag that encloses this document. */
	private Tag mListTag;

	/**
	 * Create a new TaggedDocument.
	 * 
	 * @param frags
	 * @param trailer
	 */
	ListDocument(Tag listTag, List<ITagFragment> frags, String trailer)
	{
		super(frags, trailer);

		mListTag = listTag;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gavaghan.devtest.ITagFragment#getContent()
	 */
	@Override
	public String getContent()
	{
		return getTrailer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gavaghan.devtest.ITagFragment#getTag()
	 */
	@Override
	public Tag getTag()
	{
		return mListTag;
	}

	/**
	 * 
	 * @param rawFrags
	 * @return
	 */
	static private List<ITagFragment> read(List<ITagFragment> rawFrags)
	{
		List<ITagFragment> cookedFrags = new ArrayList<ITagFragment>();

		// keep processing raw fragments until we run out
		while (!rawFrags.isEmpty())
		{
			ITagFragment frag = rawFrags.remove(0); // get and remove next
			Tag tag = frag.getTag();

			// if it's a LIST, recurse and create a sublist
			if (tag.getName().equals("LIST"))
			{
				List<ITagFragment> listFrags = read(rawFrags);

				ListDocument listDoc = new ListDocument(tag, listFrags, frag.getContent());
				cookedFrags.add(listDoc);
			}
			// if this is the end of this LIST, return it
			else if (tag.getName().equals("ENDLIST"))
			{
				cookedFrags.add(frag);
				break;
			}
			// otherwise, just add it
			else
			{
				cookedFrags.add(frag);
			}
		}

		return cookedFrags;
	}

	/**
	 * Convert text into a ListDocument.
	 * 
	 * @param text
	 * @return
	 */
	static public ListDocument read(String text)
	{
		TaggedDocument doc = TaggedDocument.read(text, sUnderstood);
		List<ITagFragment> docFrags = new ArrayList<ITagFragment>();
		List<ITagFragment> frags;

		docFrags.addAll(doc.getTagFragments());
		frags = read(docFrags);

		return new ListDocument(null, frags, doc.getTrailer());
	}
}
