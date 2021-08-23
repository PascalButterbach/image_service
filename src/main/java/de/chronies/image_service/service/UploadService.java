package de.chronies.image_service.service;

import com.jcraft.jsch.ChannelSftp;
import de.chronies.image_service.config.FileTypeComponent;
import de.chronies.image_service.config.FtpConfig;
import de.chronies.image_service.model.FileType;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.file.remote.session.Session;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UploadService {

    private final FtpConfig ftpConfig;
    private final FileTypeComponent fileTypeComponent;

    @Value("${ftp.baseuri}")
    String baseUri;


    public String uploadFile(MultipartFile file) throws IOException {

        Session<ChannelSftp.LsEntry> session = ftpConfig.sftpSessionFactory().getSession();

        Optional<FileType> fileType = guessContentTypeFromStream(new ByteArrayInputStream(file.getBytes()));

        // todo: better exception handling
        String filename = fileType
                .map(type -> RandomStringUtils.randomAlphanumeric(7) + type.getFileEnding())
                .orElseThrow(() -> new RuntimeException("Filetype konnte nicht geguessed werden SKKRRRR"));


        //write
        session.write(file.getInputStream(), filename);

        //remove
        //session.remove(filename);

        if (session.isOpen())
            session.close();

        return baseUri + filename;
    }


    // Todo Throw on failure (IO Error : Not Supported Error)
    private Optional<FileType> guessContentTypeFromStream(InputStream is) throws IOException {
        if (!is.markSupported())
            return Optional.empty();

        is.mark(16);
        StringBuilder signature = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            signature.append(String.format("%02X ", is.read()));
        }
        is.reset();

        for (FileType fileType : fileTypeComponent.getFileTypes()) {
            if (fileType.match(signature.toString()))
                return Optional.of(fileType);
        }

        return Optional.empty();
    }

}
