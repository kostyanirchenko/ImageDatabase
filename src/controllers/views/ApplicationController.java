package controllers.views;

import controllers.Main;
import entity.ImageProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.hibernate.Query;
import org.hibernate.Session;
import util.HibernateUtil;
import util.UsersAlert;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by Kostya Nirchenko.
 *
 * @since 30.07.2016
 */
public class ApplicationController implements Runnable {

    private static Logger logger = Logger.getLogger(ApplicationController.class.getName());

    public Button addToDatabaseButton;
    public Button aboutProgramButton;
    public Button databaseListButton;
    public Button settingButton;
    public Button exitButton;
    public AnchorPane root;
    public Button test;

    private Main main;

    DirectoryChooser directoryChooser = new DirectoryChooser();

    private final int MAX_VALUE = 32;

    private List<File> files = new ArrayList<>(); // Список переименованных файлов для добавления в БД

    public void setMain(Main main) {
        this.main = main;
    }

    public void addToDatabaseAction(ActionEvent actionEvent) {
        chooseDirectory();
    }

    private void chooseDirectory() {
        this.files.clear();
        directoryChooser.setTitle("Выберите папку с изображениями");
        loadFiles(directoryChooser.showDialog(main.getPrimaryStage()));
        run();
        try {
            databaseSession();
        } catch (Exception e) {
            UsersAlert.throwingException(e);
        }
    }

    public void run() {
        rename(this.files);
    }

    protected void databaseSession() throws Exception {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        for (File i : files) {
            try {
                entity.Image image = new entity.Image(
                        i.getPath(),
                        i.getName().substring(0, 4),
                        i.getName().substring(5, 7),
                        i.getName().substring(8, 10),
                        i.getName().substring(11, 13),
                        i.getName().substring(14, 16),
                        i.getName().substring(17, 19),
                        i.getName().substring(20, i.getName().indexOf(".")) // Maybe file path will be need with extension (in code without indexOf("."))
                );
                session.save(image);
                if (files.indexOf(i) % 20 == 0) {
                    session.flush();
                    session.clear();
                }

            } catch (Exception e) {
                logger.log(Level.SEVERE, "Exception: ", e);
                UsersAlert.throwingException(e);
            } /*finally {

            }*/
        }
        session.getTransaction().commit();
        if (session.isOpen() && session != null) {
            session.close();
        }
    }

    protected void loadFiles(File file) {
        File[] fileList = file.listFiles();
        for (File f : fileList) {
            if (f.isDirectory()) {
                loadFiles(f);
            } else {
                if (f.getName().contains("_")) {
                    files.add(f);
                }
            }
        }
    }

    private void rename(List<File> files) {
        List<File> fileList = new ArrayList<>();
        for (File file : files) {
            if (file.getName().length() <= MAX_VALUE) {
                continue;
            }
                String name = file.getName();
                String date = name.substring(0, 19).replaceAll("_", "-").concat(name.substring(27));
                file.renameTo(new File(file.getParent() + "\\".concat(date)));
                fileList.add(new File(file.getParent() + "\\".concat(date)));
        }
        System.out.println(this.files.size());
        this.files.clear();
        this.files.addAll(fileList);
        fileList.clear();
    }

    public void aboutProgramAction(ActionEvent actionEvent) {
        Alert about = new Alert(Alert.AlertType.INFORMATION);
        about.setTitle("О программе");
        about.setHeaderText("Image database");
        about.setContentText("Программный продукт предназначен для обработки различных файлов и занесения их в базу данных. " +
                "Рекомендуется использовать данный программный продукт для сортировки по базе данных сохраненных изображений, сделанных программным продуктом NomerOk. " +
                "В базе данных изображения сортируются по времени, дате. Программный продукт позволяет просмотреть изображения за определенные дни, определенный промежуток времени. \n\n" +
                "Программный продукт разработал Нырченко Константин Сергеевич, студент Херсонского национального технического университета. \n\n" +
                "© 2016");
        Stage aboutStage = (Stage) about.getDialogPane().getScene().getWindow();
        aboutStage.getIcons().add(new Image(main.getClass().getResourceAsStream("views/images/info.jpg")));
        aboutStage.showAndWait();
    }

    private ObservableList<ImageProperty> imageProperties = FXCollections.observableArrayList();

    public void databaseListAction(ActionEvent actionEvent) {
        if (main.getImageProperties() != null) {
           main.getImageProperties().clear();
        }
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            Query query = session.createQuery("from Image");
            List<entity.Image> images = (List<entity.Image>) query.list();
            session.getTransaction().commit();
            session.close();
            imageProperties.addAll(images.stream().map(i -> new ImageProperty(
                    "" + i.getImageId() + "",
                    i.getImagePath(),
                    i.getScreenYear() + "." + i.getScreenMonth() + "." + i.getScreenDate(),
                    i.getScreenHour() + ":" + i.getScreenMinute() + ":" + i.getScreenSecond(),
                    i.getPlate())).collect(Collectors.toList()));
            main.setImages(imageProperties);
            main.aboutFile(imageProperties);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception: ", e);
            UsersAlert.throwingException(e);
        }
    }

    public void settingAction(ActionEvent actionEvent) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        String currentDate =  new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss").format(System.currentTimeMillis());
        try {
            session.beginTransaction();
            String deleteMonth = Integer.parseInt(currentDate.substring(3, 5)) - 2 + "";
            if (deleteMonth.equals("0")) {
                deleteMonth = "12";
            } else if (deleteMonth.equals("1")) deleteMonth = "11";
            if (deleteMonth.length() == 1) {
                deleteMonth = "0".concat(deleteMonth);
            }
            Query q = session.createQuery("from  Image where screenMonth <= :screenMonth").setString("screenMonth", deleteMonth);
            List<entity.Image> list = (List<entity.Image>) q.list();
            session.getTransaction().commit();
            session.close();
            for (entity.Image i : list) {
                Session s = HibernateUtil.getSessionFactory().openSession();
                s.beginTransaction();
                Query qu = s.createQuery("delete from Image where imageId = :imageId").setParameter("imageId", i.getImageId());
                qu.executeUpdate();
                s.getTransaction().commit();
                s.close();
            }
            if (list.size() != 0)
                list.clear();
        } catch(Exception e) {
            logger.log(Level.SEVERE, "Exception: ", e);
            UsersAlert.throwingException(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public void exitAction(ActionEvent actionEvent) {
        System.exit(0);
    }


}
