package project.diploma.agreement.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"roles", "modules", "solutions", "subscribers", "subscriptions"})
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "fio")
    private String fio;

    @Column(name = "university")
    private String university;

    @Column(name = "group_number")
    private String groupNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "position")
    private String position;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "photo_url")
    private String photoUrl;

    @OneToMany(mappedBy = "user")
    private List<ImageDB> image = new ArrayList<>();

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_module",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "module_id")
    )
    private List<Module> modules;

    @ManyToMany
    @JoinTable(
            name = "user_subscriptions",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "subscriber_id") }
    )
    @JsonIgnoreProperties({"subscribers", "subscriptions"})
    private Set<User> subscribers = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_subscriptions",
            joinColumns = { @JoinColumn(name = "subscriber_id") },
            inverseJoinColumns = { @JoinColumn(name = "user_id") }
    )
    @JsonIgnoreProperties({"subscriptions", "subscribers"})
    private Set<User> subscriptions = new HashSet<>();


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author")
    private List<Solution> solutions;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "senderUser")
    private List<Message> messages;

    public User(String username, String password, String fio) {
        this.username = username;
        this.password = password;
        this.fio = fio;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", fio='" + fio + '\'' +
                ", university='" + university + '\'' +
                ", groupNumber='" + groupNumber + '\'' +
                ", address='" + address + '\'' +
                ", position='" + position + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
