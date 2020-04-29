import java.util.Stack;

public class Evaluator {
	private Tree ast;
	private Stack<Token> tokenStack;
	
	public String evaluateTree(Tree parseTree) {
		ast = parseTree;
		tokenStack = new Stack<Token>();
		try {
			evalLeaf(ast);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ("\nOutput: " + tokenStack.get(0).getValue() + "\n\n\n");
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
		//Check for NUMBER, NUMBER, OPERATOR
		if(tokenStack.size() == 1) {
			return;
		}
				if(tokenStack.get(tokenStack.size() - 1).getType().contentEquals("NUMBER")) {
					
					//Confirm Stack starts with NUMBER
					
					//Check for NUMBER, NUMBER
					if(tokenStack.get(tokenStack.size() - 2).getType().contentEquals("NUMBER")) {
						
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
	}
}
