package vistar.practice.demo.dtos.photo;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PhotoStorageDto {

    private byte[] data;
    private Long ownerId;
    private Boolean isAvatar;
    private String suffix;
}
