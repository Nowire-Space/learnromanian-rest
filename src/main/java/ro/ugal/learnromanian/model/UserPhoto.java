package ro.ugal.learnromanian.model;

import javax.persistence.*;

@Entity
@Table(name="user_photo")
public class UserPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="photo_id")
    protected Integer photoId;

    @Column(name="photo_name")
    protected String photoName;

    @Column(name="photo_type")
    protected String photoType;

    @Column(name="photo_content")
    protected byte[] photoContent;

    public UserPhoto() {}

    public UserPhoto(String photoName, String photoType, byte[] photoContent) {
        this.photoName = photoName;
        this.photoType = photoType;
        this.photoContent = photoContent;
    }

    public UserPhoto(Integer photoId, String photoName, String photoType, byte[] photoContent) {
        this.photoId = photoId;
        this.photoName = photoName;
        this.photoType = photoType;
        this.photoContent = photoContent;
    }

    public Integer getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Integer photoId) {
        this.photoId = photoId;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public String getPhotoType() {
        return photoType;
    }

    public void setPhotoType(String photoType) {
        this.photoType = photoType;
    }

    public byte[] getPhotoContent() {
        return photoContent;
    }

    public void setPhotoContent(byte[] photoContent) {
        this.photoContent = photoContent;
    }
}
