package vaks.com;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DefaultController {
	static final Logger logger = Logger.getLogger(DefaultController.class.getName());
	
	public DefaultController() {
		logger.log(Level.INFO,"DefaultController ctor");
	}

	@RequestMapping(value = "/**")
	public String defaultContent() {
		logger.log(Level.INFO,"defaultContent");
		return "index";
	}
}