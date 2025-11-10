package hyos1.myapp.repository.user.jdbc;

import hyos1.myapp.dto.UserUpdateDto;
import hyos1.myapp.entity.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class UserJdbcRepository {

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public UserJdbcRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
    }

    public User save(User user) {
//        SqlParameterSource param = new BeanPropertySqlParameterSource(user); // boolean 타입 매핑 안됨.
//        String sql = "insert into items(name, email, password, user_type, is_deleted) values(:name, :email, :password, :userType, :is_deleted)";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("name", user.getName())
                .addValue("email", user.getEmail())
                .addValue("password", user.getPassword())
                .addValue("user_role", user.getUserRole().name())
                .addValue("is_deleted", user.isDeleted());
        Number key = jdbcInsert.executeAndReturnKey(param);
        user.setId(key.longValue());
        return user;
    }

    public void update(Long id, UserUpdateDto dto) {
        String sql = "update users set name = :name, email = :email where user_id = :id";
//        new BeanPropertySqlParameterSource(dto)
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("name", dto.getName())
                .addValue("email", dto.getEmail())
                .addValue("id", id);
        template.update(sql, param);
    }

    public Optional<User> findById(Long id) {
        String sql = "select user_id as id, name, email, password, user_role from users where user_id = :id";
        try {
            SqlParameterSource param = new MapSqlParameterSource()
                    .addValue("id", id);
            User user = template.queryForObject(sql, param, userRowMapper());
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


    public List<User> findAll() {
        String sql = "select user_id as id, name, email, password, user_role as userRole, is_deleted from users";
        return template.query(sql, userRowMapper());
    }

    private RowMapper<User> userRowMapper() {
        return BeanPropertyRowMapper.newInstance(User.class);
    }
}
