package springbootapi.booksapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.AbstractEnvironment;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@EnableSwagger2
@SpringBootApplication
public class BooksApiApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		System.out.println("DHANESH "+ System.getProperty("env"));
		SpringApplication.run(BooksApiApplication.class, args);
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, System.getProperty("env"));
		super.onStartup(servletContext);
	}

}
