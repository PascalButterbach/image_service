package de.chronies.image_service.config;

import com.jcraft.jsch.ChannelSftp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;

import java.util.Properties;

@Configuration
public class FtpConfig {

    @Value("${ftp.host}")
    private String HOST;

    @Value("${ftp.username}")
    private String USER;

    @Value("${ftp.password}")
    private String PASSWORD;

    @Value("${ftp.port}")
    private int PORT;

    @Bean
    public SessionFactory<ChannelSftp.LsEntry> sftpSessionFactory() {
        Properties config = new java.util.Properties();
        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory(true);
        factory.setSessionConfig(config);
        factory.setHost(HOST);
        factory.setPort(PORT);
        factory.setUser(USER);
        factory.setPassword(PASSWORD);
        factory.setAllowUnknownKeys(true);
        return new CachingSessionFactory<>(factory);
    }

}
