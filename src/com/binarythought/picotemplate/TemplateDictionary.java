package com.binarythought.picotemplate;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.Collections;


/**
 * TemplateDictionary is used to assign variables and show sections
 * inside templates. It is functionally similar to a HashMap, although
 * not a decendent. <b>All methods that use identifying keys under this object
 * are case-insensitive.</b> This means that "MY_VAR" is the same as "my_var"
 * for both section names and variable names.
 * <p>At the most basic level, TemplateDictionary can be used with
 * Template to create simple fill-in-the-blank style templates.
 * <p>More advanced functionality is available, allowing developers
 * to toggle shown sections and loop over sections using child dictionaries.
 * @see Template
 */ 
public class TemplateDictionary
{
	private Map<String,String> dictionary;
	private Map<String,List<TemplateDictionary>> children;
	private Set<String> sections;
	private TemplateDictionary parent;

	/**
	 * Instantiate an empty TemplateDictionary.
	 */ 
	public TemplateDictionary(){ this(null); }


	private TemplateDictionary(TemplateDictionary parent)
	{
		this.parent = parent;

		dictionary = new HashMap<String,String>();
		children = new HashMap<String,List<TemplateDictionary>>();
		sections = new HashSet<String>();
	}


	/**
	 * Get the parent dictionary to this one, or null if one does not exist.
	 * @return This dictionary's parent dictionary.
	 */
	public TemplateDictionary getParent(){ return parent; }


	/**
	 * Show a specific section. This method is case-insensitive.
	 * @param name The section to show.
	 */
	public void show(String name){ sections.add(name.toUpperCase()); }


	/**
	 * Hide a specific section. This method is case-insensitive.
	 * @param name The section to hide.
	 */
	public void hide(String name){ sections.remove(name.toUpperCase()); }


	/**
	 * Checks if a section is hidden. This method is case-insensitive.
	 * @param name The section name checking against.
	 * @return True if a section is hidden, false if it is not.
	 */
	public boolean isHidden(String name){ return !sections.contains(name.toUpperCase()); }


	/**
	 * Checks if a section is shown. This method is case-insensitive.
	 * @param name The section name checking against.
	 * @return True if this section is shown, false if it is not.
	 */ 
	public boolean isShown(String name){ return sections.contains(name.toUpperCase()); }


	/**
	 * Provides a list of children identified by a case-insensitive section name.
	 * @param name The section name checking against.
	 * @return List of children dictionaries identified by specific section name or null if there are none.
   */
	public List<TemplateDictionary> getChild(String name){
		if(children.containsKey(name.toUpperCase())){
			return children.get(name.toUpperCase());
		} else { return null; }
	}


	/**
	 * Remove all of the child dictionaries under this one identified by 
	 * a specific section name.
	 * @param name Case-insensitive section name's children to remove.
	 */
	public void removeChildren(String name){
		children.remove(name.toUpperCase());
	}


	/**
	 * Create a child dictionary underneath this one for use with a specific
	 * section.
	 * @param name Case-insensitive section name for the new dictionary.
	 * @return TemplateDictionary for specific section.
	 */
	public TemplateDictionary createChild(String name){
		TemplateDictionary child = new TemplateDictionary(this);

		if(!children.containsKey(name.toUpperCase())){
			List<TemplateDictionary> list = new LinkedList<TemplateDictionary>();
			children.put(name.toUpperCase(), list);
		}

		children.get(name.toUpperCase()).add(child);
		return child;
	}


	/**
	 * Gets the value of a variable by key. This method also ascends into parent
	 * dictionaries looking for the key.
	 * @param key Case-insensitive Key to look for.
	 * @return Value of key as string, or an empty string if the key is not found.
	 */
	public String get(String key)
	{
		if(dictionary.containsKey(key.toUpperCase())){
			return dictionary.get(key.toUpperCase());
		} else if(parent != null){
			return parent.get(key);
		} else {
			return "";
		}
	}


	/**
	 * Set the value of a variable.
	 * @param key Case-insensitive key that identifies this variable.
	 * @param val Value of this variable.
	 */
	public void put(String key, String val)
	{
		dictionary.put(key.toUpperCase(), val);
	}


	/**
	 * Remove a variable.
	 * @param key Case-insensitive key to remove.
	 */
	public void remove(String key)
	{
		dictionary.remove(key.toUpperCase());
	}	
}
