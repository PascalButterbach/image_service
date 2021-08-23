package de.chronies.image_service.service;

import de.chronies.image_service.models.enums.FileSizeDir;
import de.chronies.image_service.models.enums.MimeType;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${image.resize-max-width}")
    int MAX_WIDTH;

    @Value("${image.thumbnail-width-height}")
    int THUMBNAIL_WIDTH_HEIGHT;

    public Map<FileSizeDir, byte[]> resize(MultipartFile file, MimeType fileType) throws IOException {

        BufferedImage imBuff = ImageIO.read(file.getInputStream());
        Map<FileSizeDir, byte[]> resizedFiles = new HashMap<>();

        // resize if bigger than MAX  && is not a .gif
        if (imBuff.getWidth() > MAX_WIDTH && !fileType.getMimeType().equals("image/gif")) {
            var resized = createLimitedSize(imBuff, fileType);
            resizedFiles.put(FileSizeDir.RESIZED, resized);
        }

        // create a thumbnail
        var thumbnail = createThumbnail(imBuff, fileType);
        resizedFiles.put(FileSizeDir.THUMBNAIL, thumbnail);


        return resizedFiles;
    }


    private byte[] createThumbnail(BufferedImage bufferedImage, MimeType fileType) throws IOException {


        // CROPPING QUAD. -------------------------------->
        var minDimension = Math.min(bufferedImage.getWidth(), bufferedImage.getHeight());

        var x = 0;
        var y = 0;
        if(bufferedImage.getWidth() < bufferedImage.getHeight()){
            y = (bufferedImage.getHeight() - minDimension) / 2;
        }else{
            x = (bufferedImage.getHeight() - minDimension) / 2;
        }
        BufferedImage croppedImage = bufferedImage.getSubimage(x, y, minDimension, minDimension);
        // CROPPED ----------------------------------------<


        // RESIZE TO THUMB -------------------------------->
        BufferedImage croppedImage2 = new BufferedImage(THUMBNAIL_WIDTH_HEIGHT, THUMBNAIL_WIDTH_HEIGHT, bufferedImage.getType());
        Graphics2D graphics2D = croppedImage2.createGraphics();
        graphics2D.drawImage(croppedImage,0, 0, THUMBNAIL_WIDTH_HEIGHT, THUMBNAIL_WIDTH_HEIGHT, null);
        graphics2D.dispose();
        // RESIZEd ---------------------------------------->


        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String formatName = StringUtils.substringAfterLast(fileType.getFileEnding(), ".").toUpperCase();

        ImageIO.write(croppedImage2, formatName, byteArrayOutputStream);
/*

        BufferedImage resizedImage = new BufferedImage(THUMBNAIL_WIDTH_HEIGHT, THUMBNAIL_WIDTH_HEIGHT, bufferedImage.getType());

        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(bufferedImage,0, 0, THUMBNAIL_WIDTH_HEIGHT, THUMBNAIL_WIDTH_HEIGHT, null);
        graphics2D.dispose();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        System.out.println(StringUtils.substringAfterLast(fileType.getFileEnding(), "."));

        String formatName = StringUtils.substringAfterLast(fileType.getFileEnding(), ".").toUpperCase();

        ImageIO.write(resizedImage, formatName, byteArrayOutputStream);
*/


        return byteArrayOutputStream.toByteArray();
    }



    private byte[] createLimitedSize(BufferedImage bufferedImage, MimeType fileType) throws IOException {
        return resizeImage(bufferedImage, 600, 600);
    }

    private byte[] resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, originalImage.getType());
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "gif", byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }
}
