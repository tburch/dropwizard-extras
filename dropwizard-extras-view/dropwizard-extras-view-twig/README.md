##dropwizard-extras-view-twig
dropwizard-extras-view-twig is an addon for the [Dropwizard](http://www.dropwizard.io/) framework to enable rendering of [Twig](http://jtwig.org/) templates. This is only supported in Dropwizard version 0.7.0 and greater.

##Enable twig views for your Dropwizard service:

Add the "dropwizard-extras-view-twig" dependency:

	<dependency>
		<groupId>com.lowtuna.dropwizard-extras</groupId>
    	<artifactId>dropwizard-extras-view-twig</artifactId>
    	<version>1.0.0-SNAPSHOT</version>
 	</dependency>

If you want to use a specific instance of a Twig AbstractFunctionRepository (so you can use custom [functions](http://jtwig.org/documentation/add-new-function/)), add the ViewBundle with the AbstractFunctionRepository instance in the initialize method of your Service class:

	public void initialize(Bootstrap<MyConfiguration> bootstrap) {
		bootstrap.addBundle(new ConfiguredTwigViewBundle<MyConfiguration>() {
            @Override
            public AbstractFunctionRepository getInstance(MyConfiguration configuration) {
                //either load the AbstractFunctionRepository instance from your config or instantiate it here
                AbstractFunctionRepository fr = new DefaultFunctionRepository();
                return fr;
            }
        });
	}
	
If you don't need to have a specific instance of a AbstractFunctionRepository, add the ViewBundle in the initialize method of your Service class:

	public void initialize(Bootstrap<MyConfiguration> bootstrap) {
		bootstrap.addBundle(new ViewBundle());
	}

##Rendering Twig views

Once added to your Bootstrap, Twig views can be rendered similar to [Dropwizard Views](http://dropwizard.io/manual/views.html). All of the View's declared fields are exposed to the template.


