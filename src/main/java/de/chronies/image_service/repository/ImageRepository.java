package de.chronies.image_service.repository;

import de.chronies.image_service.exceptions.ApiException;
import de.chronies.image_service.model.Image;
import de.chronies.image_service.rowmapper.ImageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
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
    public Optional<Image> get(int image_id) {
        String sql = "SELECT * FROM image_service.images WHERE image_id = ?";

        Image image = null;
        try {
            image = jdbcTemplate.queryForObject(sql, rowMapper, image_id);
        } catch (Exception e) {
            // no actions required -> return empty optional
        }

        return Optional.ofNullable(image);
    }

    public List<Image> getAll(){
        String sql = "SELECT * FROM image_service.images";
        return jdbcTemplate.query(sql,rowMapper);
    }


    @Override
    public boolean delete(int image_id) {
        String sql = "DELETE FROM image_service.images  WHERE image_id = ?";
        return jdbcTemplate.update(sql, image_id) > 0;
    }
}
