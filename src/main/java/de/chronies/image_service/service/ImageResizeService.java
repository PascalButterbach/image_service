package de.chronies.image_service.service;

import de.chronies.image_service.exceptions.ApiException;
import de.chronies.image_service.model.enums.FileSizeDir;
import de.chronies.image_service.model.enums.MimeType;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ImageResizeService {

    @Value("${image.thumbnail-width-height}")
    int THUMBNAIL_WIDTH_HEIGHT;

    @Value("${image.resize-max-width}")
    int MAX_WIDTH;

    @Value("${image.min-width}")
    int MIN_WIDTH;

    @Value("${image.min-height}")
    int MIN_HEIGHT;

    public Map<FileSizeDir, byte[]> resize(MultipartFile file, MimeType fileType) throws IOException {

        BufferedImage imBuff = ImageIO.read(file.getInputStream());
        Map<FileSizeDir, byte[]> resizedFiles = new HashMap<>();

        if (imBuff.getWidth() < MIN_WIDTH || imBuff.getHeight() < MIN_HEIGHT)
            throw new ApiException("Image to small. ", HttpStatus.NOT_ACCEPTABLE);


        // resize if bigger than MAX  && is not a .gif
        if (imBuff.getWidth() > MAX_WIDTH && !fileType.getMimeType().equals("image/gif")) {
            byte[] resized = resizeImage(imBuff, fileType);
            resizedFiles.put(FileSizeDir.RESIZED, resized);
        }

        // create a thumbnail
        byte[] thumbnail = resizeToThumbnail(imBuff, fileType);
        resizedFiles.put(FileSizeDir.THUMBNAIL, thumbnail);


        return resizedFiles;
    }


    private byte[] resizeToThumbnail(BufferedImage originalImage, MimeType fileType) throws IOException {

        // CROPPING QUAD. -------------------------------->
        var minDimension = Math.min(originalImage.getWidth(), originalImage.getHeight());

        var x = 0;
        var y = 0;
        if (originalImage.getWidth() < originalImage.getHeight()) {
            y = (originalImage.getHeight() - minDimension) / 2;
        } else {
            x = (originalImage.getHeight() - minDimension) / 2;
        }
        BufferedImage croppedImage = originalImage.getSubimage(x, y, minDimension, minDimension);
        // CROPPED ----------------------------------------<

        return resize(croppedImage, fileType, THUMBNAIL_WIDTH_HEIGHT, THUMBNAIL_WIDTH_HEIGHT);
    }


    private byte[] resizeImage(BufferedImage originalImage, MimeType fileType) throws IOException {
        int original_width = originalImage.getWidth();
        int original_height = originalImage.getHeight();
        int new_width = original_width;
        int new_height = original_height;

        if (original_width > MAX_WIDTH) {
            new_width = MAX_WIDTH;
            new_height = (new_width * original_height) / original_width;
        }

        return resize(originalImage, fileType, new_width,new_height);
    }

    private byte[] resize(BufferedImage originalImage, MimeType fileType, int width, int height) throws IOException {

        BufferedImage resizedImage = new BufferedImage(width, height, originalImage.getType());
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, width, height, null);
        graphics2D.dispose();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String formatName = StringUtils.substringAfterLast(fileType.getFileEnding(), ".").toUpperCase();

        ImageIO.write(resizedImage, formatName, byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }

}
