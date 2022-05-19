package project.diploma.agreement.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "solution")
@Data
@EqualsAndHashCode(exclude = {"task", "author", "comment"})
@NoArgsConstructor
public class Solution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "text")
    private String text;

    @Column(name = "mark")
    private Integer mark;

    @Column(name = "return_flag")
    private Boolean returnFlag;

    @Column(name = "datetime")
    private LocalDateTime dateTime;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EStatus status;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "solution")
    private List<Comment> comment = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "solution")
    @JsonManagedReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<FileDB> files = new ArrayList<>();

    public Solution(Task task, User author) {
        this.task = task;
        this.author = author;
    }

    public Solution(String text, LocalDateTime dateTime, Task task, User author) {
        this.text = text;
        this.dateTime = dateTime;
        this.task = task;
        this.author = author;
    }

    public Solution(String text, Integer mark, Boolean returnFlag, LocalDateTime dateTime, EStatus status, Task task, User author) {
        this.text = text;
        this.mark = mark;
        this.returnFlag = returnFlag;
        this.dateTime = dateTime;
        this.status = status;
        this.task = task;
        this.author = author;
    }

    @Override
    public String toString() {
        return "Solution{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", mark=" + mark +
                ", returnFlag=" + returnFlag +
                ", dateTime=" + dateTime +
                '}';
    }
}
