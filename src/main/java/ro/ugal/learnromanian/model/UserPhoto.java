package ro.ugal.learnromanian.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
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
}
