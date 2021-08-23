package de.chronies.image_service.models.enums;

public enum MimeType {

    IMAGE_JPEG("image/jpeg", ".jpg", new String[]{"FF D8 FF DB",
            "FF D8 FF E0 00 10 4A 46 49 46 00 01",
            "FF D8 FF EE",
            "FF D8 FF E1 * * 45 78 69 66 00 00"}),
    IMAGE_PNG("image/png", ".png", new String[]{"89 50 4E 47 0D 0A 1A 0A"}),
    IMAGE_GIF("image/gif", ".gif", new String[]{"47 49 46 38 37 61",
            "47 49 46 38 39 61"});


    private final String mimeType;
    private final String fileEnding;
    private final String[] leadingBytes;

    MimeType(String mimeType, String fileEnding, String[] leadingBytes) {
        this.mimeType = mimeType;
        this.fileEnding = fileEnding;
        this.leadingBytes = leadingBytes;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getFileEnding() {
        return fileEnding;
    }

    public String[] getLeadingBytes() {
        return leadingBytes;
    }


    }
