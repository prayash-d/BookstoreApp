package books;

import java.io.IOException;
import java.util.ArrayList;

public class Manager{
 
    protected String username = "admin";
    protected String password = "admin";
 
    protected static ArrayList<BookItem> arraylist_booksList = new ArrayList<>(); //create BookItem specific arraylist
    private static ArrayList<Customer> arraylist_customersList = new ArrayList<>(); //create Customer specific arraylist
    
    private static final Files f = new Files(); //create a Files Object

    //import values from arraylist obtained from files in Files class, and store those values into temporary arrays, which further are appended to main arraylist --> booksList and customersList
    public void array_file_refresh() throws IOException {
     ArrayList<BookItem> tempBooks = f.bookReadf(); //obtain the book's information from the arraylist in Files class
     ArrayList<Customer> tempCustomers = f.readCustomerFile(); //obtain the customer's information from the arraylist in Files class

     arraylist_booksList.addAll(tempBooks); //append the book info to BookItem object specific arraylist
     arraylist_customersList.addAll(tempCustomers); //append the customer info to Customer object specific arraylist  
    }

    public String getUsername(){
        return username; //returns admin
    }
    public String getPassword(){
        return password; //returns admin
    }

    public void addCustomer(Customer c){
        arraylist_customersList.add(c);
    }

    public void deleteCustomer(Customer c){
        arraylist_customersList.remove(c);
    }

    public ArrayList<Customer> getCustomers(){
        return arraylist_customersList;
    }

}



