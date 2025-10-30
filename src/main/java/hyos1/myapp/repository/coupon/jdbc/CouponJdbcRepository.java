package hyos1.myapp.repository.coupon.jdbc;

import hyos1.myapp.entity.Coupon;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class CouponJdbcRepository implements CouponRepository {

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public CouponJdbcRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("coupons")
                .usingGeneratedKeyColumns("coupon_id");
    }

    @Override
    public Coupon save(Coupon coupon) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(coupon);
        Number key = jdbcInsert.executeAndReturnKey(param);
        Long couponId = key.longValue();
        coupon.setId(couponId);
        return coupon;
    }

    @Override
    public Optional<Coupon> findById(Long couponId) {
        String sql = "select coupon_id as id, name, discount_amount, quantity, available_count, start_date, expired_date from coupons where coupon_id = :couponId";
        try {
            SqlParameterSource param = new MapSqlParameterSource()
                    .addValue("couponId", couponId);
            Coupon coupon = template.queryForObject(sql, param, couponRowMapper());
            return Optional.of(coupon);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Coupon> findAll() {
        String sql = "select coupon_id as id, name, discount_amount, quantity, available_count, start_date, expired_date from coupons";
        return template.query(sql, couponRowMapper());
    }

    private RowMapper<Coupon> couponRowMapper() {
        return BeanPropertyRowMapper.newInstance(Coupon.class);
    }
}
