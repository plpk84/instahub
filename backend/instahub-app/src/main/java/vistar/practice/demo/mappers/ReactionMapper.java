package vistar.practice.demo.mappers;

import vistar.practice.demo.dtos.reaction.ReactionCreateEditDto;
import vistar.practice.demo.dtos.reaction.ReactionReadDto;
import vistar.practice.demo.models.ReactionEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ReactionMapper {
    ReactionReadDto toReadDto (ReactionEntity reactionEntity);
    ReactionEntity toEntity (ReactionCreateEditDto createEditDto);
    default ReactionEntity copyToEntityFromDto(ReactionEntity entity ,ReactionCreateEditDto createEditDto) {
        entity.setIconUrl(createEditDto.getUrl());
        entity.setName(createEditDto.getName());
        entity.setCreatedAt(createEditDto.getCreatedAt());
        return entity;
    }
}