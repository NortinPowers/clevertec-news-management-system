package by.clevertec.auth.mapper;

import by.clevertec.auth.BaseDto;
import by.clevertec.auth.domain.BaseDomain;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BaseMapper {

    /**
     * Преобразует объект {@link BaseDomain} в объект передачи данных (DTO).
     *
     * @param baseDomain объект для преобразования.
     * @return Объект {@link BaseDto}.
     */
    BaseDto convertToDto(BaseDomain baseDomain);
}
