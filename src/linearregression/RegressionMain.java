package linearregression;
import java.util.Scanner;

/**
 * Machine Learning -- A Linear Regression Model
 * @author  Gareth
 * @date    5 April 2017
 * 
 * A machine learning program to generate a linear regression model from a 
 * .csv or text file. Originally made for comparing the amount of effort 
 * versus number of fish harvested for subsistence and commercial 
 * fishing methods.
 * 
 * Input:   A .csv file formated such that
 *              -Column 1: date or instance number (ignored)
 *              -Column 2: independent variable (x-axis value)
 *              -Column 3: dependent variable (y-axis value)
 *          Or a text file formated such that each line consists of a date,
 *              an independent variable value, and a dependent variable value.
 *              Each item in a line is separated by a comma. Each line is 
 *              separated by \r\n or \n.
 *          The file may have column headers.
 * Output:  The slope, intercept, and R^2 values of the best fit line
 *              for the graph of data where the independent variable is on
 *              the x-axis and the dependent variable is on the y-axis.
 *          Prints five predicted dependent variable values from randomly 
 *              selected independent variable values (randomly selected
 *              values lie within the range of values given to the program). 
 */


public class RegressionMain {

    public static void main(String[] args) {
        LinearRegression reg = new LinearRegression();
        
        Scanner keys = new Scanner(System.in);
        System.out.print("Enter the .csv or text file path: ");
        String file = keys.nextLine();

        System.out.println("Retrieving data from file...");
        reg.extractValues(file);
        reg.getXvals();
        reg.getYvals();
        System.out.println("Retrieved");
        System.out.println("Calculating model...\n");
        reg.calcSlope();
        reg.calcIntercept();
        reg.calcCoefficient();
        System.out.println("Calculated\n");
        
        System.out.println("--- Linear Regression Model ---");
        System.out.println("Slope: \t\t\t\t"+ reg.slope);
        System.out.println("Intercept: \t\t\t"+ reg.intercept);
        System.out.println("Line Function:\t\t\tf(x) = " + reg.slope + "x" 
                + " + " + reg.intercept );    
        System.out.println("Coefficient of Determination: \tR^2  = "+ reg.rSqrd);
        System.out.println("Range of model: \t\t"+ reg.printRange());
        System.out.println("-------------------------------\n");
        
        System.out.println("Enter a new independent variable value to have the "
                    + "model predict a dependent value.");
        System.out.println("Or, have 5 independent values "
                    + "generated and modeled for you.");
        
        boolean keepGoing = true;
        while(keepGoing) {
            System.out.print("Type a number or 'random'. ['exit' to quit] ");
            String answer = keys.nextLine();
            if (answer.matches("\\d*.?\\d*")) { // Number can be double or int
                try {
                    // User has entered a number that is a new indep. var (x). 
                    // Use it to predict the dependent var (y) value.
                    double numAnswer = Double.parseDouble(answer);
                    if (reg.isOutOfRange(numAnswer)) {
                        // User-entered value is out of model range
                        System.out.println("The value you entered is outside of "
                                + "the range of values used to generate the model");
                    }
                    else { // User-entered value is in model range
                        System.out.println("\n------ Prediction ------");
                        System.out.println("Independent Variable Value:\t\t" + numAnswer);
                        System.out.println("Predicted Dependent Variable Value:\t"
                                + reg.predict(numAnswer));
                        System.out.println("\n------------------------");
                    }
                }
                catch (NumberFormatException e) { // Cannot be parsed to double
                    System.err.println("Number in wrong format: " + e.getMessage());
                }
            }
            else if (answer.equalsIgnoreCase("random")) {
                // Predict 5 dependent var (y) values from 5 random 
                //   independent var (x) values.
                System.out.println("\n--- Random Predictions ---");
                for (int i = 0; i < 5; i++) {
                    double predictedDependent = reg.predictRand();
                    System.out.print(i + ")  ");
                    System.out.println("Random Independent variable value:\t" 
                            + reg.randIndependent);
                    System.out.println("    Predicted Dependent variable value:\t" 
                            + predictedDependent);
                }
                System.out.println("-----------------------\n");
            }
            else if (answer.equalsIgnoreCase("exit")||answer.equalsIgnoreCase("quit")) {
                keepGoing = false;
            }
            else System.out.println("Input not recognized. Please try again."
                    + "\n.\n.");
        }
        
        System.out.println("Goodbye");
        System.exit(0);
        
    }
    
}
