package entity;

import java.io.Serializable;

/**
 * Created by Kostya Nirchenko.
 *
 * @since 30.07.2016
 */
public class Image implements Serializable {

    private static final long serialVersionUID = 1L;

    private int imageId;
    private String imagePath;
    private String screenYear;
    private String screenMonth;
    private String screenDate;
    private String screenHour;
    private String screenMinute;
    private String screenSecond;
    private String plate;

    public Image(String imagePath, String screenYear, String screenMonth, String screenDate, String screenHour, String screenMinute, String screenSecond, String plate) {
        this.imagePath = imagePath;
        this.screenYear = screenYear;
        this.screenMonth = screenMonth;
        this.screenDate = screenDate;
        this.screenHour = screenHour;
        this.screenMinute = screenMinute;
        this.screenSecond = screenSecond;
        this.plate = plate;
    }

    public Image(){}

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getScreenYear() {
        return screenYear;
    }

    public void setScreenYear(String screenYear) {
        this.screenYear = screenYear;
    }

    public String getScreenMonth() {
        return screenMonth;
    }

    public void setScreenMonth(String screenMonth) {
        this.screenMonth = screenMonth;
    }

    public String getScreenDate() {
        return screenDate;
    }

    public void setScreenDate(String screenDate) {
        this.screenDate = screenDate;
    }

    public String getScreenHour() {
        return screenHour;
    }

    public void setScreenHour(String screenHour) {
        this.screenHour = screenHour;
    }

    public String getScreenMinute() {
        return screenMinute;
    }

    public void setScreenMinute(String screenMinute) {
        this.screenMinute = screenMinute;
    }

    public String getScreenSecond() {
        return screenSecond;
    }

    public void setScreenSecond(String screenSecond) {
        this.screenSecond = screenSecond;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }
}
