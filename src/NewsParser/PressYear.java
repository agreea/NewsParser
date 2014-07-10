/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NewsParser;

import NewsParser.Article;
import java.util.ArrayList;

/**
 *
 * @author Agree
 */
public class PressYear implements Comparable<PressYear>{
    int year;
    int totalArticles;
    
    //This represents the whole year:
    //An array where each month has 1 array (upon construction) which will
    //hold articles.
    ArrayList<ArrayList<Article>> months = new ArrayList();
//Initializes the totalArticles; creates adds 12 arrays of articles to the months
//array.
    PressYear(int yearGiven){
        //Creates the 12 arrays of articles that will go into the months array...
        //1 array for each month.
        year = yearGiven;
        for (int i = 0; i < 12; i++){
            ArrayList<Article> articles = new ArrayList();
            months.add(articles);
        }
        totalArticles = 0;
    }
    //Finds which month the article should go into,
    //sends it to the corresponding index in the months array.
    void add(Article a){
        totalArticles++;
        months.get(whatMonth(a.month)).add(a);
    }
    
    //Returns whatMonth to send the thing
    int whatMonth(String month){
        int whatMonth = 0;
        if (month.equals("January"))
           return 0;
        if (month.equals("February"))
           return 1;
        if (month.equals("March"))
           return 2;
        if (month.equals("April"))
           return 3;
        if (month.equals("May"))
           return 4;
        if (month.equals("June"))
           return 5;
        if (month.equals("July"))
           return 6;
        if (month.equals("August"))
           return 7;
        if (month.equals("September"))
           return 8;
        if (month.equals("October"))
           return 9;
        if (month.equals("November"))
           return 10;
        if (month.equals("December"))
           return 11;
        return whatMonth;
    }
    double yearAverage(){
        double yrAv = 0;
        for(int i = 0; i < months.size(); i++){
            yrAv += months.get(i).size();
        }
        return yrAv/months.size();
    }
    //Returns the average use of a word over a the span of the year
    double yearAverageWord(String word){
        double yrAv = 0;
        //For every month...
        for(int i = 0; i < months.size(); i++){
            //for every article in that month...
            for(int j = 0; j < months.get(i).size(); j++){
                yrAv += months.get(i).get(j).matchCount(word);
            }
        }
        return yrAv;
    }
    
    int articlesInMonth(int i){
        return months.get(i).size();
    }
    int articlesUsingWord(String word, int month){
        //For all the articles in a given month...
        int articlesUsingWord =0;
        ArrayList<Article> articles = months.get(month-1);
        for(Article article : articles){
            if(article.matchCount(word) > 0){
                articlesUsingWord++;
            }
        }
        return articlesUsingWord;
    }
    
    int articlesUsingWord(String word){
        int articlesUsingWord =0;
        //For all months:
        for(int j = 0; j < months.size(); j++){
            //for all articles in that month...
            for(int i = 0; i < months.get(j).size(); i++){
                if(months.get(j).get(i).matchCount(word) > 0){
                    articlesUsingWord++;
                }
            }
        }    
        return articlesUsingWord;
        
    }
    void printAll(){
        for (int j = 0; j < 12; j++){
            /*
            System.out.println("Month: " + (j+1) + " Year: " + year); 
            */
            System.out.println(articlesInMonth(j) + " " + articlesUsingWord("genocid", j));
            //System.out.println(articlesInMonth(j));
            //System.out.println(articlesUsingWord("genocid", j));
            }
    }
    
    void printMonths(){
        for (int i = 0; i < 12; i++){
            if(i == 0)
               System.out.println("January" + " " + year);
            else if(i == 1)
               System.out.println("February" + " " + year);
            else if(i == 2)
               System.out.println("March" + " " + year);
            else if(i == 3)
               System.out.println("April" + " " + year);
            else if(i == 4)
               System.out.println("May" + " " + year);
            else if(i == 5)
               System.out.println("June" + " " + year);
            else if(i == 6)
               System.out.println("July" + " " + year);
            else if(i == 7)
               System.out.println("August" + " " + year);
            else if(i == 8)
               System.out.println("September" + " " + year);
            else if(i == 9)
               System.out.println("October" + " " + year);
            else if(i == 10)
               System.out.println("November" + " " + year);
            else if(i == 11)
               System.out.println("December" + " " + year);
        }
    }

    @Override
    public int compareTo(PressYear o) {
        if(this.year > o.year){
            return 1;
        }
        else if(this.year < o.year){
            return -1;
        }
        else{
            return 0;
        }
    }
}
