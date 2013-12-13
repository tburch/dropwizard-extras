##Enable [Handlebars](http://handlebarsjs.com/) views for your Dropwizard service:

Add the "dropwizard-extras-view-handlebars" dependency:

	<dependency>
		<groupId>com.lowtuna.dropwizard-extras</groupId>
    	<artifactId>dropwizard-extras-view-handlebars</artifactId>
    	<version>1.0.0-SNAPSHOT</version>
 	</dependency>

If you want to use a specific instance of Handlebars (so you can use your own Helpers, TemplateCache, etc.), add the ViewBundle with the HandlebarsInstance in the initialize method of your Service class:

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

Once enabled, Handlebars views can be rendered like all other [Dropwizard Views](http://dropwizard.codahale.com/manual/views/).
