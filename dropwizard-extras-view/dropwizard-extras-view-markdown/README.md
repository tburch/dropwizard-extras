##dropwizard-extras-view-markdown
dropwizard-extras-view-markdown is an addon for the [Dropwizard](http://www.dropwizard.io/) framework to enable rendering of [Markdown](http://daringfireball.net/projects/markdown/) templates. This is only supported in Dropwizard version 0.7.0 and greater.

##Enable Markdown views for your Dropwizard service:

Add the "dropwizard-extras-view-markdown" dependency:

	<dependency>
		<groupId>com.lowtuna.dropwizard-extras</groupId>
    	<artifactId>dropwizard-extras-view-markdown</artifactId>
    	<version>1.0.0-SNAPSHOT</version>
 	</dependency>

If you want to use a specific instance of a Markdown4jProcessor (so you can use your own Plugins, custom HTML rendering, etc.), add the ViewBundle with the Markdown4jProcessor instance in the initialize method of your Service class:

	public void initialize(Bootstrap<MyConfiguration> bootstrap) {
 		bootstrap.addBundle(new ConfiguredMarkdownViewBundle<MyConfiguration>() {
            @Override
            public Markdown4jProcessor getInstance(MyConfiguration configuration) {
                //either load the Markdown4jProcessor instance from your config or instantiate it here
                Markdown4jProcessor md = new Markdown4jProcessor()
                 		    .addHtmlAttribute("target", "_blank", "a")
                 		    .register(new MyPlugin());
                return md;
            }
        });
	}
	
If you don't need to have a specific instance of a Markdown4jProcessor, add the ViewBundle in the initialize method of your Service class:

	public void initialize(Bootstrap<MyConfiguration> bootstrap) {
		bootstrap.addBundle(new ViewBundle());
	}

Once added to your Bootstrap, Markdown views can be rendered like all other [Dropwizard Views](http://www.dropwizard.io/manual/views/).
