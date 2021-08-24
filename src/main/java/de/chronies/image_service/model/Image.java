package de.chronies.image_service.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class Image {

    private int image_id;

    private int user_id;

    private String path;

    private String path_thumbnail;

    private String path_original;


    private LocalDateTime created;

    private LocalDateTime changed;

    private boolean deleted;
}
