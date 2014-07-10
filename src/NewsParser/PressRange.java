/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package NewsParser;

import NewsParser.Article;
import NewsParser.PressYear;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 *
 * @author Agree
 */
public class PressRange {
    ArrayList<PressYear> years;
    String[] keywords;
    PressRange(String[] keywords){
        years = new ArrayList();
        this.keywords = keywords;
    }
    //adds an article...
    void add(Article a){
        //the value that SHOULD be the index value of the year...
        int yearIndex = yearInRange(Integer.parseInt(a.year));
        //if the article's not in the year range...
        if(yearIndex == -1){
            PressYear PY = new PressYear(Integer.parseInt(a.year));
            PY.add(a);
            addYear(PY);
        }
        else{//if it's in the index...
            years.get(yearIndex).add(a);//Add the article to the corresponding
            //press year.
        }
    }
    
    void addYear(PressYear PY){
        years.add(PY);
        Collections.sort(years);
    }
    //Returns the index value of the year that this article requested...
    //Returns -1 if it's not in the array.
    int yearInRange(int year){
        for (int i = 0; i < years.size(); i++){
            if(year == years.get(i).year){
                return i;
            }
        }
        return -1;
    }
    int lowestYear(){
        return years.get(0).year;
    }
    
    int highestYear(){
        return years.get(years.size()-1).year;
    }
    
    int getTotalArticles(){
        int GrandTotalArticles = 0;
        for (int i = 0; i < years.size(); i++){
            GrandTotalArticles += years.get(i).totalArticles;
        }
        return GrandTotalArticles;
    }
    
    int allArticlesMatching(String word){
        int allArticlesMatching = 0;
        for (int i = 0; i < years.size(); i++){
            allArticlesMatching += years.get(i).articlesUsingWord(word);
        }
        return allArticlesMatching;
    }
    
    void printAll() throws WriteException{
        // set up a workbook file
        String directoryAddress = "C:\\Users\\lenovo\\Documents\\GEORGETOWN\\Receipts\\";
        Date d = new Date();
        String date = d.toString().replace(':', '.');
        String fileName = directoryAddress + date + ".xls";
        try {
            File file = new File(fileName);
            Path fileDest = Paths.get(fileName);
            if(!file.exists()){//determine the file and if it doesn't exist, make it.
                Files.createFile(fileDest);
            }    
        WritableWorkbook workbook = Workbook.createWorkbook(file);
        System.out.println(getTotalArticles() + " Articles detected, spanning " 
                + (highestYear() - lowestYear()) + " years: " + lowestYear() + " - " + highestYear());
        // For each year in the press range... working backwards...
        // For each keyword in the range
        for(String keyword : keywords){
            // create a spreadsheet: months for that range on each A cell
            // insert the number of articles matching that keyword each month of each year
            workbook.createSheet(keyword, 0);
            WritableSheet currentSheet = workbook.getSheet(0);
            for (int i = 0; i < years.size(); i++){
                for(int j = 1; j < 13; j++){ // write a cell for each month of each year in the range of articles
                    WritableCell dateCell = currentSheet.getWritableCell(0, j+12*(i));
                    Label l = new Label(0, j+12*(i), 
                               Integer.toString(j) + "-" + Integer.toString(years.get(i).year));
                    dateCell = (WritableCell) l;
                    currentSheet.addCell(dateCell);
                    // write in the adjacent column the number of articles containing that word for each month
                    WritableCell matchCell = currentSheet.getWritableCell(1, j+12*(i));
                    Number matches = 
                            new Number(1, j+12*(i), years.get(i).articlesUsingWord(keyword, j));
                    matchCell = (WritableCell) matches;
                    currentSheet.addCell(matchCell);
                }    
            }
        }
        workbook.write();
        workbook.close();

        } catch (IOException ex) {
            Logger.getLogger(PressRange.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
