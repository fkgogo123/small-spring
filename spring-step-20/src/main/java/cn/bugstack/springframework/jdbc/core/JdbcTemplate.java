package cn.bugstack.springframework.jdbc.core;

import cn.bugstack.springframework.jdbc.UncategorizedSQLException;
import cn.bugstack.springframework.jdbc.datasource.DataSourceUtils;
import cn.bugstack.springframework.jdbc.support.JdbcAccessor;
import cn.hutool.core.lang.Assert;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class JdbcTemplate extends JdbcAccessor implements JdbcOperations {

    private int fetchSize = 1;

    private int maxRows = -1;

    private int queryTimeout = -1;

    public JdbcTemplate(){}

    // 把数据源注入到JdbcTemplate
    public JdbcTemplate(DataSource dataSource) {
        setDateSource(dataSource);
        afterPropertiesSet();
    }

    public int getFetchSize() {
        return fetchSize;
    }

    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
    }

    public int getMaxRows() {
        return maxRows;
    }

    public void setMaxRows(int maxRows) {
        this.maxRows = maxRows;
    }

    public int getQueryTimeout() {
        return queryTimeout;
    }

    public void setQueryTimeout(int queryTimeout) {
        this.queryTimeout = queryTimeout;
    }

    /**
     * 所有 sql 语句的最终执行逻辑
     *
     * @param action
     * @return
     * @param <T>
     */
    @Override
    public <T> T execute(StatementCallback<T> action) {
        Connection con = DataSourceUtils.getConnection(obtainDataSource());
        try {
            Statement stat = con.createStatement();
            applyStateSettings(stat);
            // 调用真正的 执行语句 并进行 数据解析，以map的格式返回数据。
            return action.doInStatement(stat);
        } catch (SQLException ex) {
            throw new UncategorizedSQLException("ConnectionCallback", getSql(action), ex);
        }
    }

    private static String getSql(Object sqlProvider) {
        if (sqlProvider instanceof SqlProvider) {
            return ((SqlProvider) sqlProvider).getSql();
        } else {
            return null;
        }
    }

    // execute方法中调用了 Statement的execute方法，包装了一下。
    @Override
    public void execute(String sql) {
        class ExecuteStatementCallback implements StatementCallback<Object>, SqlProvider {

            @Override
            public String getSql() {
                return sql;
            }

            @Override
            public Object doInStatement(Statement statement) throws SQLException {
                // 真正的执行 sql 语句
                statement.execute(sql);
                return null;
            }
        }
        execute(new ExecuteStatementCallback());
    }

    @Override
    public <T> T query(String sql, ResultSetExtractor<T> res) {

        class QueryStatementCallback implements StatementCallback<T>, SqlProvider {

            @Override
            public String getSql() {
                return sql;
            }

            @Override
            public T doInStatement(Statement statement) throws SQLException {
                ResultSet rs = statement.executeQuery(sql);
                // 执行 sql 语句，并对查询结果 rs 进行解析。
                return res.extractData(rs);
            }
        }

        return execute(new QueryStatementCallback());
    }

    @Override
    public <T> List<T> query(String sql, RowMapper<T> rowMapper) {
        return result(query(sql, new RowMapperResultSetExtractor<>(rowMapper)));
    }

    @Override
    public List<Map<String, Object>> queryForList(String sql) {
        return query(sql, new ColumnMapRowMapper());
    }

    private static <T> T result(T result) {
        Assert.state(null != result, "No result");
        return result;
    }

    protected void applyStateSettings(Statement stat) throws SQLException {
        int fetchSize = getFetchSize();
        if (fetchSize != -1) {
            stat.setFetchSize(fetchSize);
        }

        int maxRows = getMaxRows();
        if (maxRows != -1) {
            stat.setMaxRows(maxRows);
        }
    }
}
