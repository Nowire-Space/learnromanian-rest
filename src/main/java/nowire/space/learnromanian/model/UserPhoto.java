package nowire.space.learnromanian.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

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
