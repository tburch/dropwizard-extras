##Enable [Markdown](http://daringfireball.net/projects/markdown/) views for your Dropwizard service:

Add the "dropwizard-extras-view-markdown" dependency:

	<dependency>
		<groupId>com.lowtuna.dropwizard-extras</groupId>
    	<artifactId>dropwizard-extras-view-markdown</artifactId>
    	<version>1.0.0-SNAPSHOT</version>
 	</dependency>

If you want to use a specific instance of a Markdown4jProcessor (so you can use your own Plugins, custom HTML rendering, etc.), add the ViewBundle with the Markdown4jProcessor instance in the initialize method of your Service class:

	public void initialize(Bootstrap<MyConfiguration> bootstrap) {
 		Markdown4jProcessor md =
 			new Markdown4jProcessor().addHtmlAttribute("target", "_blank", "a");
 		MarkdownViewRenderer mdRenderer = new MarkdownViewRenderer(md);
		bootstrap.addBundle(new ViewBundle(ImmutableList.of(mdRenderer)));
	}
	
If you don't need to have a specific instance of a Markdown4jProcessor, add the ViewBundle in the initialize method of your Service class:

	public void initialize(Bootstrap<MyConfiguration> bootstrap) {
		bootstrap.addBundle(new ViewBundle());
	}

Once enabled, Markdown views can be rendered like all other [Dropwizard Views](http://dropwizard.codahale.com/manual/views/).
