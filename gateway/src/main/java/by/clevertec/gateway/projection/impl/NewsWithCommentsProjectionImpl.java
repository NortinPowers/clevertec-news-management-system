package by.clevertec.gateway.projection.impl;

import by.clevertec.gateway.projection.NewsWithCommentsProjection;
import by.clevertec.response.CommentResponseDto;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@AllArgsConstructor
public class NewsWithCommentsProjectionImpl implements NewsWithCommentsProjection {

    private LocalDateTime time;
    private String title;
    private String text;
    private String author;
    private Page<CommentResponseDto> comments;
}
