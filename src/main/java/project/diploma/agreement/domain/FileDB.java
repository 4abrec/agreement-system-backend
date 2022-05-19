package project.diploma.agreement.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "file")
@Data
@NoArgsConstructor
public class FileDB {

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
    @Column(name = "data")
    @JsonIgnore
    private byte[] data;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "task_id")
    private Integer taskId;

    @ManyToOne
    @JoinColumn(name = "solution_id")
    @JsonBackReference
    private Solution solution;

    public FileDB(String name, String type, byte[] data, Solution solution) {
        this.name = name;
        this.type = type;
        this.data = data;
        this.solution = solution;
    }

    public FileDB(String name, String type, byte[] data, Integer userId, Integer taskId) {
        this.name = name;
        this.type = type;
        this.data = data;
        this.userId = userId;
        this.taskId = taskId;
    }
}
