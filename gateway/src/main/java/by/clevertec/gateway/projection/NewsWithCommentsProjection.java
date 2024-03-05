package by.clevertec.gateway.projection;

import by.clevertec.response.CommentResponseDto;
import by.clevertec.response.NewsResponseDto;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "NewsWithComments", types = NewsResponseDto.class)
public interface NewsWithCommentsProjection {

    LocalDateTime getTime();

    String getTitle();

    String getText();

    String getAuthor();

    Page<CommentResponseDto> getComments();
}
