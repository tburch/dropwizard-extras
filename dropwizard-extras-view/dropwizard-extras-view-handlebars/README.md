##dropwizard-extras-view-handlebars
dropwizard-extras-view-handlebars is an addon for the [Dropwizard](http://www.dropwizard.io/) framework to enable rendering of [Handlebars](http://handlebarsjs.com/) templates. This is only supported in Dropwizard version 0.7.0 and greater.

##Usage
Add the "dropwizard-extras-view-handlebars" dependency:

	<dependency>
		<groupId>com.lowtuna.dropwizard-extras</groupId>
    	<artifactId>dropwizard-extras-view-handlebars</artifactId>
    	<version>1.0.0-SNAPSHOT</version>
 	</dependency>

If you want to use a specific instance of Handlebars (so you can use your own Helpers, TemplateCache, etc.), add the ViewBundle with the Handlebars instance in the initialize method of your Service class:

	public void initialize(Bootstrap<MyConfiguration> bootstrap) {
 		Handlebars hbs = 
 			new Handlebars()
 			.with(new ClassPathTemplateLoader(StringUtils.EMPTY, StringUtils.EMPTY))
 			.with(new HighConcurrencyTemplateCache());
 		HandlebarsViewRenderer hsbRenderer = new HandlebarsViewRenderer(hbs);
		bootstrap.addBundle(new ViewBundle(ImmutableList.of(hsbRenderer)));
	}
	
If you don't need to have a specific instance of Handlebars, add the ViewBundle in the initialize method of your Service class:

	public void initialize(Bootstrap<MyConfiguration> bootstrap) {
		bootstrap.addBundle(new ViewBundle());
	}

Once added to your Bootstrap, Handlebars views can be rendered like all other [Dropwizard Views](http://www.dropwizard.io/manual/views/).
