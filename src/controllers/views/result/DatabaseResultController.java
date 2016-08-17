package controllers.views.result;

import controllers.Main;
import entity.ImageProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.hibernate.Query;
import org.hibernate.Session;
import util.HibernateUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Image database
 * Created by Kostya Nirchenko.
 *
 * @since 01.08.2016
 */
public class DatabaseResultController {

    public Button backButton;
    public Button aboutFileButton;
    public TextField searchField;
    public Button searchButton;
    public ImageView imageView;
    public Label pathLabel;
    public Label dateLabel;
    public Label timeLabel;
    public Label plateLabel;
    public RadioButton dateSearch;
    public RadioButton timeSearch;
    public DatePicker dateBegin;
    public DatePicker dateEnd;
    public TextField timeBegin;
    public TextField timeEnd;
    public Button selectAllButton;
    private Stage stage;
    private boolean back = false;
    private ObservableList<ImageProperty> image;
    private Main main;
    private ToggleGroup toggleGroup = new ToggleGroup();

    public TableView<ImageProperty> database;
    public TableColumn<ImageProperty, String> numberColumn;
    public TableColumn<ImageProperty, String> pathColumn;
    public TableColumn<ImageProperty, String> dateColumn;
    public TableColumn<ImageProperty, String> timeColumn;
    public TableColumn<ImageProperty, String> plateColumn;

    @FXML
    public void initialize() {
        dateSearch.setToggleGroup(toggleGroup);
        timeSearch.setToggleGroup(toggleGroup);
        dateBegin.setDisable(true);
        dateEnd.setDisable(true);
        timeBegin.setDisable(true);
        timeEnd.setDisable(true);
        selectAllButton.setVisible(false);
        numberColumn.setCellValueFactory(cellData -> cellData.getValue().numberProperty());
        pathColumn.setCellValueFactory(cellData -> cellData.getValue().pathProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        timeColumn.setCellValueFactory(celData -> celData.getValue().timeProperty());
        plateColumn.setCellValueFactory(cellData -> cellData.getValue().plateProperty());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setImage(ObservableList<ImageProperty> image) {
        this.image = image;
    }

    public ObservableList<ImageProperty> getImage() {
        return image;
    }

    public boolean isBack() {
        return back;
    }

    private SortedList<ImageProperty> getSortedData(ObservableList<ImageProperty> images) {
        FilteredList<ImageProperty> filteredData = new FilteredList<ImageProperty>(images, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(image -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                if (image.getPlateProperty().contains(newValue)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<ImageProperty> sortedList = new SortedList<ImageProperty>(filteredData);
        sortedList.comparatorProperty().bind(database.comparatorProperty());
        return sortedList;
    }

    public void setMain(Main main) {
        this.main = main;
        database.setItems(getSortedData(main.getImages()));
    }

    public void backAction(ActionEvent actionEvent) {
        stage.close();
    }

    public void aboutFileAction(ActionEvent actionEvent) throws IOException {
        ImageProperty imageProperty = database.getSelectionModel().getSelectedItem();
        if (imageProperty != null) {
            if (!new File(imageProperty.getPathProperty()).exists()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Невозможно загрузить. Данный файл отсутствует на HDD");
                alert.showAndWait();
            }
            else main.showImageInfo(imageProperty);
        }
    }

    public void searchButtonAction(ActionEvent actionEvent) {
        // TODO реализовать поиск по дате, по времени (указывается определенный период времени)
        // select * from image where screenDate between '26' and '30' and screenMonth = '7';
        Query query;
        List<entity.Image> images = null;
        ObservableList<ImageProperty> imageProperties = FXCollections.observableArrayList();
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        if (dateSearch.isSelected()) {
            String date = dateBegin.getValue().toString();
            String date1 = dateEnd.getValue().toString();
            query = session.createQuery("from Image where screenDate between :date and :date1 and screenMonth between :month and :month1")
                    .setString("date", date.substring(8))
                    .setString("date1", date1.substring(8))
                    .setString("month", date.substring(5, 7))
                    .setString("month1", date1.substring(5, 7));
            images = (List<entity.Image>) query.list();
            session.getTransaction().commit();
            session.close();
            for (entity.Image i : images) {
                System.out.println(i.getImagePath());
            }
        } else if (timeSearch.isSelected()){
            String time = "";
            String time1 = "";
            if (timeBegin.getText().length() <= 2 && timeEnd.getText().length() <= 2) {
                time = timeBegin.getText();
                time1 = timeEnd.getText();
                query = session.createQuery("from Image where screenHour between :hour and :hour1")
                        .setString("hour", time)
                        .setString("hour1", time1);
                images = (List<entity.Image>) query.list();
                session.getTransaction().commit();
                session.close();
            } else {
                time = timeBegin.getText();
                time1 = timeEnd.getText();
                query = session.createQuery("from Image where screenHour between :hour and :hour1 and screenMinute between :minute and :minute1")
                        .setString("hour", time)
                        .setString("hour1", time1)
                        .setString("minute", time.substring(3))
                        .setString("minute1", time.substring(3));
                images = (List<entity.Image>) query.list();
                session.getTransaction().commit();
                session.close();
            }
        }
        assert images != null;
        imageProperties.addAll(images.stream().map(i -> new ImageProperty(
                "" + i.getImageId() + "",
                i.getImagePath(),
                i.getScreenYear() + "." + i.getScreenMonth() + "." + i.getScreenDate(),
                i.getScreenHour() + ":" + i.getScreenMinute() + ":" + i.getScreenSecond(),
                i.getPlate())).collect(Collectors.toList()));
        database.setItems(imageProperties);
        selectAllButton.setVisible(true);
//        imageProperties.clear();
//        images.clear();
    }

    private void createSearchDialog() {
//        final
    }

    public void info(Event event) {
        if (!new File(database.getSelectionModel().getSelectedItem().getPathProperty()).exists()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Невозможно загрузить. Данный файл отсутствует на HDD");
            alert.showAndWait();
        } else {
            imageView.setImage(new Image("file:" + database.getSelectionModel().getSelectedItem().getPathProperty()));
            pathLabel.setText(database.getSelectionModel().getSelectedItem().getPathProperty());
            dateLabel.setText(database.getSelectionModel().getSelectedItem().getDateProperty());
            timeLabel.setText(database.getSelectionModel().getSelectedItem().getTimeProperty());
            plateLabel.setText(database.getSelectionModel().getSelectedItem().getPlateProperty());
        }
    }

    public void dateSearchAction(ActionEvent actionEvent) {
        if (!timeBegin.isDisable() && !timeEnd.isDisable()) {
            timeBegin.setDisable(true);
            timeEnd.setDisable(true);
        }
        dateBegin.setDisable(false);
        dateEnd.setDisable(false);
    }

    public void timeSearchAction(ActionEvent actionEvent) {
        if (!dateBegin.isDisable() && !dateEnd.isDisable()) {
            dateBegin.setDisable(true);
            dateEnd.setDisable(true);
        }
        timeBegin.setDisable(false);
        timeEnd.setDisable(false);
    }

    public void selectAllAction(ActionEvent actionEvent) {
        database.setItems(getSortedData(main.getImages()));
        selectAllButton.setVisible(false);
    }
}
