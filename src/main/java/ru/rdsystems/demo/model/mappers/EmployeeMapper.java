package ru.rdsystems.demo.model.mappers;

import org.mapstruct.Mapper;
import ru.rdsystems.demo.model.dto.EmployeeDto;
import ru.rdsystems.demo.model.entities.EmployeeEntity;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

	EmployeeDto map(EmployeeEntity entity);

}
