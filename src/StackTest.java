public class StackTest {
    public static void main(String[] args){
        ArrayStack as = new ArrayStack(30);

        for(int i = 1; i <= 30; i++){
            as.push(i);
        }

        for(int i = 0; i < 30; i++){
            System.out.println("ArrayStack top element is: " + as.top);
            as.pop();
        }
    }


}
