package de.chronies.image_service.config;

import de.chronies.image_service.model.FileType;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FileTypeComponent {

    @Bean
    public List<FileType> getFileTypes(){
        List<FileType> fileTypeList = new ArrayList<>();

        // source -> https://en.wikipedia.org/wiki/List_of_file_signatures

        fileTypeList.add(FileType.builder()
                .mimeType("image/jpeg")
                .fileEnding(".jpg")
                .bytes(new String[]{"FF D8 FF DB",
                        "FF D8 FF E0 00 10 4A 46 49 46 00 01",
                        "FF D8 FF EE",
                        "FF D8 FF E1 * * 45 78 69 66 00 00"})
                .build());

        fileTypeList.add(FileType.builder()
                .mimeType("image/png")
                .fileEnding(".png")
                .bytes(new String[]{"89 50 4E 47 0D 0A 1A 0A"})
                .build());

        fileTypeList.add(FileType.builder()
                .mimeType("image/gif")
                .fileEnding(".gif")
                .bytes(new String[]{"47 49 46 38 37 61",
                        "47 49 46 38 39 61"})
                .build());

        return fileTypeList;
    }

}
