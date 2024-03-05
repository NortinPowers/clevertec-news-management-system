package by.clevertec.comment.mapper;

import by.clevertec.comment.domain.Comment;
import by.clevertec.comment.mapper.annotation.DateMapping;
import by.clevertec.comment.mapper.util.DateConverter;
import by.clevertec.request.CommentPathRequestDto;
import by.clevertec.request.CommentRequestDto;
import by.clevertec.response.CommentResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CustomMapperConfig.class)
public interface CommentMapper extends DateConverter {

    /**
     * Преобразует объект типа {@link Comment} в объект {@link CommentResponseDto} с использованием
     * маппинга, определенного аннотацией {@link DateMapping}.
     *
     * @param entity Объект типа {@link Comment}, который требуется преобразовать в {@link CommentResponseDto}.
     * @return Объект {@link CommentResponseDto}, созданный на основе данных из объекта {@link Comment}.
     */
    @DateMapping
    @Mapping(source = "news.id", target = "newsId")
    @Mapping(source = "author.name", target = "author")
    CommentResponseDto toDto(Comment entity);

    /**
     * Преобразует объект типа {@link CommentRequestDto} в объект {@link Comment}.
     *
     * @param dto Объект типа {@link CommentRequestDto}, который требуется преобразовать в {@link Comment}.
     * @return Объект {@link Comment}, созданный на основе данных из объекта {@link CommentRequestDto}.
     */
    @Mapping(source = "newsId", target = "news.id")
    Comment toDomain(CommentRequestDto dto);

    /**
     * Преобразует объект типа {@link CommentPathRequestDto} в объект {@link Comment}.
     *
     * @param dto Объект типа {@link CommentPathRequestDto}, который требуется преобразовать в {@link Comment}.
     * @return Объект {@link Comment}, созданный на основе данных из объекта {@link CommentPathRequestDto}.
     */
    @Mapping(source = "newsId", target = "news.id")
    Comment toDomain(CommentPathRequestDto dto);

    /**
     * Обновляет существующий объект типа {@link Comment} данными из объекта {@link Comment updated}.
     *
     * @param comment Объект типа {@link Comment}, который требуется обновить.
     * @param updated Объект типа {@link Comment}, содержащий обновленные данные.
     * @return Обновленный объект типа {@link Comment}.
     */
    Comment merge(@MappingTarget Comment comment, Comment updated);
}
