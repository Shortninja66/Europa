package net.shortninja.europa.scoreboard.attribute;

public class Placeholder
{
	private String placeholder;
	
	public Placeholder(String placeholder)
	{
		this.placeholder = placeholder;
	}
	
	/**
	 * @return The current placeholder for this instance.
	 */
	public String getPlaceholder()
	{
		return placeholder;
	}
	
	/**
	 * The given object is converted to a string with Object#toString().
	 * 
	 * @param original The object that should contain the placeholder and will have
	 * the placeholder replaced.
	 * @param replacement The string to substitute the placeholder for.
	 * @return The string with any replaced placeholders. 
	 */
	public String transform(Object original, String replacement)
	{
		return replace(original.toString(), placeholder, replacement, -1);
	}
	
	/**
	 * @param placeholder The placeholder to update to.
	 */
	public void setPlaceholder(String placeholder)
	{
		this.placeholder = placeholder;
	}
	
	/**
	 * Attempts to replace the given string with the replacement string. Taken from
	 * org.apache.commons.lang3.StringUtils and slightly modified.
	 * 
     * @param text Text to search and replace in, may be null.
     * @param searchString The String to search for, may be null.
     * @param replacement The String to replace it with, may be null.
     * @param max Maximum number of values to replace, or -1 if no maximum.
     * @return The text with any replacements processed.
	 */
	private String replace(final String text, final String searchString, final String replacement, int max)
	{
		if(isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0)
		{
			return text;
		}
		
		int start = 0;
		int end = text.indexOf(searchString, start);
		final int replLength = searchString.length();
		int increase = replacement.length() - replLength;
		increase = increase < 0 ? 0 : increase;
		increase *= max < 0 ? 16 : max > 64 ? 64 : max;
		final StringBuilder buf = new StringBuilder(text.length() + increase);
		
		if(end == - 1)
		{
			return text;
		}
		
		while(end != - 1)
		{
			buf.append(text.substring(start, end)).append(replacement);
			start = end + replLength;
			
			if(--max == 0)
			{
				break;
			}
			
			end = text.indexOf(searchString, start);
		}
		
		return buf.append(text.substring(start)).toString();
	}
    
	/**
	 * Checks if the given char sequence is empty. Taken from 
	 * org.apache.commons.lang3.StringUtils.
	 * 
     * @param cs The CharSequence to check, may be null.
     * @return If the CharSequence is empty or null.
	 */
    private boolean isEmpty(CharSequence chars)
    {
    	return chars == null || chars.length() == 0;
    }
}