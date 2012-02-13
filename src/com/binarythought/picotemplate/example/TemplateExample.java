package com.binarythought.picotemplate.example;

import com.binarythought.picotemplate.Template;
import com.binarythought.picotemplate.TemplateDictionary;
import java.io.File;


public class TemplateExample {
	public static void main(String args[]) throws Exception
	{
		Template template = new Template(new File("res/test.html"));	

		TemplateDictionary dict = new TemplateDictionary();
		dict.put("object1", "rifle");

		dict.show("section");
		TemplateDictionary d1 = dict.createChild("section");
		d1.put("object2", "fighting");
		TemplateDictionary d2 = dict.createChild("section");
		d2.put("object2", "fun");
		
		System.out.println("*** Parsed Template: ");
		System.out.println(template.parse(dict));

		long t = System.currentTimeMillis();
		template.parse(dict);
		System.out.println("1 Template: "+(System.currentTimeMillis()-t)+"ms");

		t = System.currentTimeMillis();
		for(int i=0;i < 1000; i++){
			template.parse(dict);
		}
		System.out.println("1000 Templates: "+(System.currentTimeMillis()-t)+"ms");

		t = System.currentTimeMillis();
		for(int i=0;i < 10000; i++){
			template.parse(dict);
		}
		System.out.println("10000 Templates: "+(System.currentTimeMillis()-t)+"ms");

		t = System.currentTimeMillis();
		for(int i=0;i < 100000; i++){
			template.parse(dict);
		}
		System.out.println("100000 Templates: "+(System.currentTimeMillis()-t)+"ms");
	}

}
