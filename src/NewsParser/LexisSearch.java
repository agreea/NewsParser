/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NewsParser;
import NewsParser.Article;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Vector;
import NewsParser.PressRange;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.write.WriteException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
/**
 *
 * @author Agree
 */


public class LexisSearch{
    /**
     * @param args the command line arguments
     */
    // the press range class stores data about the articles, organized by year.
    static Vector<Article> Articles = new Vector();
    
    public static void main(String[] args) {
        String filePath = "";
        String finalDest;
        String regionInput;
        Scanner scan = new Scanner(System.in);
        System.out.println("Please give me a file to analyze:");
        filePath = scan.nextLine();
        System.out.println("Please give me words or word roots to search for, separated by a comma (\"genocid, East\"): ");
        String keywords = scan.nextLine();
        String PDFText = getPDFText(filePath);
        String[] keywordArray = keywords.split(",");
        for(String keyword : keywordArray){
                keyword.trim();
        }
        PressRange PR = new PressRange(keywordArray); 
        createPressAnalytics(PDFText, keywordArray, PR);
    }

    // Takes all the news articles as a single string
    // searches for instances of string KEYWORD
    static void createPressAnalytics(String text, String[] keywords, PressRange PR){
        String [] rawArts = text.split("<NEWARTICLE>");
        System.out.println("Checking for keywords: " + keywords);
        System.out.println("Found " + Integer.toString(rawArts.length) + " articles");
        for (String articleText : rawArts){ // for each article
            Article currArticle = new Article();
            String month = "";
            for (int j = 0; j < articleText.length(); j++){
                //If the article title's empty 
                if(currArticle.newspaper.isEmpty()){//and the substring matches WaPo or NYT...
                   if(articleText.substring(j, j+"Washington Post".length()).
                           equals("Washington Post") || articleText.substring(j, j+"Washington Post".length()).
                           equals("WASHINGTON POST")){
                       currArticle.newspaper = articleText.substring(j, j+"Washington Post".length());
                       j += currArticle.newspaper.length();
                   }
                   else if (articleText.substring(j, j+"New York Times".length()).
                           equals("New York Times") || 
                           articleText.substring(j, j+"New York Times".length()).equals("NEW YORK TIMES")){
                       currArticle.newspaper = articleText.substring(j, j+"New York Times".length());
                       j += currArticle.newspaper.length();
                   }
                }
                //If the current article's title isn't empty but the month is...
                else if (currArticle.month.isEmpty() && 
                        !Character.isWhitespace(articleText.charAt(j)) && 
                        !Character.isDigit(articleText.charAt(j))){
                            month +=articleText.charAt(j);
                        if (isMonth(month)){
                            currArticle.month = month;
                            month = "";
                        }
                }
                //if the current day is empty...
                else if (currArticle.day.isEmpty()){
                    char a = articleText.charAt(j);
                    char b = articleText.charAt(j+1);
                    if (Character.isDigit(a)){
                        currArticle.day = Character.toString(a);
                        if (Character.isDigit(b)){//if b is also a digit...
                            currArticle.day +=b;//don't forget to add b.
                        }
                    }
                }
                else if(currArticle.year.isEmpty()){
                    //if the next four characters are digits...
                    if(Character.isDigit(articleText.charAt(j)) && 
                            Character.isDigit(articleText.charAt(j+1)) && 
                            Character.isDigit(articleText.charAt(j+2)) && 
                            Character.isDigit(articleText.charAt(j+3))){
                        //that must be the article's year!
                        currArticle.year = Character.toString(articleText.charAt(j)) + Character.toString(articleText.charAt(j+1)) 
                                + Character.toString(articleText.charAt(j+2)) + Character.toString(articleText.charAt(j+3));
                    }
                }
                else if(currArticle.text.isEmpty()){
                    currArticle.text = articleText.substring(j);
                    for (String keyword : keywords){
                        currArticle.matchCt = currArticle.matchCount(keyword);
                    }
                }
            }
            PR.add(currArticle);
        }
        try {
            PR.printAll();
        } catch (WriteException ex) {
            Logger.getLogger(LexisSearch.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    static void chronologicalLook(){
        int currentYear = 0;
        String currentMonth = "";
        for (int i = Articles.size() - 1; i > 0; i--){
            if (currentYear == 0){
                currentYear = Integer.parseInt(Articles.get(i).year);
                System.out.println(currentYear);
            }
            if (currentMonth.isEmpty()){
                currentMonth = Articles.get(i).month;
            }
        }
        
    }
    
    static boolean isMonth(String candidate){
       if(candidate.equals("January") || candidate.equals("February") || 
               candidate.equals("March") || candidate.equals("April") || 
               candidate.equals("May") || candidate.equals("June") || 
               candidate.equals("July") || candidate.equals("August") || 
               candidate.equals("September") || candidate.equals("October") || 
               candidate.equals("November") || candidate.equals("December")){
        return true;
       }
       return false;
    }

    static Pattern datePattern(String month, int year){
        String strYr = Integer.toString(year);
        Pattern date2Search = Pattern.compile("^" + month + " /d/d /," + strYr);
                /*87 of 212 DOCUMENTS                  
                 * The New York Times 
                 * January 8, 1984, Sunday, Late City Final Edition
                 */
        return date2Search;
    }
    
    // Returns a string containing all the news articles from a single PDF file.
    public static String getPDFText(String FileName) {
        PDDocument pd;
        BufferedWriter wr;
        try {
            File input = new File(FileName);  // The PDF file from where you would like to extract
            pd = PDDocument.load(input);
            System.out.println("Loaded pdf with " + pd.getNumberOfPages() + " pages.");
            //pd.save("CopyOfInvoice.pdf"); // Creates a copy called "CopyOfInvoice.pdf"
            PDFTextStripper stripper = new PDFTextStripper();
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            wr = new BufferedWriter(new OutputStreamWriter(byteStream));
            stripper.writeText(pd, wr);
            if (pd != null) {
                pd.close();
            }
            wr.close();
            return byteStream.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
