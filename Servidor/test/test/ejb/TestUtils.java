package test.ejb;

import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;


public class TestUtils {

	public static InitialContext createContext(String serverUrl) throws NamingException {
		Properties p = new Properties();

		p.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
		serverUrl = "remote://" + serverUrl;
		p.put(javax.naming.Context.PROVIDER_URL, serverUrl);
		p.put("jboss.naming.client.ejb.context", true);


		return new InitialContext(p);
	}

}
