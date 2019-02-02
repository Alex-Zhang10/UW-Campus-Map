package campuspaths;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Campus Map API's Spring application main class
 */
@SpringBootApplication
@ComponentScan(basePackages = {"hw8"})
public class CampusPathsApplication {

	/**
	 * Campus Map API's Spring application main class
	 * @param args arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(CampusPathsApplication.class, args);
	}
}
