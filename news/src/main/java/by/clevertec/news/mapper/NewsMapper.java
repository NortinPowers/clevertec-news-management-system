package by.clevertec.news.mapper;

import by.clevertec.news.domain.News;
import by.clevertec.news.mapper.annotation.DateMapping;
import by.clevertec.news.mapper.util.DateConverter;
import by.clevertec.request.NewsPathRequestDto;
import by.clevertec.request.NewsRequestDto;
import by.clevertec.response.NewsResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CustomMapperConfig.class)
public interface NewsMapper extends DateConverter {

    /**
     * Преобразует объект типа {@link News} в объект {@link NewsResponseDto} с использованием
     * маппинга, определенного аннотацией {@link DateMapping}.
     *
     * @param entity Объект типа {@link News}, который требуется преобразовать в {@link NewsResponseDto}.
     * @return Объект {@link NewsResponseDto}, созданный на основе данных из объекта {@link News}.
     */
    @DateMapping
    @Mapping(source = "author.name", target = "author")
    NewsResponseDto toDto(News entity);

    /**
     * Преобразует объект типа {@link NewsRequestDto} в объект {@link News}.
     *
     * @param dto Объект типа {@link NewsRequestDto}, который требуется преобразовать в {@link News}.
     * @return Объект {@link News}, созданный на основе данных из объекта {@link NewsRequestDto}.
     */
    News toDomain(NewsRequestDto dto);

    /**
     * Преобразует объект типа {@link NewsPathRequestDto} в объект {@link News}.
     *
     * @param dto Объект типа {@link NewsPathRequestDto}, который требуется преобразовать в {@link News}.
     * @return Объект {@link News}, созданный на основе данных из объекта {@link NewsPathRequestDto}.
     */
    News toDomain(NewsPathRequestDto dto);

    /**
     * Обновляет существующий объект типа {@link News} данными из объекта {@link News updated}.
     *
     * @param news    Объект типа {@link News}, который требуется обновить.
     * @param updated Объект типа {@link News}, содержащий обновленные данные.
     * @return Обновленный объект типа {@link News}.
     */
    News merge(@MappingTarget News news, News updated);
}
