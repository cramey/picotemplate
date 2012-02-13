package com.binarythought.picotemplate;


/**
 * Groups of TemplateNodes compose a template once it's compiled. 
 * This class shouldn't be used directly.
 * @see Template
 */
public class TemplateNode
{
	/** Plain node type */
	public static final int PLAIN = 0;
	/** Variable node type */
	public static final int VARIABLE = 1;
	/** Start of section node type */
	public static final int SECTION_START = 2;
	/** End of section node type */
	public static final int SECTION_END = 3;

	private int nodeType;
	private String content;


	/**
	 * Instantiate new node.
	 * @param nodeType Type of node
	 * @param content Content of this node.
	 */
	public TemplateNode(int nodeType, String content)
	{
		this.nodeType = nodeType;
		this.content = content;
	}


	/**
	 * Get the type of this node
	 * @return Type of this node.
	 */
	public int getNodeType(){ return nodeType; }


	/**
	 * Get the content of this node.
	 * @return Content of this node.
	 */
	public String getContent(){ return content; }
}
