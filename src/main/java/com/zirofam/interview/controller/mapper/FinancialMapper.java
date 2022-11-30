package com.zirofam.interview.controller.mapper;

import com.zirofam.interview.controller.dto.FinancialDto;
import com.zirofam.interview.controller.dto.PartialFinancialDto;
import com.zirofam.interview.controller.dto.UpdateFinancialDto;
import com.zirofam.interview.controller.model.FinancialModel;
import com.zirofam.interview.domain.FinancialEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {})
public interface FinancialMapper {

  FinancialEntity toEntity(FinancialDto dto);

  FinancialEntity updateEntity(@MappingTarget FinancialEntity financialEntity, UpdateFinancialDto dto);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  FinancialEntity updateEntity(@MappingTarget FinancialEntity financialEntity, PartialFinancialDto dto);

  FinancialModel toModel(FinancialEntity entity);

  List<FinancialModel> toModel(List<FinancialEntity> entity);
}
