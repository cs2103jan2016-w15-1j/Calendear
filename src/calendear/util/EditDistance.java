// @@author A0126513
package calendear.util;

public class EditDistance {
	
	/**
	 * 
	 * Compute the LevenshteinDistance between 2 CharSequence. 
	 * This distance measure how much 2 string are different from each other.
	 * 
	 * Informally, the Levenshtein distance between two words is the minimum number
	 * of single-character edits (i.e. insertions, deletions or substitutions) required
	 * to change one word into the other. Inserting 1 character to one string will have 
	 * the same effect on Levenshtein distance as deleting 1 character in another string.
	 * 
	 * For more information https://en.wikipedia.org/wiki/Levenshtein_distance.
	 * 
	 * @param string1
	 * @param string2
	 * @return the Levenshtein distance between string1 and string2
	 */
    public static int computeEditDistance(CharSequence string1, CharSequence string2) {      
        //dynamic programming memo table
    	int[][] distance = new int[string1.length() + 1][string2.length() + 1];        

    	//The subproblem is finding edit distance of string1.substring(0,i) and string2.substring(0,j)
    	//The subproblem state can be described by 2 argument i, j.

    	//initialize base row
        for (int i = 0; i <= string1.length(); i++) {                                 
            distance[i][0] = i;                     
        }
        //initialize base column
        for (int j = 1; j <= string2.length(); j++) {                            
            distance[0][j] = j;                     
        }
                                                                                 
        for (int i = 1; i <= string1.length(); i++) {                                 
            for (int j = 1; j <= string2.length(); j++) {                             
                int caseDeleteString1Endpoint = distance[i - 1][j] + 1;
                int caseDeleteString2Endpoint = distance[i][j - 1] + 1;
                //We compare the end point of 2 string and add 1 substitution if they are not the same
                int caseNoDeletion = distance[i - 1][j - 1] + 
                		((string1.charAt(i - 1) == string2.charAt(j - 1)) ? 0 : 1);
            	distance[i][j] = minimum(caseDeleteString1Endpoint, caseDeleteString2Endpoint, caseNoDeletion);
            }
        }
                
        return distance[string1.length()][string2.length()];                           
    }
    
    private static int minimum(int a, int b, int c) {                            
        return Math.min(Math.min(a, b), c);                                      
    }                                                                            
    
}
