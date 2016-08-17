package entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Image database
 * Created by Kostya Nirchenko.
 *
 * @since 02.08.2016
 */
public class ImageProperty {

    private StringProperty numberProperty;
    private StringProperty pathProperty;
    private StringProperty dateProperty;
    private StringProperty timeProperty;
    private StringProperty plateProperty;

    public String getPlateProperty() {
        return plateProperty.get();
    }

    public StringProperty plateProperty() {
        return plateProperty;
    }

    public void setPlateProperty(String plateProperty) {
        this.plateProperty.set(plateProperty);
    }

    public String getTimeProperty() {
        return timeProperty.get();
    }

    public StringProperty timeProperty() {
        return timeProperty;
    }

    public void setTimeProperty(String timeProperty) {
        this.timeProperty.set(timeProperty);
    }

    public String getDateProperty() {
        return dateProperty.get();
    }

    public StringProperty dateProperty() {
        return dateProperty;
    }

    public void setDateProperty(String dateProperty) {
        this.dateProperty.set(dateProperty);
    }

    public String getPathProperty() {
        return pathProperty.get();
    }

    public StringProperty pathProperty() {
        return pathProperty;
    }

    public void setPathProperty(String pathProperty) {
        this.pathProperty.set(pathProperty);
    }

    public String getNumberProperty() {
        return numberProperty.get();
    }

    public StringProperty numberProperty() {
        return numberProperty;
    }

    public void setNumberProperty(String numberProperty) {
        this.numberProperty.set(numberProperty);
    }

    public ImageProperty() {
        this (null, null, null, null, null);
    }

    public ImageProperty(String number, String path, String date, String time, String plate) {
        numberProperty = new SimpleStringProperty(number);
        pathProperty = new SimpleStringProperty(path);
        dateProperty = new SimpleStringProperty(date);
        timeProperty = new SimpleStringProperty(time);
        plateProperty = new SimpleStringProperty(plate);
    }
}
