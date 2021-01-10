package gui.MVC;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.geometry.Pos;
import java.util.ArrayList;
import java.util.Arrays;

public class ElectronicStoreView extends Pane {
    private Button ResetStore, AddToCart, RemoveFromCart, CompleteSale;
    private ListView<String> MostPopularItems, StoreStock, CurrentCart;
    private TextField SalesField, TotalRevenue,RevenuePerSale;
    private ArrayList<String> CurrentCartArray,CurrentStock, MostPopularArray;
    private Label Header3;
    private int TotalSales, CartCounter =0;
    private double CartCost,TotalProfit = 0.00;

    ElectronicStore model;
    //Constructor
    public ElectronicStoreView(ElectronicStore initialModel){
        model = initialModel;
        CurrentCartArray = new ArrayList<>();
        MostPopularArray = new ArrayList<>();
        //Gets the converted StockString
        CurrentStock = model.getStockString();
        Label Header1, Header2, Header4, SaleQuantityHeader,RevenueHeader,ProfitPerSale;
        String sale = "0";
        String rev = "0.00";
        String revPerSale = "N/A";
        //Creates all the necessary components of the menu
        ResetStore = new Button("Reset Store");
        ResetStore.relocate(30, 350);
        ResetStore.setPrefSize(120, 40);
        ResetStore.setAlignment(Pos.CENTER);

        AddToCart = new Button("Add to Cart");
        AddToCart.relocate(280, 350);
        AddToCart.setPrefSize(120, 40);
        AddToCart.setAlignment(Pos.CENTER);
        AddToCart.setDisable(true);

        RemoveFromCart = new Button("Remove from Cart");
        RemoveFromCart.relocate(530, 350);
        RemoveFromCart.setPrefSize(120, 40);
        RemoveFromCart.setAlignment(Pos.CENTER);
        RemoveFromCart.setDisable(true);

        CompleteSale = new Button("Complete Sale");
        CompleteSale.relocate(650, 350);
        CompleteSale.setPrefSize(120, 40);
        CompleteSale.setAlignment(Pos.CENTER);
        CompleteSale.setDisable(true);

        MostPopularItems = new ListView<String>();
        MostPopularItems.setPrefSize(150, 195);
        MostPopularItems.relocate(15, 145);

        StoreStock = new ListView<String>();
        StoreStock.setPrefSize(240, 310);
        StoreStock.relocate(222, 30);

        CurrentCart = new ListView<String>();
        CurrentCart.setPrefSize(240, 310);
        CurrentCart.relocate(530, 30);

        SalesField = new TextField(sale);
        SalesField.setPrefSize(100, 25);
        SalesField.relocate(65, 33);

        TotalRevenue = new TextField(rev);
        TotalRevenue.setPrefSize(100, 25);
        TotalRevenue.relocate(65, 63);

        RevenuePerSale = new TextField(revPerSale);
        RevenuePerSale.setPrefSize(100, 25);
        RevenuePerSale.relocate(65, 93);

        Header1 = new Label("Store Summary:");
        Header1.relocate(15, 15);
        Header2 = new Label("Store Stock:");
        Header2.relocate(222, 15);
        Header3 = new Label("Current Cart: ("+CartCost+")");
        Header3.relocate(530, 15);
        Header4 = new Label("Most Popular Items:");
        Header4.relocate(15, 130);

        SaleQuantityHeader = new Label("# Sales:");
        SaleQuantityHeader.relocate(23, 37);
        RevenueHeader = new Label("Revenue:");
        RevenueHeader.relocate(15, 67);
        ProfitPerSale = new Label("$/Sale:");
        ProfitPerSale.relocate(27, 97);

        //Adds those components to the plane
        getChildren().addAll(ResetStore, AddToCart, RemoveFromCart, CompleteSale, MostPopularItems, CurrentCart,
                StoreStock, Header1, Header2, Header3, Header4, SaleQuantityHeader, RevenueHeader, ProfitPerSale,SalesField,
                TotalRevenue, RevenuePerSale);
    }

    public void AddToCartAction ( int index){
        CartCounter++;
        //Adds the value according to index value from CurrentStock
        CurrentCartArray.add(CurrentStock.get(index));
        //Puts it into the other ListView
        ObservableList<String> Products = FXCollections.observableArrayList(CurrentCartArray);
        CurrentCart.setItems(Products);

        //Update Label
        CartCost+=model.stock[index].getPrice();
        Header3.setText("Current Cart: ($"+CartCost+")");
    }
    public void RemoveItemAction(int index){
        CartCounter--;
        int StockIndex;
        //ConvertedArrayList in ElectronicStore.indexOf(The currently selected item in CurrentCartArray)
        StockIndex = model.getStockString().indexOf(CurrentCartArray.get(index));
        //Removes the selected item from the Cart list and updates it
        CurrentCartArray.remove(index);
        ObservableList<String> Products = FXCollections.observableArrayList(CurrentCartArray);
        CurrentCart.setItems(Products);

        //Update Label and cost
        CartCost-=model.stock[StockIndex].getPrice();
        Header3.setText("Current Cart: ($"+CartCost+")");;
    }
    public void initialStore(){
        ObservableList<String> Products = FXCollections.observableArrayList(CurrentStock);
        StoreStock.setItems(Products);
        ObservableList<String> Popular = FXCollections.observableArrayList(CurrentStock.get(0),CurrentStock.get(1),CurrentStock.get(2));
        MostPopularItems.setItems(Popular);

    }

    public void CompleteSaleAction(){
        //Adds the cart cost to the total profit. Increase # of sales. Calculates Rev/Sale. Updates the text boxes
        TotalProfit+= CartCost;
        TotalSales++;
        SalesField.setText(String.valueOf(TotalSales));
        TotalRevenue.setText(String.valueOf(Math.round(TotalProfit*100.0)/100.0));
        RevenuePerSale.setText(String.valueOf(Math.round((TotalProfit/TotalSales)*100.0)/100.0));


        //Reset The Cart
        CartCost = 0;
        CurrentCartArray = new ArrayList<>();
        ObservableList<String> Products = FXCollections.observableArrayList(CurrentCartArray);
        CurrentCart.setItems(Products);
        //Reset Header
        Header3.setText("Current Cart: ($"+CartCost+")");
        MostPopularItems();
    }
    private void MostPopularItems() {
        //initially clears the array/list
        MostPopularItems.getItems().clear();
        MostPopularArray.clear();
        int[] QuantitySoldofStock = new int[model.getNullSpace()];
        //Makes an array of all sold Quantities
        for (int i=0; i<QuantitySoldofStock.length;i++){
            if(model.stock[i]!=null){
                QuantitySoldofStock[i]=model.stock[i].getSoldQuantity();
            }
        }
        System.out.println(Arrays.toString(QuantitySoldofStock));
        //find the largest 3 numbers
        int firstIndex=0,secondIndex = 1,thirdIndex=2;
        for(int i=1; i<model.getNullSpace();i++){
            if (QuantitySoldofStock[i]>QuantitySoldofStock[firstIndex]){
                //1st=i,2nd=1st,3rd = 2nd
                thirdIndex=secondIndex;
                secondIndex = firstIndex;
                firstIndex=i;
            }
            if (QuantitySoldofStock[firstIndex]>QuantitySoldofStock[i]&&QuantitySoldofStock[i]>QuantitySoldofStock[secondIndex]){
                //2nd = i, 3rd =2nd
                thirdIndex=secondIndex;
                secondIndex=i;
            }
        }
        //Adds the top 3 sold products to an array
        MostPopularArray.add(""+model.stock[firstIndex]);
        MostPopularArray.add(""+model.stock[secondIndex]);
        MostPopularArray.add(""+model.stock[thirdIndex]);
        //puts the array values into the list
        ObservableList<String> Popular = FXCollections.observableArrayList(MostPopularArray);
        MostPopularItems.setItems(Popular);
    }

    //Getters
    public Button getAddToCart() { return AddToCart;}
    public Button getRemoveFromCart() {return RemoveFromCart;}
    public Button getResetStore(){return ResetStore;}
    public Button getCompleteSale(){return CompleteSale;}

    public ListView<String> getStoreStock(){return StoreStock;}
    public ListView<String> getCurrentCart(){return CurrentCart;}

    public ArrayList<String> getCurrentCartArray() {return CurrentCartArray;}

    public int getCartCounter() {return CartCounter; }
}
