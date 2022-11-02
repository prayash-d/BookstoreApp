package books;

public class Customer extends User{
 
 protected String username;
 protected String password;
 private int points;
 private String status;
 
 public Customer(String username, String password){
  
  this.username = username;
  this.password = password;
  this.status = status;
  points = 0;

 }
 
 //username
 public String getUsername() {
  return username;
 }
 
 public void setUsername(String username) {
  this.username = username;
 }
 
 //password
 public String getPassword() {
  return password;
 }

 //points
 public int getPoints() {
  return points;
 }
 
 public void setPoints(int points) {
  this.points += points;
  setStatus(this.points);
 }

 //status
 public String getStatus() {
  return status;
 }
 
 private void setStatus(int points) {
  if(points > 1000){ status = "GOLD"; }
  
  else if(points >= 0 && points <= 1000) { status = "SILVER"; }
 }

}

