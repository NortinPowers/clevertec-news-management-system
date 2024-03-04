package by.clevertec.auth.mapper;


import by.clevertec.auth.BaseDto;
import by.clevertec.auth.domain.BaseDomain;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BaseMapper {

    BaseDto convertToDto(BaseDomain baseDomain);
}
