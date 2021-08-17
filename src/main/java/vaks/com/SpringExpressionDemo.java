package vaks.com;

import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class SpringExpressionDemo {
	public static void main(String args[]) {

		SpelExpressionParser parser = new SpelExpressionParser();
		Expression expression = parser.parseExpression("'Hello'.concat('!')");
		System.out.println(expression.getValue(String.class));

		System.out.println(parser.parseExpression("10 * 10/2").getValue());

		StandardEvaluationContext context = new StandardEvaluationContext("MIZN0101");
		SpelExpression rawExp = parser.parseRaw("#MIZN0101");
		rawExp.setValue(context, "test");
		System.out.println(rawExp.getValue(String.class));
	}
}