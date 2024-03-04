package by.clevertec.comment.domain;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@FieldNameConstants
@Table(name = "comments")
@NamedEntityGraph(name = "comment-with-news-id-and-author", attributeNodes = {@NamedAttributeNode("author"), @NamedAttributeNode("news")})
public class Comment {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime time;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private String username;

//    @Column(nullable = false)
//    private Long newsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id", nullable = false)
    private News news;

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
