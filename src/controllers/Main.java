package controllers;

import controllers.views.ApplicationController;
import controllers.views.result.DatabaseResultController;
import controllers.views.result.imageInfo.ImageInfoController;
import entity.ImageProperty;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.UsersAlert;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Kostya Nirchenko.
 *
 * @since 30.07.2016
 */
public class Main extends Application {

    private static Logger logger = Logger.getLogger(Main.class.getName());

    private Stage primaryStage;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Image database");
        this.primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("views/images/database.png")));
        launchApplication();
    }

    private void launchApplication() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        AnchorPane pane = loader.load(getClass().getResourceAsStream("views/application.fxml"));
        ApplicationController applicationController = loader.getController();
        applicationController.setMain(this);
        Scene scene = new Scene(pane);
        scene.getStylesheets().add(getClass().getResource("views/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void showImageInfo(ImageProperty imageProperty) {
        try {
            FXMLLoader loader = new FXMLLoader();
            AnchorPane pane = loader.load(getClass().getResourceAsStream("views/result/imageInfo/imageInfo.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Изображение");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryStage);
            stage.setScene(new Scene(pane));
            ImageInfoController imageInfoController = loader.getController();
            imageInfoController.setStage(stage);
            imageInfoController.setImageProperty(imageProperty);
            stage.showAndWait();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Exception: ", e);
            UsersAlert.throwingException(e);
        }
    }

    public ObservableList<ImageProperty> getImageProperties() {
        return imageProperties;
    }

    private ObservableList<ImageProperty> imageProperties;

    public boolean aboutFile(ObservableList<ImageProperty> image) {
        try {
            FXMLLoader loader = new FXMLLoader();
            AnchorPane pane = loader.load(getClass().getResourceAsStream("views/result/newResult.fxml"));
            Stage aboutFileStage = new Stage();
            aboutFileStage.setTitle("Файлы в базе данных");
            aboutFileStage.getIcons().add(new Image(getClass().getResourceAsStream("views/images/database.png")));
            aboutFileStage.initModality(Modality.WINDOW_MODAL);
            aboutFileStage.initOwner(primaryStage);
            aboutFileStage.setScene(new Scene(pane));
            DatabaseResultController databaseResultController = loader.getController();
            databaseResultController.setStage(aboutFileStage);
            databaseResultController.setMain(this);
            databaseResultController.setImage(image);
            databaseResultController.setStage(aboutFileStage);
            imageProperties = databaseResultController.getImage();
            aboutFileStage.setResizable(false);
            aboutFileStage.showAndWait();
            return databaseResultController.isBack();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Exception: ", e);
            UsersAlert.throwingException(e);
            return false;
        }
    }

    private ObservableList<ImageProperty> images = FXCollections.observableArrayList();

    public void setImages(ObservableList<ImageProperty> images) {
        this.images = images;
    }

    public ObservableList<ImageProperty> getImages() {
        return images;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
 }
