package ch.ctiv.addressbook;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.util.Json;

/**
 * @author Nicolas Regez
 * @since 23 Jan 2018
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages={
		"ch.ctiv.addressbook"})
@EnableJpaAuditing
@ComponentScan(basePackages={
		"ch.ctiv.addressbook"})
@PropertySources({
	@PropertySource(value = "classpath:/default.properties", ignoreResourceNotFound = false),
	@PropertySource(value = "classpath:/local.properties", ignoreResourceNotFound = true),
	@PropertySource(value = "file:${config.file}", ignoreResourceNotFound = true)})
public class ApplicationConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(ApplicationConfiguration.class);

	@Bean
	public BeanConfig swaggerConfig(@Value("${local.swagger.base.url}") String baseUrl) {

		final String PACKAGE = "ch.ctiv.addressbook";

		String scheme = baseUrl.split("://")[0];
		String host = baseUrl.split("://")[1].split("/")[0];
		String basePath = baseUrl.split("://")[1].replace(host, "");
		logger.info("Register SWAGGER documentation with scheme '{}', host '{}', base path '{}' and package '{}'", scheme, host, basePath, PACKAGE);

		Json.mapper().setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

		BeanConfig config = new BeanConfig();
		config.setTitle("Addressbook");
		config.setDescription("Simple Spring JPA Tutorial");
		config.setSchemes(new String[] {scheme});
		config.setHost(host);
		config.setBasePath(basePath);
		config.setResourcePackage(PACKAGE);
		config.setScan(true);
		Json.mapper().setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE);
		return config;
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {

		JpaTransactionManager tm = new JpaTransactionManager();
		tm.setEntityManagerFactory(emf);
		return tm;
	}

	@Bean(name="entityManagerFactory")
	@DependsOn("flyway")
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean( //
			@Value("${local.db.databaseVendor}") String databaseVendor, //
			@Value("${local.db.generateDdl}") boolean generateDdl, //
			@Value("${local.db.showSql}") boolean showSql, //
			DataSource dataSource) {

		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setDatabase(Database.valueOf(databaseVendor));
		adapter.setGenerateDdl(generateDdl);
		adapter.setShowSql(showSql);

		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setJpaVendorAdapter(adapter);
		emf.setDataSource(dataSource);
		emf.setPackagesToScan(new String[] {"ch.ctiv.addressbook"});
		emf.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		emf.setPersistenceUnitName("default");
		return emf;
	}

	@Bean(name = "datasource")
	public DataSource dataSource( //
			@Value("${local.db.url}") String url, //
			@Value("${local.db.username}") String username, //
			@Value("${local.db.password}") String password, //
			@Value("${local.db.driverClassName}") String driverClassName) {

		DriverManagerDataSource ds = null;
		if ((username == null || "".equals(username))
				&& (password == null || "".equals(password))) {
			ds = new DriverManagerDataSource(url);
		} else {
			ds = new DriverManagerDataSource(url, username, password);
		}
		ds.setDriverClassName(driverClassName);

		return ds;
	}

	@Bean
	public Flyway flyway( //
			@Value("${local.db.databaseVendor}") String databaseVendor, //
			@Value("${local.db.flyway}") String useFlyway , //
			DataSource dataSource) {

		Flyway flyway = new Flyway();
		flyway.setDataSource(dataSource);
		flyway.setLocations("db/migration/" + Database.valueOf(databaseVendor));
		flyway.setBaselineVersionAsString("1.0.0");

		if ("true".equals(useFlyway)) {
			flyway.setBaselineOnMigrate(true);
			int numberOfMigrations = flyway.migrate();
			logger.info("Flyway migration executed {} migration steps", numberOfMigrations);
		} else {
			logger.warn("Not using Flyway");
		}
		return flyway;
	}

}
