package org.tron.tronj.client.Transaction;

/**
 * The {@code TransactionBuilder} class provides mutator methods
 * for common used attributes.
 *
 * <p>The {@code TransactionBuilder} object are mostly used before signing a
 * transaction, for setting attributes values like {@link #setFeeLimit}, {@link
 * #setMemo}, Etc.</p>
 *
 * @since jdk13.0.2+8
 * @see org.tron.tronj.proto.Chain.Transaction;
 */

import com.google.protobuf.ByteString;
import org.tron.tronj.proto.Chain.Transaction;

public class TransactionBuilder {
    private Transaction transaction;

    public TransactionBuilder(Transaction transaction) {
        this.transaction = transaction;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public TransactionBuilder setFeeLimit(long feeLimit) {
        transaction.toBuilder()
            .setRawData(transaction.getRawData().toBuilder().setFeeLimit(feeLimit))
            .build();
        return this;
    } 

    public TransactionBuilder setPermissionId(int permissionId) {
        transaction.getRawData().toBuilder()
            .setContract(0, transaction.getRawData().getContract(0).toBuilder().setPermissionId(permissionId))
            .build();
        return this;
    }

    public TransactionBuilder setMemo(byte[] memo) {
        transaction.toBuilder()
            .setRawData(transaction.getRawData().toBuilder().setData(ByteString.copyFrom(memo)))
            .build();
        return this;
    }

    public TransactionBuilder setMemo(String memo) {
        transaction.toBuilder()
            .setRawData(transaction.getRawData().toBuilder().setData(ByteString.copyFromUtf8(memo)))
            .build();
        return this;
    }

    public Transaction build() {
        return this.transaction;
    }
}