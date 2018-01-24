package ch.ctiv.addressbook;

import javax.inject.Inject;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;

/**
 * @author Nicolas Regez
 * @since 23 Jan 2018
 */
public class Application extends ResourceConfig {

	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	@Inject
	public Application(ApplicationContext context) {

		logger.info("Register Jersey-Application 'Addressbook'");
		register(RequestContextFilter.class);
		packages("ch.ctiv.addressbook");
		register(ApiListingResource.class);
		register(SwaggerSerializers.class);
	}

}
