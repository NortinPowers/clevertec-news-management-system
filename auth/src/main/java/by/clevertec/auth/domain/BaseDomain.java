package by.clevertec.auth.domain;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

@Getter
@Setter
@MappedSuperclass
@EqualsAndHashCode
@FieldNameConstants
public class BaseDomain implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
}
