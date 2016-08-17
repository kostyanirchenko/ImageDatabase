package controllers.views.result.imageInfo;

import entity.ImageProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * Created by Kostya Nirchenko.
 *
 * @since 03.08.2016
 */
public class ImageInfoController {

    public ImageView image;
    public Label pathLabel;
    public Label yearLabel;
    public Label dateLabel;
    public Label plateLabel;
    public Button backButton;
    private Stage stage;
    private ImageProperty imageProperty;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setImageProperty(ImageProperty imageProperty) {
        this.imageProperty = imageProperty;
        image.setImage(new Image("file:" + imageProperty.getPathProperty()));
        setLabels();
    }

    public void backButtonAction(ActionEvent actionEvent) {
        stage.close();
    }

    private void setLabels() {
        pathLabel.setText(imageProperty.getPathProperty());
        yearLabel.setText(imageProperty.getDateProperty());
        dateLabel.setText(imageProperty.getTimeProperty());
        plateLabel.setText(imageProperty.getPlateProperty());
    }
}
