package vistar.practice.demo.dtos.photo;

import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PhotoDto {

    private Long id;
    private Long ownerId;
    private String iconUrl;
    private String storageUrl;
    private Boolean isShown;

    @PastOrPresent
    private LocalDateTime createdAt;
    private Boolean isAvatar;
}
