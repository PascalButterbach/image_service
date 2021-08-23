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
    private String host;

    @Value("${ftp.username}")
    private String user;

    @Value("${ftp.password}")
    private String password;

    @Value("${ftp.port}")
    private int port;

    @Bean
    public SessionFactory<ChannelSftp.LsEntry> sftpSessionFactory() {
        Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");

        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory(true);

        factory.setSessionConfig(config);

        factory.setHost(host);
        factory.setPort(port);
        factory.setUser(user);
        factory.setPassword(password);
        factory.setAllowUnknownKeys(true);
        return new CachingSessionFactory<>(factory);
    }
}
