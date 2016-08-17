package util;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * Created by Kostya Nirchenko.
 *
 * @since 30.07.2016
 */
public class HibernateUtil {

    private static SessionFactory sessionFactory;
    private static ServiceRegistry serviceRegistry;

    public static SessionFactory createSessionFactory() throws HibernateException {
        Configuration configuration = new Configuration();
        configuration.configure();
        serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        return sessionFactory;
    }

    public static SessionFactory getSessionFactory() {
        return createSessionFactory();
    }

/*    public static void main(String[] args) {
        File file = new File("C:\\2016_07_26_17_22_39_836_1_1-BT3397BC.jpg");
        Session session = getSessionFactory().openSession();
        session.beginTransaction();
        try {
            entity.Image image = new entity.Image(
                    file.getPath(),
                    file.getName().substring(0, 4),
                    file.getName().substring(5, 7),
                    file.getName().substring(8, 10),
                    file.getName().substring(11, 13),
                    file.getName().substring(14, 16),
                    file.getName().substring(17, 19),
                    file.getName().substring(20, file.getName().indexOf(".")) // Maybe file path will be need with extension (in code without indexOf("."))
            );
            session.save(image);
            session.getTransaction().commit();
            System.out.println(image.getImageId());
        } catch (HibernateException e) {
            e.printStackTrace();
        } finally {
            if (session.isOpen() && session != null) {
                session.close();
            }
        }
    }*/
}
