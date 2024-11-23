package carsharing.logic;

public class InputHandler {

    public static int handleIntInput(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Input is not an integer");
            return -1;
        }
    }
}
