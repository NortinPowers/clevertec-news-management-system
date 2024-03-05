package by.clevertec.auth.mapper;

import static org.junit.Assert.assertEquals;

import by.clevertec.auth.BaseDto;
import by.clevertec.auth.domain.BaseDomain;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BaseMapperTest {

    @Autowired
    private BaseMapper baseMapper;

    @Test
    void convertToDtoShouldReturnBaseDomain() {
        Long id = 1L;
        BaseDto baseDto = new BaseDto();
        baseDto.setId(id);
        BaseDomain baseEntity = new BaseDomain();
        baseEntity.setId(id);

        assertEquals(baseDto, baseMapper.convertToDto(baseEntity));
    }
}
