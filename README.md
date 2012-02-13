picotemplate
============

`picotemplate` is a simple, tiny C-Template workalike written in java
with the intention of being as high performance as possible.
`picotemplate` is very small, only three class files and is BSD licensed.

basic usage
-----------
			
First, create your template:
<code><pre>
<html>
	<body>
		This is my template. My favorite food is {{FOOD}}.
	</body>
</html>
</pre></code>


Import the required classes:
<code><pre>
import com.binarythought.picotemplate.Template;
import com.binarythought.picotemplate.TemplateDictionary;
</pre></code>


Create your template and template dictionary:
<code><pre>
Template template = new Template(new File("mytemplate.tpl"));
TemplateDictionary dict = new TemplateDictionary();
</pre></code>


Assign a value to the "food" variable (Unassigned variables are not shown):
<code><pre>
dict.put("food", "cookies");
</pre></code>


And parse your template:
<code><pre>
String result = template.parse(dict);
</pre></code>


And the result:
<code><pre>
<html>
	<body>
		This is my template. My favorite food is cookies.
	</body>
</html>
</pre></code>

advanced usage
--------------

picotemplate can selectively show areas of static content, called "sections".
It can also loop over these sections using child dictionaries. Consider the
following example:
<code><pre>
<html>
	<body>
		{{FAVORITE_SHOW}} is probably my favorite show.
		{{#GOODSHOWS}}
		{{SHOW}} is pretty good, too..
		{{/GOODSHOWS}}
	</body>
</html>
</pre></code>


Create your template and template dictionary as usual:
<code><pre>
Template template = new Template(new File("mytemplate.tpl"));
TemplateDictionary dict = new TemplateDictionary();
</pre></code>


Define our favorite show:
<code><pre>
dict.put("favorite_show", "Happy Days");
</pre></code>


Now show the section called "goodshows" (Sections are by default hidden, and
must be explicitly told to be shown):
<code><pre>
dict.show("goodshows");
</pre></code>


And add some shows for it to loop over:
<code><pre>
TemplateDictionary child1 = dict.createChild("goodshows");
child1.put("show", "M.A.S.H");
TemplateDictionary child2 = dict.createChild("goodshows");
child2.put("show", "A-Team");
</pre></code>


And the result:
<code><pre>
<html>
	<body>
		Happy Days is probably my favorite show.

		M.A.S.H is pretty good, too..

		A-Team is pretty good, too..
	</body>
</html>
</pre></code>
