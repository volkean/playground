
package vaks.com;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import vaks.com.dto.HelloBean;

public class SpringContextDemo {
	public static void main(String args[]) {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-config.xml");
		// HelloBean bean = (HelloBean) applicationContext.getBean("helloBean");
		HelloBean bean = applicationContext.getBean(HelloBean.class);
		System.out.println(bean.getMesaj());
		
		DefaultController defaultController = applicationContext.getBean(DefaultController.class);
		//String display = helloController.display();
		
		// server = new WebServer
		// springServlet = dispatcherServlet
		// server.addServlet(springServlet)
		// server.start();
		
		applicationContext.close();
	}
}
