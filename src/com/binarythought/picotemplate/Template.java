package com.binarythought.picotemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.FileReader;
import java.io.File;

/**
 * Template is the base class for picotemplate. It it used to generate
 * html or text from a supplied template and dictionary. 
 * <p>Basic usage:
 * <p><code>Template template = new Template("I like {{FOOD}}.");<br>
 * TemplateDictionary dictionary = new TemplateDictionary();<br>
 * dictionary.put("food", "cookies");<br>
 * String result = template.parse(dictionary);</code>
 * <p>Value of result : <code>I like cookies.</code>
 */
public class Template {
	private static final Pattern pattern = Pattern.compile(
		"\\{\\{(#|/){0,1}(\\w+)\\}\\}", Pattern.CANON_EQ
	);

	private TemplateNode parsedTemplate[];
	private String originalTemplate;


	/**
	 * Instantiate and compile a new template with the template provided.
	 * @param template String containing the template.
	 */
	public Template(String template) throws Exception
	{
		this.originalTemplate = template;
		this.parsedTemplate = initTemplate(template);
	}


	/**
	 * Instantiate and compile a new template with the template provided.
	 * @param file File containing the template.
	 */
	public Template(File file) throws Exception
	{
		if(file == null || !file.canRead()){
			throw new Exception("Cannot read "+file.getName());
		}
	
		FileReader reader = new FileReader(file);
		StringBuilder b = new StringBuilder();

		char buf[] = new char[1024];
		for(int i=0; (i = reader.read(buf)) != -1;){
			b.append(buf, 0, i);
		}

		reader.close();
		this.originalTemplate = b.toString();
		this.parsedTemplate = initTemplate(this.originalTemplate);
	}


	private static TemplateNode[] initTemplate(String template) throws Exception
	{
		ArrayList<TemplateNode> parsedTemplate = new ArrayList<TemplateNode>();
		Stack<String> sections = new Stack<String>();
		Matcher match = pattern.matcher(template);
		int lastpos = 0;

		while(match.find()){
			String segment = template.substring(lastpos,match.start());
			if(segment.length() > 0){
				parsedTemplate.add(new TemplateNode(TemplateNode.PLAIN, segment));
			}

			if("#".equals(match.group(1))){
				sections.push(match.group(2));
				parsedTemplate.add(
					new TemplateNode(TemplateNode.SECTION_START, match.group(2))
				);
			} else if("/".equals(match.group(1))){
				parsedTemplate.add(
					new TemplateNode(TemplateNode.SECTION_END, match.group(2))
				);
				if(sections.empty() || !sections.pop().equals(match.group(2))){
					throw new Exception("Out of turn section termation at "+match.start());
				}
			} else {
				parsedTemplate.add(
					new TemplateNode(TemplateNode.VARIABLE, match.group(2))
				);	
			}
			lastpos = match.end();
		}

		String segment = template.substring(lastpos, template.length());
		if(segment.length() > 0){
			parsedTemplate.add(new TemplateNode(TemplateNode.PLAIN, segment));
		}

		if(!sections.empty()){
			throw new Exception("Unterminated section in template");
		}

		return parsedTemplate.toArray(new TemplateNode[0]);
	}


	/**
	 * Parse this template with the provided dictionary and output it to a string.
	 * <p><b>This method is threadsafe.</b>
	 * @param dict Template dictionary to parse against.
	 * @return String containing the parsed result of the template.
	 */
	public String parse(TemplateDictionary dict)
	{
		StringBuilder b = new StringBuilder();

		if(dict != null){ parseTemplate(dict, b, parsedTemplate, 0); }
		else { parseTemplate(b, parsedTemplate); }

		return b.toString();
	}


	private static int parseTemplate(StringBuilder output, TemplateNode template[])
	{
		for(int i = 0; i < template.length; i++){
			if(template[i].getNodeType() == TemplateNode.PLAIN){
				output.append(template[i].getContent());
			}
		}
		
		return 0;
	}


	private static int parseTemplate(TemplateDictionary dict, StringBuilder output, TemplateNode template[], int i)
	{
		for(;i < template.length; i++){
			switch(template[i].getNodeType()){
				case TemplateNode.PLAIN:
					output.append(template[i].getContent());
				break;

				case TemplateNode.VARIABLE:
					output.append(dict.get(template[i].getContent()));
				break;

				case TemplateNode.SECTION_START:
					if(dict.isShown(template[i].getContent())){
						List<TemplateDictionary> l = (
							dict.getParent() == null ? dict.getChild(template[i].getContent()) :
								dict.getParent().getChild(template[i].getContent())
						);

						if(l == null){
							i = parseTemplate(dict, output, template, i+1);
						} else {
							int last = 0;
							for(TemplateDictionary d : l){
								last = parseTemplate(d, output, template, i+1);
							}	
							i = last;
						}
					} else {
						for(int c=1; c > 0;){
							i++;
							switch(template[i].getNodeType()){
								case TemplateNode.SECTION_START: c++; break;
								case TemplateNode.SECTION_END: c--; break;
							}
						}
					}
				break;

				case TemplateNode.SECTION_END: return i;
			}
		}
		return 0;
	}
}
