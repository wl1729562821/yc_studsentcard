package cc.manbu.schoolinfocommunication.bean;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by manbuAndroid5 on 2016/9/5.
 */
public class ContactEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 联系人名称 **/
    private String name;
    /** 联系人号码 **/
    private String number;
    /** 联系人头像 **/
    private Bitmap photo;
    private long photoid;

    public ContactEntity(String name, String number, Bitmap photo,long photoid) {
        super();

        this.name = name;
        this.number = number;
        this.photo = photo;
        this.photoid = photoid;
    }

    public ContactEntity(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public long getPhotoid() {
        return photoid;
    }

    public void setPhotoid(long photoid) {
        this.photoid = photoid;
    }
}
