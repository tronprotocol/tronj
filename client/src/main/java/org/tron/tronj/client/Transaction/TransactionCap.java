package org.tron.tronj.client.transaction;

/**
 * The {@code TransactionCap} class contains a {@code org.tron.tronj.proto.Transaction}
 * instance and the corresponding transaction id.
 */

import org.tron.tronj.proto.Chain.Transaction;
import org.tron.tronj.proto.Response.TransactionExtention;

public class TransactionCap {
    private Transaction transaction;
    private byte[] transactionId;

    public TransactionCap(Transaction transaction, byte[] transactionId) {
        this.transaction = transaction;
        this.transactionId = transactionId;
    }

    public TransactionCap(TransactionExtention txnExt) {
        this.transaction = txnExt.getTransaction();
        this.transactionId = txnExt.getTxid().toByteArray();
    }

    public TransactionCap(Transaction transaction, ByteString transactionId) {
        this.transaction = transaction;
        this.transactionId = transactionId.toByteArray();
    }

    public TransactionCap(Builder builder) {
        this.transaction = builder.transaction;
        this.transactionId = builder.transactionId;
    }

    public Transaction getTransaction() {
        return this.transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public byte[] getTransactionId() {
        return this.transactionId;
    }

    public void setTransacionId(byte[] transactionId) {
        this.transactionId = transactionId;
    }

    public static class Builder {
        private Transaction transaction;
        private byte[] transactionId;

        public Builder setTransaction(Transaction transaction) {
            this.transaction = transaction;
            return this;
        }

        public Builder setTransacionId(byte[] transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public TransactionCap build() {
            return new TransactionCap(this);
        }
    }
}