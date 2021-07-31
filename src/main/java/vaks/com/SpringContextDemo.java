
package vaks.com;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringContextDemo {
	public static void main(String args[]) {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-config.xml");
		//HelloBean bean = (HelloBean) applicationContext.getBean("helloBean");
		HelloBean bean = applicationContext.getBean(HelloBean.class);
		System.out.println(bean.getMesaj());
		applicationContext.close();	
	}
}
