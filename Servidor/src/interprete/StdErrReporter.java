package interprete;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StdErrReporter implements IErrorReporter {
    public void reportError(String error) {
    	throw new RuntimeException(error);
    	
       /* String pattern = "line\\s(\\d+):(\\d+)";

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Now create matcher object.
        Matcher m = r.matcher(error);
        if (m.find( )) {    	
        		 System.out.println("Error: " + m.group(0));    
        }else{
        	System.out.println(error);
        }
        System.out.println(error);*/
    }
}