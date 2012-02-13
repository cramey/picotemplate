picotemplate
============

`picotemplate` is a simple, tiny C-Template workalike written in java
with the intention of being as high performance as possible.
`picotemplate` is very small, only three class files and is BSD licensed.

basic usage
-----------
			
First, create your template:
```html
<html>
	<body>
		This is my template. My favorite food is {{FOOD}}.
	</body>
</html>
```


Import the required classes:
```java
import com.binarythought.picotemplate.Template;
import com.binarythought.picotemplate.TemplateDictionary;
```


Create your template and template dictionary:
```java
Template template = new Template(new File("mytemplate.tpl"));
TemplateDictionary dict = new TemplateDictionary();
```


Assign a value to the "food" variable (Unassigned variables are not shown):
```java
dict.put("food", "cookies");
```


And parse your template:
```java
String result = template.parse(dict);
```


And the result:
```html
<html>
	<body>
		This is my template. My favorite food is cookies.
	</body>
</html>
```

advanced usage
--------------

picotemplate can selectively show areas of static content, called "sections".
It can also loop over these sections using child dictionaries. Consider the
following example:
```html
<html>
	<body>
		{{FAVORITE_SHOW}} is probably my favorite show.
		{{#GOODSHOWS}}
		{{SHOW}} is pretty good, too..
		{{/GOODSHOWS}}
	</body>
</html>
```


Create your template and template dictionary as usual:
```java
Template template = new Template(new File("mytemplate.tpl"));
TemplateDictionary dict = new TemplateDictionary();
```


Define our favorite show:
```java
dict.put("favorite_show", "Happy Days");
```

Now show the section called "goodshows" (Sections are by default hidden, and
must be explicitly told to be shown):
```java
dict.show("goodshows");
```

And add some shows for it to loop over:
```java
TemplateDictionary child1 = dict.createChild("goodshows");
child1.put("show", "M.A.S.H");
TemplateDictionary child2 = dict.createChild("goodshows");
child2.put("show", "A-Team");
```


And the result:
```html
<html>
	<body>
		Happy Days is probably my favorite show.
		
		M.A.S.H is pretty good, too..
		
		A-Team is pretty good, too..
	</body>
</html>
```
