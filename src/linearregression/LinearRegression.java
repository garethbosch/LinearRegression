package linearregression;
import java.io.*;
import java.util.*;

/** 
 * Univariate Linear Regression Model
 * @author  Gareth Bosch
 * @date    17 March 2017
 * 
 */

public class LinearRegression {

    public ArrayList independent;
    public ArrayList dependent;
    public String[] valuesFromFile;
    public double[] indValRange;
    public double slope;
    public double intercept;
    public double rSqrd;
    public double sumXY = 0;
    public double sumXsquared = 0;
    public Random rand = new Random();
    public double randIndependent;
    public double predictedDependent;
    
    // Input:   String - path to file that has the values to be analyzed.
    // Output:  An array of Strings, each element being
    //              an individual value from the file.
    public void extractValues(String path) {
        Scanner fromFile = null;
        try {
            fromFile = new Scanner(new File(path));
        }
        catch(FileNotFoundException e) {
            System.out.println("Couldn't open file " + path);
            System.exit(0);
        }
        
        if (fromFile.hasNext("\\D+"))                           // If a non-digit char is seen in the first line, 
            fromFile.nextLine();                                //    skip over this line -- it's the collumn titles.
        String delim = Character.toString((char)3);
        fromFile.useDelimiter(delim);                           // Scanner delimiter is end of text char.
        String fileContent = fromFile.next();                   // Capture every value in the file in a string.
        
        valuesFromFile = fileContent.split(",|(\\r\\n)|\\n");   // Put each value in an array of Strings.
        
        fromFile.close();
    }
    
    // Inserts all independent variable (x-axis) values into 
    //      ArrayList independent.
    public void getXvals() {
        independent = new ArrayList();
        
        for(int i = 1; i < valuesFromFile.length; i=i+3){       // increment i+3 for three columns in data file
            independent.add(Double.parseDouble(valuesFromFile[i]));   
        }
        // Independent variable values sorted in ascending order so
        //   LinearRegression.predict() knows the range from which 
        //   to randomly select values.
        independent.sort(null);
    }
    
    // Insters all dependent variable (y-axis) values into
    //      ArrayList dependent.
    public void getYvals() {
        dependent = new ArrayList();
        
        for(int i = 2; i < valuesFromFile.length; i=i+3){       // increment i+3 for three columns in data file
            dependent.add(Double.parseDouble(valuesFromFile[i]));       
        }
    }
    
    // Calculates the slope of the linear regression line.
    // Sets slope variable to this value.
    // slope = sum(xy) / sum(x^2)
    public void calcSlope() {                                       
        double product;
        double square;
        double x;
        double y;
        
        for (int i = 0; i < independent.size(); i++) {          // sum(xy)
            x = (double)independent.get(i);
            y = (double)dependent.get(i);
            
            product = x * y;
            sumXY = sumXY + product;
        }
        
        for(int i = 0; i < independent.size(); i++) {           // sum(x^2)  
            x = (double)independent.get(i);
            
            square = Math.pow(x, 2);
            sumXsquared = sumXsquared + square;
        }
                
        slope = sumXY/sumXsquared;
    }
    
    // Calculates the intercept of the linear regression line
    // Selts intercept variable to this value.
    // intercept = mean(y) - slope * mean(x)
    public void calcIntercept() {
        double xsum = 0;
        double ysum = 0;
        
        for (Object x : independent) {
            xsum = xsum + (double)x;
        }
        double xmean = xsum/independent.size();                 // mean of x
        
        for (Object y : dependent) {
            ysum = ysum + (double)y;
        }
        double ymean = ysum/dependent.size();                   // mean of y
        
        intercept = ymean - (slope * xmean);
        
    }
    
    // Calculate the coefficient of determination of the linear regression line
    // Sets rSqrd variable to this value.
    // R^2 = [sum(xy)/sqrt(sum(x^2)sum(y^2))]^2
    public void calcCoefficient(){    
        double denominator = 0;
        double sumYsquared = 0;
        
        for (Object y : dependent) {                            // sum(y^2)
            double temp = Math.pow(((double)y), 2);
            sumYsquared = sumYsquared + temp;
        }
        
        denominator = Math.sqrt(sumXsquared * sumYsquared);     // sqrt(sum(x^2)sum(y^2))
        
        rSqrd = Math.pow((sumXY/denominator), 2);               // R^2
    }
    
    // Finds the range of independent variable values.
    // Output:      An array of doubles, the first element is range 
    //                  minimum, the second is range maximum.
    // Assumptions: Values have already been read from the source file
    //                  and inserted into ArrayList independent.
    //              ArrayList independent is sorted (done in Main).
    public double[] indValRange() {
        double[] range = new double[2];
        double min = (double)independent.get(0);
        double max = (double)independent.get(independent.size()-1);
        range[0] = min;
        range[1] = max;
        
        return range;
    }
    
    // Input:       A double that is a  new independent var value interactively 
    //                  entered by the user to be tested on the model.
    // Returns true if the double is out of range of the values used to generate
    //      the model.
    public boolean isOutOfRange(double enteredVal) {
        return (enteredVal < indValRange()[0] || enteredVal > indValRange()[1]);
    }
    
    // Returns the range of the model in String form
    public String printRange() {
        String min = Double.toString(indValRange()[0]);
        String max = Double.toString(indValRange()[1]);
        return min + " - " + max;
    }
    
    // Uses the model to predict a dependent variable (y) value from a new
    //    independent variable (x) value.
    // Input:       A double that is the new independent value.
    // Output:      A double that is the corresponding dependent value.
    // Assumptions: A model has already been generated.
    public double predict(double indVal) {
        return intercept + slope * indVal;
    }
    
    // Randomly generates a value for the independent variable.
    // Uses this value to predict the corresponding dependent variable value
    //    according to the linear regression model.
    public double predictRand() {
        double min = indValRange()[0];
        double max = indValRange()[1];
        
        randIndependent = rand.nextDouble();
        // Normalizd the random value to within the range of indep. var values
        randIndependent = randIndependent * (max - min) + min;
        
        // equation of a line y = b + mx
        return intercept + slope * randIndependent;
    }
 
    

}
