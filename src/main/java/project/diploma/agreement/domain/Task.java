package project.diploma.agreement.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "task")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"module", "solutions"})
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "type_assessment")
    private Integer typeAssessment;

    @Column(name = "deadline")
    private LocalDateTime deadLine;

    @ManyToOne
    @JoinColumn(name = "module_id")
    @JsonBackReference
    private Module module;

    @OneToMany(mappedBy = "task")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Solution> solutions;

    public Task(String title, String description, Integer typeAssessment, LocalDateTime deadLine) {
        this.title = title;
        this.description = description;
        this.typeAssessment = typeAssessment;
        this.deadLine = deadLine;
    }

    public Task(String title, String description, Integer typeAssessment, LocalDateTime deadLine, Module module) {
        this.title = title;
        this.description = description;
        this.typeAssessment = typeAssessment;
        this.deadLine = deadLine;
        this.module = module;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", typeAssessment=" + typeAssessment +
                ", deadline=" + deadLine +
                '}';
    }
}
