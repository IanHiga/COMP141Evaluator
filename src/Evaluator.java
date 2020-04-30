import java.util.HashMap;
import java.util.Stack;

public class Evaluator {
	private Tree ast;
	private Stack<Token> tokenStack;
	private HashMap<String, Integer> memory;
	
	public String evaluateTree(Tree parseTree) {
		ast = parseTree;
		tokenStack = new Stack<Token>();
		memory = new HashMap<String, Integer>();
		
		try {
			evalLeaf(ast);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String temp = memory.entrySet().toString();
		
		return ("\n-----End-----\n\n" + temp);
	}
	
	private void evalLeaf(Tree curRoot) throws Exception {
		
		//Add next token to stack
		tokenStack.add(curRoot.getValue());
		
		//Check leaves of root
		if(curRoot.getLeft() != null) {	
			evalLeaf(curRoot.getLeft());
		}
		if(curRoot.getRight() != null) {
			evalLeaf(curRoot.getRight());
		}
		
		try {
			evalStack();
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Evaluation Error");
		}
	}
	
	private void evalStack() throws Exception {
		/*
		// Debug
		System.out.println(tokenStack.size() + " tokens in the stack!\nEvaluator Stack:\n");
		for(int i = 0; i < tokenStack.size(); i++) {
			System.out.println(tokenStack.get(tokenStack.size() - (i + 1)).toString());
		}
		*/
		
		if(tokenStack.size() == 1) {
			return;
		}
		//Check for NUMBER, _____
		if(tokenStack.get(tokenStack.size() - 1).getType().contentEquals("NUMBER")) {
			
			//Confirm Stack starts with NUMBER
			
			//Check for NUMBER, IDENTIFIER (ASSIGNMENT)
			if(tokenStack.get(tokenStack.size() - 2).getType().contentEquals("IDENTIFIER")) {
				//NUMBER, IDENTIFIER, _____
				
				if(tokenStack.get(tokenStack.size() - 3).getValue().contentEquals(":=")) {
					//NUMBER, IDENTIFIER, :=, _____
					Token val = tokenStack.get(tokenStack.size() - 1);
					Token id = tokenStack.get(tokenStack.size() - 2);
					
					//Save to memory
					memory.put(id.getValue(),Integer.parseInt(val.getValue()));
					System.out.println("Added the following to memory!\n" + id.getValue() + " = " + memory.get(id.getValue()));
					
					tokenStack.pop();
					tokenStack.pop();
					tokenStack.pop();
				}
				else {
					String op = tokenStack.get(tokenStack.size() - 3).getValue();
					int val1, val2, result;
					Token id1;
					switch (op) {
						case "+":
							val2 = Integer.parseInt(tokenStack.pop().getValue());
							id1 = tokenStack.pop();
							val1 = memory.get(id1.getValue());
							tokenStack.pop();
							
							result = val1 + val2;
							tokenStack.add(new Token("NUMBER", Integer.toString(result)));
							
							break;
						case "-":
							val2 = Integer.parseInt(tokenStack.pop().getValue());
							id1 = tokenStack.pop();
							val1 = memory.get(id1.getValue());
							tokenStack.pop();
							
							result = val1 - val2;
							if(result < 0) {
								throw new Exception("Negative Number!");
							}
							tokenStack.add(new Token("NUMBER", Integer.toString(result)));
							
							break;
						case "*":
							val2 = Integer.parseInt(tokenStack.pop().getValue());
							id1 = tokenStack.pop();
							val1 = memory.get(id1.getValue());
							tokenStack.pop();
							
							result = val1 * val2;
							tokenStack.add(new Token("NUMBER", Integer.toString(result)));
							break;
						case "/":
							val2 = Integer.parseInt(tokenStack.pop().getValue());
							id1 = tokenStack.pop();
							val1 = memory.get(id1.getValue());
							tokenStack.pop();
							if(val2 == 0) {
								throw new Exception("Divide by 0 Error!");
							}
							
							result = val1 / val2;
							tokenStack.add(new Token("NUMBER", Integer.toString(result)));
							break;
						default:
							break;
					}
				}
				
			}
			//Check for NUMBER, NUMBER (EXPRESSION)
			else if(tokenStack.get(tokenStack.size() - 2).getType().contentEquals("NUMBER")) {
				
				//Found two NUMBERs in a row.
				
				// OPERATE
				String op = tokenStack.get(tokenStack.size() - 3).getValue();
				int val1, val2, result;
				switch (op) {
					case "+":
						val2 = Integer.parseInt(tokenStack.pop().getValue());
						val1 = Integer.parseInt(tokenStack.pop().getValue());
						tokenStack.pop();
						
						result = val1 + val2;
						tokenStack.add(new Token("NUMBER", Integer.toString(result)));
						
						break;
					case "-":
						val2 = Integer.parseInt(tokenStack.pop().getValue());
						val1 = Integer.parseInt(tokenStack.pop().getValue());
						tokenStack.pop();
						
						result = val1 - val2;
						if(result < 0) {
							throw new Exception("Negative Number!");
						}
						tokenStack.add(new Token("NUMBER", Integer.toString(result)));
						
						break;
					case "*":
						val2 = Integer.parseInt(tokenStack.pop().getValue());
						val1 = Integer.parseInt(tokenStack.pop().getValue());
						tokenStack.pop();
						
						result = val1 * val2;
						tokenStack.add(new Token("NUMBER", Integer.toString(result)));
						break;
					case "/":
						val2 = Integer.parseInt(tokenStack.pop().getValue());
						val1 = Integer.parseInt(tokenStack.pop().getValue());
						tokenStack.pop();
						if(val2 == 0) {
							throw new Exception("Divide by 0 Error!");
						}
						
						result = val1 / val2;
						tokenStack.add(new Token("NUMBER", Integer.toString(result)));
						break;
					default:
						break;
				}
			}
		}
		//Check for Identifier, _____
		else if(tokenStack.get(tokenStack.size() - 1).getType().contentEquals("IDENTIFIER")) {
			
			//Confirm Stack starts with IDENTIFIER
			
			//Check for IDENTIFIER, NUMBER (ASSIGNMENT STATEMENT)
			if(tokenStack.get(tokenStack.size() - 2).getType().contentEquals("NUMBER")) {
				
				//Found ID, NUM, Assignment
				
				// OPERATE
				String op = tokenStack.get(tokenStack.size() - 3).getValue();
				int val1, val2, result;
				Token id1;
				switch (op) {
					case "+":
						id1 = tokenStack.pop();
						val2 = memory.get(id1.getValue());
						val1 = Integer.parseInt(tokenStack.pop().getValue());
						tokenStack.pop();
						
						result = val1 + val2;
						tokenStack.add(new Token("NUMBER", Integer.toString(result)));
						
						break;
					case "-":
						id1 = tokenStack.pop();
						val2 = memory.get(id1.getValue());
						val1 = Integer.parseInt(tokenStack.pop().getValue());
						tokenStack.pop();
						
						result = val1 - val2;
						if(result < 0) {
							throw new Exception("Negative Number!");
						}
						tokenStack.add(new Token("NUMBER", Integer.toString(result)));
						
						break;
					case "*":
						id1 = tokenStack.pop();
						val2 = memory.get(id1.getValue());
						val1 = Integer.parseInt(tokenStack.pop().getValue());
						tokenStack.pop();
						
						result = val1 * val2;
						tokenStack.add(new Token("NUMBER", Integer.toString(result)));
						break;
					case "/":
						id1 = tokenStack.pop();
						val2 = memory.get(id1.getValue());
						val1 = Integer.parseInt(tokenStack.pop().getValue());
						tokenStack.pop();
						if(val2 == 0) {
							throw new Exception("Divide by 0 Error!");
						}
						
						result = val1 / val2;
						tokenStack.add(new Token("NUMBER", Integer.toString(result)));
						break;
					default:
						break;
				}
			}
			//Check for IDENTIFIER, IDENTIFIER (ASSIGNMENT STATEMENT)
			else if(tokenStack.get(tokenStack.size() - 2).getType().contentEquals("IDENTIFIER")) {
				
				//Found two IDENTIFIERSs in a row.
				
				// OPERATE
				String op = tokenStack.get(tokenStack.size() - 3).getValue();
				int val1, val2, result;
				Token id1, id2;
				switch (op) {
					case "+":
						id1 = tokenStack.pop();
						id2 = tokenStack.pop();
						val2 = memory.get(id1.getValue());
						val1 = memory.get(id2.getValue());
						tokenStack.pop();
						
						result = val1 + val2;
						tokenStack.add(new Token("NUMBER", Integer.toString(result)));
						
						break;
					case "-":
						id1 = tokenStack.pop();
						id2 = tokenStack.pop();
						val2 = memory.get(id1.getValue());
						val1 = memory.get(id2.getValue());
						tokenStack.pop();
						
						result = val1 - val2;
						if(result < 0) {
							throw new Exception("Negative Number!");
						}
						tokenStack.add(new Token("NUMBER", Integer.toString(result)));
						
						break;
					case "*":
						id1 = tokenStack.pop();
						id2 = tokenStack.pop();
						val2 = memory.get(id1.getValue());
						val1 = memory.get(id2.getValue());
						tokenStack.pop();
						
						result = val1 * val2;
						tokenStack.add(new Token("NUMBER", Integer.toString(result)));
						break;
					case "/":
						id1 = tokenStack.pop();
						id2 = tokenStack.pop();
						val2 = memory.get(id1.getValue());
						val1 = memory.get(id2.getValue());
						tokenStack.pop();
						if(val2 == 0) {
							throw new Exception("Divide by 0 Error!");
						}
						
						result = val1 / val2;
						tokenStack.add(new Token("NUMBER", Integer.toString(result)));
						break;
					default:
						break;
				}
			}
		}
	}
}
