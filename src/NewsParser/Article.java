/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NewsParser;

/**
 *
 * @author Agree
 */
public class Article{
    String title;
    String author;
    String newspaper;
    String month;
    String day;
    String year;
    String text;
    int matchCt;    // the match count is the number of times a match for a given string
                    // shows up in th text of the article.
                    
    
    Article(){
        title = "";
        author = "";
        newspaper = "";
        month = "";
        day = "";
        year = "";
        text = "";
        matchCt = 0;
    }
    //Returns how many times a body of text uses a given word.
    int matchCount(String match){
        int matchCount = 0;
        for (int i = 0; i < text.length() - match.length(); i++){
            if(text.substring(i, i+match.length()).equals(match)){
                matchCount++;
            }
        }
        return matchCount;
    }
}