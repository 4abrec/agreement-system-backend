package project.diploma.agreement.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "role")
@Data
@EqualsAndHashCode(exclude = {"users"})
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonIgnore
    private Integer id;

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private ERole name;

    @JsonBackReference
    @ManyToMany(mappedBy = "roles")
    private List<User> users;


}
