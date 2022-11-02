package books;

import javafx.scene.control.CheckBox;

public class BookItem{
 
 private String bookTitle;
 private double bookPrice;
 
 public CheckBox select; //checkbox for selecting books as customer

 public BookItem(String bookTitle, double bookPrice){
  
  select = new CheckBox();
  this.bookTitle = bookTitle;
  this.bookPrice = bookPrice;
  
 }

 //title
 public String getTitle(){
  return this.bookTitle;
 }
 
 public void setTitle(String bookTitle){
  this.bookTitle = bookTitle;
 }

 //price
 public double getPrice(){
  return this.bookPrice;
 }

 public void setBookPrice(double bookPrice) {
  this.bookPrice = bookPrice;
 }

 //checkbox
 public CheckBox getSelect(){
  return select;
 }

 //unselects checkbox after leaving customer home screen
 public void setSelect(CheckBox select){
  this.select = select;
 }

}


