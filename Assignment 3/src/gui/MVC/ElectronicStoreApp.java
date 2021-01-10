package gui.MVC;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class ElectronicStoreApp extends Application {
    ElectronicStore model;
    ElectronicStoreView view;

    public void start(Stage primaryStage) {
        model = ElectronicStore.createStore();
        view = new ElectronicStoreView(model);
        //Creates the initial scene
        primaryStage.setTitle("Electronic Store Application");
        primaryStage.setScene(new Scene(view, 800, 400));
        primaryStage.show();

        //When there is something in the cart buttons will be enabled
        if(view.getCartCounter()>0){
            UpdateButtons(1,2);
            UpdateButtons(1,3);
        }
        view.getResetStore().setOnMouseReleased(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                //Resets the store
                model = ElectronicStore.createStore();
                start(primaryStage);
            }
        });

        view.getStoreStock().setOnMouseReleased(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent e) {
                //If the list view has something selected it will enable the AddToCart Button
                UpdateButtons(ItemSelection(1), 1);
            }
        });

        view.getAddToCart().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                int index = ItemSelection(1);
                if(model.getStockQuant(index)==0){
                    //Disables button if there is no more stock quantity of the selected item
                    UpdateButtons(-1,1);
                }else{
                    //Get the item that will be removed from StoreStock and placed into the Cart
                    String itemtoRemove = view.getStoreStock().getSelectionModel().getSelectedItem();
                    if (model.UpdateQuantity(itemtoRemove,true)& index>=0) {
                        //Uses the method in the view if user selects an item and clicks the button to add
                        view.AddToCartAction(index);
                    }
                }
            }
        });
        view.getRemoveFromCart().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                int index = ItemSelection(2);
                if(view.getCurrentCartArray().size()==0){
                    //Disables buttons to prevent completing the purchase
                    UpdateButtons(-1,3);
                }else{
                    //Removes the item from the cart
                    String itemRemove = view.getCurrentCart().getSelectionModel().getSelectedItem();
                    if (model.UpdateQuantity(itemRemove,false)& index>=0) {
                        view.RemoveItemAction(index);
                    }
                }

            }
        });
        view.getCompleteSale().setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                //Calls the method to process the sale
                view.CompleteSaleAction();
                //disable buttons
                UpdateButtons(-1,2);
                UpdateButtons(-1,3);
            }
        });
        //Shows the initial store before anything occurs
        view.initialStore();
    }
    public void UpdateButtons(int index,int ButtonType){
        /*
        ButtonType=1 --> Add To Cart
        ButtonType=2 --> Remove From Cart
        ButtonType=3 --> Complete Sale
        */
        if(index == -1 & ButtonType==1){
            view.getAddToCart().setDisable(true);
        }else{
            view.getAddToCart().setDisable(false);
        }
        if(index == -1 & ButtonType==2){
            view.getRemoveFromCart().setDisable(true);
        }else{
            view.getRemoveFromCart().setDisable(false);
        }
        if(index == -1 & ButtonType==3){
            view.getCompleteSale().setDisable(true);
        }else{
            view.getCompleteSale().setDisable(false);
        }
    }
    public int ItemSelection(int type) {
        int selection;
        if (type == 1) {
            //Gets what was selected from the list
            selection = view.getStoreStock().getSelectionModel().getSelectedIndex();
            return selection;
        }
        if (type == 2) {
            //Gets what was selected from the list
            selection = view.getCurrentCart().getSelectionModel().getSelectedIndex();
            return selection;
        }
        //If nothing is selected
        return -1;
    }
    public static void main (String[]args){
            launch(args);
        }
    }
