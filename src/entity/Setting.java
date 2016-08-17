package entity;

import java.io.Serializable;

/**
 * Created by Kostya Nirchenko.
 *
 * @since 30.07.2016
 */
public class Setting implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;
    private int articlesCount;

    public Setting(int articlesCount) {
        this.articlesCount = articlesCount;
    }

    public Setting() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArticlesCount() {
        return articlesCount;
    }

    public void setArticlesCount(int articlesCount) {
        this.articlesCount = articlesCount;
    }
}
