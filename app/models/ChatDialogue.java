package models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChatDialogue {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String instruction;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String question;

    @Column(columnDefinition = "TEXT")
    private String response;

    @Column(nullable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(nullable = false)
    private String email;
}
