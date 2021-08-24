package de.chronies.image_service.model.enums;

public enum FileSizeDir {

    THUMBNAIL("thumbnail","thumb/"),
    RESIZED("resized", "resized/"),
    ORIGINAL("original", "original/");

    private final String type;
    private final String directory;

    FileSizeDir(String type, String directory) {
        this.type = type;
        this.directory = directory;
    }

    public String getDirectory() {
        return directory;
    }

    public String getType() {
        return type;
    }
}
