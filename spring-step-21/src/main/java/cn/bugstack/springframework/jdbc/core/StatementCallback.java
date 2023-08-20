package cn.bugstack.springframework.jdbc.core;

import java.sql.SQLException;
import java.sql.Statement;

public interface StatementCallback<T> {

    /**
     * 真正的执行方法调用: 包含execute方法调用和结果集解析
     *
     * @param statement
     * @return
     * @throws SQLException
     */
    T doInStatement(Statement statement) throws SQLException;
}
