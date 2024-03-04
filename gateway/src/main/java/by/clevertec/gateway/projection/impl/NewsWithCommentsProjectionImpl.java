package by.clevertec.gateway.projection.impl;

import by.clevertec.gateway.projection.NewsWithCommentsProjection;
import by.clevertec.response.CommentResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class NewsWithCommentsProjectionImpl implements NewsWithCommentsProjection {

    private LocalDateTime time;
    private String title;
    private String text;
    private String author;
    private Page<CommentResponseDto> comments;
}
