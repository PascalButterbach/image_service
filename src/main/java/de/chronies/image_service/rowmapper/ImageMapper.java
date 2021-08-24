package de.chronies.image_service.rowmapper;

import de.chronies.image_service.model.Image;
import org.springframework.stereotype.Component;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Component
public class ImageMapper implements RowMapper<Image> {

    @Override
    public Image mapRow(ResultSet resultSet, int i) throws SQLException {
        return Image.builder()
                .image_id(resultSet.getInt("image_id"))
                .user_id(resultSet.getInt("user_id"))
                .path(resultSet.getString("path"))
                .path_thumbnail(resultSet.getString("path_thumbnail"))
                .path_original(resultSet.getString("path_original"))
                .created(resultSet.getObject("created", LocalDateTime.class))
                .changed(resultSet.getObject("changed", LocalDateTime.class))
                .deleted(resultSet.getBoolean("deleted"))
                .build();
    }

}
