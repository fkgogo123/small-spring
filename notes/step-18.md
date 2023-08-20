实现功能：整合JDBC功能。

在xml中配置datasource数据源，并注入到jdbcTemplate中。
容器启动后，用户获取到jdbcTemplate实例对象即可执行相应的sql语句。


用户调用方法执行逻辑：
所有的用户调用的execute、query、queryForList方法，
最终调用了封装在StatementCallback接口中的doInStatement方法，
该方法中完成了 真正的sql语句执行 和 结果集解析。


结果集解析：
RowMapperResultSetExtractor用于解析结果集， ColumnMapRowMapper用于解析结果列。
while (rs.next()) { // 解析每一行
results.add(this.rowMapper.mapRow(rs, rowNum++)); // 解析每一列
}