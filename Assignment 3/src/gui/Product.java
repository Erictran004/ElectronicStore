package gui;

//Base class for all products the store will sell
public class Product{
 private double price;
 private int stockQuantity;
 private int soldQuantity;
 
 public Product(double initPrice, int initQuantity){
   price = initPrice;
   stockQuantity = initQuantity;
 }
 public void RemoveStockQuantity(){stockQuantity--;}
 public void AddStockQuantity(){stockQuantity++;}
 public void RemoveSoldQuantity(){soldQuantity--;}
 public void AddSoldQuantity(){soldQuantity++;}

 public int getStockQuantity(){
   return stockQuantity;
 }
 public int getSoldQuantity(){
   return soldQuantity;
 }
 public double getPrice(){
   return price;
 }
 

}