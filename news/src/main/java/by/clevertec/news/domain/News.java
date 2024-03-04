package by.clevertec.news.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@FieldNameConstants
@NamedEntityGraph(name = "news-with-author", attributeNodes = {@NamedAttributeNode("author")})
public class News {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime time;

    @Column(nullable = false)
    private String title;
    private String text;

    @OneToMany(
            mappedBy = "news",
            fetch = FetchType.LAZY)
    private List<Comment> comments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @PrePersist
    public void prePersist() {
        if (this.time == null) {
            this.time = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.time = LocalDateTime.now();
    }
}


//    @ElementCollection
//    private List<Long> commentIds;