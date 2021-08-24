package de.chronies.image_service.repository;

import de.chronies.image_service.exceptions.ApiException;
import de.chronies.image_service.model.Image;
import de.chronies.image_service.rowmapper.ImageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ImageRepository implements ObjectRepository<Image> {

    private final JdbcTemplate jdbcTemplate;
    private final ImageMapper rowMapper;

    @Override
    public boolean update(Image image) {
        return false;
    }

    @Override
    public boolean create(Image image) {
        String sql = "INSERT INTO image_service.images VALUES( default,?,?,?,?,current_timestamp,null,null)";

        boolean result;

        try {
            result = jdbcTemplate.update(sql,
                    image.getUser_id(),
                    image.getPath(),
                    image.getPath_thumbnail(),
                    image.getPath_original()) > 0;
        } catch (DataAccessException e) {
            throw new ApiException("Something went wrong. Contact support or try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return result;
    }

    @Override
    public Optional<Image> get(int id) {
        return Optional.empty();
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
