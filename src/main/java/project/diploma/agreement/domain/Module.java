package project.diploma.agreement.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "module")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"tasks", "users"})
public class Module implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    private String title;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "module")
    @JsonManagedReference
    private List<Task> tasks;

    @JsonBackReference
    @ManyToMany(mappedBy = "modules")
    private List<User> users;

    public Module(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Module{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", tasks=" + tasks +
                ", users=" + users +
                '}';
    }
}
