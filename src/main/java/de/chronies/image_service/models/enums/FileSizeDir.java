package de.chronies.image_service.models.enums;

public enum FileSizeDir {

    THUMBNAIL("thumb/"),
    RESIZED("resized/"),
    ORIGINAL("original/");

    private final String directory;


    FileSizeDir(String directory) {
        this.directory = directory;
    }

    public String getDirectory() {
        return directory;
    }
}
