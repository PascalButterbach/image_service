package de.chronies.image_service.model;

import lombok.Builder;
import lombok.Getter;
import org.apache.commons.io.FilenameUtils;

@Builder
@Getter
public class FileType {

    private final String mimeType;
    private final String fileEnding;
    private final String[] bytes;
    private static final char wildCard = '*';

    public boolean match(String input){
        for (String aByte : bytes) {
            if (FilenameUtils.wildcardMatch(input, aByte + wildCard))
                return true;
        }
        return false;
    }

}
