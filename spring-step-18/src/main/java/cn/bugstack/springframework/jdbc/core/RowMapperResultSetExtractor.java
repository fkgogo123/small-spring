package cn.bugstack.springframework.jdbc.core;

import cn.hutool.core.lang.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RowMapperResultSetExtractor<T> implements ResultSetExtractor<List<T>> {

    private final RowMapper<T> rowMapper;

    private final int rowsExpected;

    public RowMapperResultSetExtractor(RowMapper<T> rowMapper) {
        this(rowMapper, 0);
    }

    public RowMapperResultSetExtractor(RowMapper<T> rowMapper, int rowsExpected) {
        Assert.notNull(rowMapper, "RowMapper is required");
        this.rowMapper = rowMapper;
        this.rowsExpected = rowsExpected;
    }

    /**
     * 对执行结果 rs 进行解析。遍历每一行，在通过rowMapper.mapRow遍历每一列。
     * 泛型T： Map<String, Object>
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    @Override
    public List<T> extractData(ResultSet rs) throws SQLException {
        List<T> results = this.rowsExpected > 0 ? new ArrayList<>(this.rowsExpected) : new ArrayList<>();
        int rowNum = 0;
        // 遍历结果集
        while (rs.next()) {
            results.add(this.rowMapper.mapRow(rs, rowNum++));
        }
        return results;
    }
}
