import javax.swing.*;
import java.util.ArrayList;

public class Calculator {
    // Member fields
    private String expression;
    ArrayList<Integer> acceptedChars = new ArrayList<Integer>();
    private boolean validMessage = false;
    private String postfixExpression;
    float evaluation;
    String output = "";


    public Calculator(){
        // adding the ASCII values of the accepted characters to the arraylist
        acceptedChars.add(43); acceptedChars.add(45); acceptedChars.add(42); acceptedChars.add(47); acceptedChars.add(94); acceptedChars.add(40); acceptedChars.add(41);
        acceptedChars.add(48); acceptedChars.add(49); acceptedChars.add(50); acceptedChars.add(51); acceptedChars.add(52); acceptedChars.add(53); acceptedChars.add(54); acceptedChars.add(55); acceptedChars.add(56); acceptedChars.add(57);

        // infinitely checking the validity of the input expression until a valid expression is entered
        while(!validMessage){
            // displaying the input box
            expression =  JOptionPane.showInputDialog(null, "Please enter an expression(between 3 and 20 characters accepted characters: 0-9 and +, -, *, /, ^, (, ))", "Mathematical expression calculator", JOptionPane.INFORMATION_MESSAGE);
            // calling the validateMessage method to check the validity of the message
            validMessage = validateMessage(expression);
        }

        // if the input expression is valid then call the convertExpression method
        postfixExpression = convertExpression(expression);
        // Evaluate the postfix expression
        evaluation = evaluateExpression(postfixExpression);

        // making the output of the program
        output = "The result of the expression is:\nInfix: "+ expression +"\nPostfix: "+ postfixExpression +"\nResult: " + evaluation;
        // Creating the output box for the program
        JOptionPane.showMessageDialog(null, output, "Invalid expression", JOptionPane.WARNING_MESSAGE);

    }

    private boolean validateMessage(String message){
        boolean validated= true;

        // looping through the expression
        for(int i = 0; i < message.length(); i++){
            // if the message is less than 3 characters or longer than 20 validated is false
            if(message.length() < 3 || message.length() > 20){
                JOptionPane.showMessageDialog(null, "ERROR: Message must be between 3 and 20 characters", "Invalid expression", JOptionPane.WARNING_MESSAGE);
                validated = false;
                break;
            }
            // if the arraylist of acceptedCharacters does not contain a character in the expression validated is false
            if(!acceptedChars.contains((int)message.charAt(i))){
                JOptionPane.showMessageDialog(null, "ERROR: Message can only contain the following characters: 0-9 and +, -, *, /, ^, (, )", "Invalid expression", JOptionPane.WARNING_MESSAGE);
                validated = false;
                break;
            }
            // otherwise validated is true

        }
        return validated;
    }

    private int getOperatorPrecedence(char operator){
        // method checks what operator was passed and returns an integer based on the precedence, the lower the number the higher its precedence
        int precedence = 0;

        if(operator == '^') {
            precedence = 1;
        }
        else if(operator == '*'){
            precedence = 2;
        }
        else if(operator == '/'){
            precedence = 3;
        }
        else if(operator == '+'){
            precedence = 4;
        }
        else if(operator == '-'){
            precedence = 5;
        }

        return precedence;
    }

    private String convertExpression(String inputExpression){
        // initializing an outputExpression, operatorStack, and currChar variable
        String outputExpression = "";
        ArrayStack operatorStack = new ArrayStack(inputExpression.length());
        char currChar;

        // Looping through the inputExpression to check each character
        for(int i = 0; i < inputExpression.length(); i++){
            // set the currChar variable to the current character
            currChar = inputExpression.charAt(i);

            // Checking if the current character is greater than the ascii value for 0 but not equal to the ascii for ^
            if((int)currChar >= 48 && (int)currChar != 94){
                // if the currChar is numeric add it to the output
                outputExpression += currChar;
            }
            else{
                // if the currChar is a ( than add it to the operator stack
                if((int)currChar == 40){
                    operatorStack.push(currChar);
                }
                // if the currChar is a ) than add the stack to the outputExpression until a ( is reached
                else if((int)currChar == 41){
                    while((char)operatorStack.top() != 40){
                        outputExpression += operatorStack.top();
                        operatorStack.pop();
                    }
                    // pop the ( from the stack
                    operatorStack.pop();
                }
                // else if the stack is empty or the precedence of the current operator is greater than that at the top of the stack then
                // push it to the operatorStack
                else if(operatorStack.isEmpty() || getOperatorPrecedence(currChar) < getOperatorPrecedence((char)operatorStack.top())){
                    operatorStack.push(currChar);
                }

                else {
                    // else when the operator precedence is less than that of the currentStack and the stack is not empty
                    while (!operatorStack.isEmpty() && getOperatorPrecedence(currChar) > getOperatorPrecedence((char) operatorStack.top())) {
                        // if the top of the stack is a parenthesis then break the loop
                        if((char)operatorStack.top() == 40 || (char)operatorStack.top() == 41){
                            break;
                        }
                        // otherwise add the top of the operatorStack to the outputExpression
                        outputExpression += operatorStack.top();
                        // pop the top of the operatorStack
                        operatorStack.pop();
                    }
                    // when the precedence is greater than the top of the stack or the stack is empty then push the currChar to stack
                    operatorStack.push(currChar);
                }

            }
        }

        // At the end if there is still operators in the stack we push them all to the outputExpression
        while(!operatorStack.isEmpty()){
            outputExpression += operatorStack.top();
            operatorStack.pop();
        }

        // return the postfix expression
        return outputExpression;
    }

    private float evaluateExpression(String exp){
        float answer = 0;
        ArrayStack stack = new ArrayStack(25);
        char currChar;

        // Looping through the postfix expression
        for(int i = 0; i < exp.length(); i++) {
            currChar = exp.charAt(i);

            // Checking if the current character is greater than the ascii value for 0 and not the ascii value for ^
            if ((int) currChar >= 48 && (int)currChar != 94) {
                // add it to the output
                stack.push((float)currChar-48);
            }
            else{
                float tempAns;
                // Setting the top 2 values in the stack to temp values and popping them from the stack
                float operand2 = (float)stack.top();
                stack.pop();
                float operand1 = (float)stack.top();
                stack.pop();

                // Evaluating the operator and calculating the expression, and pushing the answer to the stack
                if(currChar == '^') {
                    tempAns = (float) Math.pow(operand1, operand2);
                    stack.push(tempAns);
                }
                else if(currChar == '*'){
                    tempAns = operand1 * operand2;
                    stack.push(tempAns);
                }
                else if(currChar == '/'){
                    tempAns = operand1 / operand2;
                    stack.push(tempAns);
                }
                else if(currChar == '+'){
                    tempAns = operand1 + operand2;
                    stack.push(tempAns);
                }
                else if(currChar == '-'){
                    tempAns = operand1 - operand2;
                    stack.push(tempAns);
                }
            }
        }
        // setting the answer variable to the top of the stack at the end
        answer = (float)stack.top();

        // Return the answer
        return answer;
    }
}
