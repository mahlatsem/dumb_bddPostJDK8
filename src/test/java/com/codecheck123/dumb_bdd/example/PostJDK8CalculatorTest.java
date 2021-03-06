package com.codecheck123.dumb_bdd.example;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.codecheck123.dumbbdd.dsl.UserStory;
import com.codecheck123.dumbbdd.example.Calculator;


public class PostJDK8CalculatorTest {
	private int result = 0;
	private final UserStory userStory = new UserStory.WithTitle("Addition Subtraction Division & Multiplication")
			.asA("Math idiot")
			.iWant("to run math operations")
			.soThat("I can avoid silly mistakes")
			.create();
	
	@Before
	public void resetResult(){
		result = 0;
	}
	
	@Test
	public void simpleMath(){
		Calculator calculator = new Calculator();
		
		userStory.scenarioWithTitle("Use operators + - / *")
			.given("I want to add '50'", (args) -> {
				calculator.add(args.first().asInt());
			})
			.and("I want to minus '20'", (args) -> {
				calculator.minus(args.first().asInt());
			})
			.and("Multiply by '2' & divide by '4'", (args) -> {
				calculator.multiply(args.first().asInt());
				calculator.divide(args.second().asInt());
			})
			.and("I want to add '30'", (args) -> {
				calculator.add(args.first().asInt());
			})
			.when("I request Results", (args) -> {
				result = calculator.getResults();
			})
			.then("Results equals '45'", (args) -> {
				assertEquals(args.first().asInt(), result);
			});
	}
	
	@Test
	public void reuseCalculationsResults(){
		Calculator cal = new Calculator(); 
		
		userStory.scenarioWithTitle("Reuse calculation results in next calculation")
			.given("I want to add '25'", (args) -> {
				cal.add(args.first().asInt());
			})
			.and("I want to minus '5'", (args) -> {
				cal.minus(args.first().asInt());
			})
			.when("I get the results as '20'", (args) -> {
				result = cal.getResults();
				assertEquals(args.first().asInt(), result);
			})
			.then("If I add '10', I must get '30'", (args) -> {
				cal.add(args.first().asInt());
				result = cal.getResults();
				assertEquals(args.second().asInt(),result);
			});
	}
	
	@Test
	public void restartCalculation(){
		Calculator cal = new Calculator();
		
		userStory.scenarioWithTitle("Reset calculator to start from scratch")
			.given("I want to add '25'", (args) -> {
				cal.add(args.first().asInt());
			})
			.and("I want to minus '5'", (args) -> {
				cal.minus(args.first().asInt());
			})
			.when("I reset the calculator", (args) -> {
				result = cal.multiply(1);
				assertEquals(result,20);
				cal.reset();
			})
			.then("Result must be '0'", (args) ->{
				result = cal.getResults();
				assertEquals(args.first().asInt(),result);
			});
	}
	
	@Test(expected=ArithmeticException.class)
	public void dividingByZero(){
		Calculator cal = new Calculator();
		
		userStory.scenarioWithTitle("Disallow division by zero")
			.given("numerator '5' and denominator '0'", (args) -> {
				cal.add(args.first().asInt());
				cal.divide(args.second().asInt());
			});
	}
}
