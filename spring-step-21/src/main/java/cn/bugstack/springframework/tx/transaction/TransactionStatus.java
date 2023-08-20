package cn.bugstack.springframework.tx.transaction;

import java.io.Flushable;
import java.io.IOException;

public interface TransactionStatus extends TransactionExecution, SavepointManager, Flushable {

    boolean hasSavepoint();

    @Override
    void flush() throws IOException;

}
