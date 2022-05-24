package project.diploma.agreement.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "image")
@Data
@NoArgsConstructor
public class ImageDB {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Lob
    @Type(type = "org.hibernate.type.ImageType")
    @Column(name = "data")
    @JsonIgnore
    private byte[] data;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;

    public ImageDB(String name, String type, byte[] data, User user) {
        this.name = name;
        this.type = type;
        this.data = data;
        this.user = user;
    }

    @Override
    public String toString() {
        return "ImageDB{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
