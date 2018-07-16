package com.zoe.entity;
import java.io.Serializable;
/**
 * Created by scorp on 2016/12/29.
 */
public class VersionEntity implements Serializable,Cloneable {

    /**
     * 注释内容
     */
    //private static final long serialVersionUID = 2942522063502552802L;
    private static final long serialVersionUID = 2954322060072552802L;  //8cf555ba-e68e-474b-861c-b47a83e6d287

    public String version;

    public String url;

    public String description;

    @Override
    public Object clone() throws CloneNotSupportedException {
        VersionEntity item = new VersionEntity();
        item.version = this.version;
        item.url = this.url;
        item.description = this.description;
        return item;
    }

}