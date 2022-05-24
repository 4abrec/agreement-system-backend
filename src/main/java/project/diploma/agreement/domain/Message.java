package project.diploma.agreement.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@Data
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "text")
    private String text;

    @Column(name = "datetime")
    private LocalDateTime localDateTime;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @JsonIgnore
    private User senderUser;

    @JsonIgnore
    @JoinColumn(name = "recipient_id")
    private String recipientUser;

    public Message(String text, LocalDateTime localDateTime, User senderUser, String recipientUser) {
        this.text = text;
        this.localDateTime = localDateTime;
        this.senderUser = senderUser;
        this.recipientUser = recipientUser;
    }
}
