package ro.codecamp.modularity;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;

public abstract class ServiceBaseTest {

	@Deployment
	public static Archive<?> deploy() {
		return Deployments.domain();
	}

}
