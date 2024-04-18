package vistar.practice.demo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vistar.practice.demo.dtos.reaction.ReactionCreateEditDto;
import vistar.practice.demo.dtos.reaction.ReactionReadDto;
import vistar.practice.demo.mappers.ReactionMapper;
import vistar.practice.demo.models.ReactionsPhotosEntity;
import vistar.practice.demo.repositories.ReactionRepository;
import vistar.practice.demo.repositories.ReactionsPhotosRepository;
import vistar.practice.demo.repositories.UserRepository;
import vistar.practice.demo.repositories.photo.PhotoRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(transactionManager = "transactionManager")
public class ReactionService {
    private final ReactionRepository reactionRepository;
    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;
    private final ReactionsPhotosRepository reactionsPhotosRepository;
    private final ReactionMapper mapper;

    @Transactional(transactionManager = "transactionManager", readOnly = true)
    public List<ReactionReadDto> findAll() {
        return reactionRepository.findAll().stream()
                .map(mapper::toReadDto)
                .toList();
    }

    @Transactional(transactionManager = "transactionManager", readOnly = true)
    public Optional<ReactionReadDto> findById(Long id) {
        return reactionRepository.findById(id)
                .map(mapper::toReadDto);
    }

    public ReactionReadDto create(ReactionCreateEditDto createEditDto) {
        return Optional.of(createEditDto)
                .map(mapper::toEntity)
                .map(reactionRepository::save)
                .map(mapper::toReadDto)
                .orElseThrow();
    }

    public Optional<ReactionReadDto> update(Long id, ReactionCreateEditDto createEditDto) {
        return reactionRepository.findById(id)
                .map(entity -> mapper.copyToEntityFromDto(entity, createEditDto))
                .map(reactionRepository::save)
                .map(mapper::toReadDto);
    }

    public boolean delete(Long id) {
        return reactionRepository.findById(id)
                .map((entity) -> {
                    reactionRepository.delete(entity);
                    return true;
                })
                .orElse(false);
    }

    @Transactional(transactionManager = "transactionManager", readOnly = true)
    public List<ReactionReadDto> getAllReactionsByPhotoId(Long photoId) {
        return reactionsPhotosRepository.findAllByPhoto(
                        photoRepository.findById(photoId).orElseThrow(
                                //todo->insert instance of RuntimeException, message NOT_FOUND(PhotoNotFound)
                        )
                ).stream()
                .map(
                        s -> mapper.toReadDto(s.getReaction())
                ).toList();
    }


    public ReactionReadDto reactOnPhoto(Long photoId, ReactionCreateEditDto createEditDto, String username) {
        var photo = photoRepository.findById(photoId).orElseThrow(
                //todo->insert instance of RuntimeException, message NOT_FOUND(PhotoNotFound)
        );
        var user = userRepository.findByUsername(username).orElseThrow(
                //todo->insert instance of RuntimeException, message NOT_FOUND(UserNotFound)
        );
        var reaction = mapper.toEntity(createEditDto);
        return mapper.toReadDto(reactionsPhotosRepository.save(
                ReactionsPhotosEntity.builder()
                        .reactionBy(user)
                        .photo(photo)
                        .reaction(reaction)
                        .build()
        ).getReaction());
    }

    public void removeReactionFromPhoto(Long photoId, ReactionCreateEditDto createEditDto, String username) {
        var user = userRepository.findByUsername(username).orElseThrow(
                //todo->insert instance of RuntimeException, message NOT_FOUND(UserNotFound)
        );
        var photo = photoRepository.findById(photoId).orElseThrow(
                //todo->insert instance of RuntimeException, message NOT_FOUND(PhotoNotFound)
        );

        //todo-> add isActive field to reactionsPhotosEntity, and switch it here,
        // also add filter by isActive in other methods
        reactionsPhotosRepository.delete(reactionsPhotosRepository.findByPhotoAndReactionBy(
                        photo,
                        user
                ).orElseThrow(
                        //todo->insert instance of RuntimeException, message NOT_FOUND(PhotoReactionNotFound)
                )
        );

    }


}
