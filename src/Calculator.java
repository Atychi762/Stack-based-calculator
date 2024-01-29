import javax.swing.*;
import java.util.ArrayList;

public class Calculator {
    // Member fields
    private String expression;
    private ArrayStack stack = new ArrayStack(25);
    ArrayList<Integer> acceptedChars = new ArrayList<Integer>();
    private boolean validMessage = false;
    private String postfixExpression;


    public Calculator(){
        acceptedChars.add(43); acceptedChars.add(45); acceptedChars.add(42); acceptedChars.add(47); acceptedChars.add(94); acceptedChars.add(40); acceptedChars.add(41);
        acceptedChars.add(48); acceptedChars.add(49); acceptedChars.add(50); acceptedChars.add(51); acceptedChars.add(52); acceptedChars.add(53); acceptedChars.add(54); acceptedChars.add(55); acceptedChars.add(56); acceptedChars.add(57);

        while(!validMessage){
            expression =  JOptionPane.showInputDialog(null, "Please enter an expression(between 3 and 20 characters accepted characters: 0-9 and +, -, *, /, ^, (, ))", "Mathematical expression calculator", JOptionPane.INFORMATION_MESSAGE);
            validMessage = validateMessage(expression);

            if(validMessage){
                postfixExpression = convertExpression(expression);
                System.out.println(postfixExpression);

            }

        }
    }

    private boolean validateMessage(String message){
        boolean validated= true;

        for(int i = 0; i < message.length(); i++){
            if(message.length() < 3 || message.length() > 20){
                JOptionPane.showMessageDialog(null, "ERROR: Message must be between 3 and 20 characters", "Invalid expression", JOptionPane.WARNING_MESSAGE);
                validated = false;
                break;
            }
            if(!acceptedChars.contains((int)message.charAt(i))){
                JOptionPane.showMessageDialog(null, "ERROR: Message can only contain the following characters: 0-9 and +, -, *, /, ^, (, )", "Invalid expression", JOptionPane.WARNING_MESSAGE);
                validated = false;
                break;
            }
            else{
                validated = true;
            }
        }
        return validated;
    }

    private int getOperatorPrecedence(char operator){
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
        String outputExpression = "";
        ArrayStack operatorStack = new ArrayStack(inputExpression.length());
        char currChar;

        for(int i = 0; i < inputExpression.length(); i++){
            currChar = inputExpression.charAt(i);

            if((int)currChar >= 48 && (int)currChar != 94){
                outputExpression += currChar;
            }
            else{
                if((int)currChar == 40){
                    operatorStack.push(currChar);
                }
                else if((int)currChar == 41){
                    while((char)operatorStack.top() != 40){
                        outputExpression += operatorStack.top();
                        operatorStack.pop();
                    }
                    operatorStack.pop();
                }
                else if(operatorStack.isEmpty() || getOperatorPrecedence(currChar) < getOperatorPrecedence((char)operatorStack.top())){
                    operatorStack.push(currChar);
                }
                else {
                    while (!operatorStack.isEmpty() && getOperatorPrecedence(currChar) > getOperatorPrecedence((char) operatorStack.top())) {
                        if((char)operatorStack.top() == 40 || (char)operatorStack.top() == 41){
                            break;
                        }
                        outputExpression += operatorStack.top();
                        operatorStack.pop();
                    }
                    operatorStack.push(currChar);
                }

            }
        }

        while(!operatorStack.isEmpty()){
            outputExpression += operatorStack.top();
            operatorStack.pop();
        }

        return outputExpression;
    }
}
