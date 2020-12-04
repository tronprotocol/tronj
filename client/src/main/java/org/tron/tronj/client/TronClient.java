package org.tron.tronj.client;


/**
 * A {@code TronClient} object is the entry point for calling the functions.
 *
 *<p>A {@code TronClient} object is bind with a private key and a full node.
 * {@link #broadcastTransaction}, {@link #signTransaction} and other transaction related
 * operations can be done via a {@code TronClient} object.</p>
 *
 * @since jdk13.0.2+8
 * @see org.tron.tronj.client.contract.Contract
 * @see org.tron.tronj.proto.Chain.Transaction
 * @see org.tron.tronj.proto.Contract
 */


import org.tron.tronj.abi.FunctionEncoder;
import org.tron.tronj.abi.datatypes.Function;
import org.tron.tronj.api.GrpcAPI.BytesMessage;

import org.tron.tronj.api.WalletGrpc;
import org.tron.tronj.api.WalletSolidityGrpc;
import org.tron.tronj.client.contract.Contract;
import org.tron.tronj.client.contract.ContractFunction;
import org.tron.tronj.client.Transaction.TransactionBuilder;
import org.tron.tronj.crypto.SECP256K1;
import org.tron.tronj.proto.Chain.Transaction;

import org.tron.tronj.proto.Chain.Block;

import org.tron.tronj.proto.Common.SmartContract;

import org.tron.tronj.proto.Contract.TransferAssetContract;
import org.tron.tronj.proto.Contract.UnfreezeBalanceContract;
import org.tron.tronj.proto.Contract.FreezeBalanceContract;
import org.tron.tronj.proto.Contract.TransferContract;
import org.tron.tronj.proto.Contract.VoteWitnessContract;
import org.tron.tronj.proto.Contract.TriggerSmartContract;
import org.tron.tronj.proto.Response.TransactionExtention;
import org.tron.tronj.proto.Response.TransactionReturn;
import org.tron.tronj.proto.Response.NodeInfo;
import org.tron.tronj.proto.Response.WitnessList;
import org.tron.tronj.proto.Response.BlockExtention;
import org.tron.tronj.proto.Response.BlockListExtention;
import org.tron.tronj.api.GrpcAPI.NumberMessage;
import org.tron.tronj.api.GrpcAPI.EmptyMessage;
import org.tron.tronj.api.GrpcAPI.AccountAddressMessage;
import org.tron.tronj.utils.Base58Check;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.HashMap;

import org.tron.tronj.crypto.tuweniTypes.Bytes32;
import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.bouncycastle.jcajce.provider.digest.SHA256;
import org.bouncycastle.util.encoders.Hex;
import org.tron.tronj.proto.Response.NodeList;
import org.tron.tronj.proto.Response.TransactionInfoList;
import org.tron.tronj.proto.Response.TransactionInfo;
import org.tron.tronj.proto.Response.Account;

import static org.tron.tronj.proto.Response.TransactionReturn.response_code.SUCCESS;

import java.util.List;

public class TronClient {
    public final WalletGrpc.WalletBlockingStub blockingStub;
    public final WalletSolidityGrpc.WalletSolidityBlockingStub blockingStubSolidity;
    public final SECP256K1.KeyPair keyPair;

    public TronClient(String grpcEndpoint, String grpcEndpointSolidity, String hexPrivateKey) {
        ManagedChannel channel = ManagedChannelBuilder.forTarget(grpcEndpoint).usePlaintext().build();
        ManagedChannel channelSolidity = ManagedChannelBuilder.forTarget(grpcEndpointSolidity).usePlaintext().build();
        blockingStub = WalletGrpc.newBlockingStub(channel);
        blockingStubSolidity = WalletSolidityGrpc.newBlockingStub(channelSolidity);
        keyPair = SECP256K1.KeyPair.create(SECP256K1.PrivateKey.create(Bytes32.fromHexString(hexPrivateKey)));
    }

    /*public TronClient(Channel channel, String hexPrivateKey) {
        blockingStub = WalletGrpc.newBlockingStub(channel);
        blockingStubSolidity = WalletSolidityGrpc.newBlockingStub(channel);
        keyPair = SECP256K1.KeyPair.create(SECP256K1.PrivateKey.create(Bytes32.fromHexString(hexPrivateKey)));
    }*/

    /**
     * The constuctor for main net.
     * @param hexPrivateKey the binding private key. Operations require private key will all use this unless the private key is specified elsewhere.
     * @return a TronClient object
     */
    public static TronClient ofMainnet(String hexPrivateKey) {
        return new TronClient("grpc.trongrid.io:50051", "grpc.trongrid.io:50052",hexPrivateKey);
    }

    /**
     * The constuctor for Shasta test net.
     * @param hexPrivateKey the binding private key. Operations require private key will all use this unless the private key is specified elsewhere.
     * @return a TronClient object
     */
    public static TronClient ofShasta(String hexPrivateKey) {
        return new TronClient("grpc.shasta.trongrid.io:50051", "grpc.shasta.trongrid.io:50052", hexPrivateKey);
    }

    /**
     * The constuctor for Nile test net.
     * @param hexPrivateKey the binding private key. Operations require private key will all use this unless the private key is specified elsewhere.
     * @return a TronClient object
     */
    public static TronClient ofNile(String hexPrivateKey) {
        return new TronClient("47.252.19.181:50051", "47.252.19.181:50061", hexPrivateKey);
    }

    /**
     * Generate random address
     * @return Address in hex
     */
    public static String generateAddress() {
        // generate random address
        SECP256K1.KeyPair kp = SECP256K1.KeyPair.generate();

        SECP256K1.PublicKey pubKey = kp.getPublicKey();
        Keccak.Digest256 digest = new Keccak.Digest256();
        digest.update(pubKey.getEncoded(), 0, 64);
        byte[] raw = digest.digest();
        byte[] rawAddr = new byte[21];
        rawAddr[0] = 0x41;
        System.arraycopy(raw, 12, rawAddr, 1, 20);

        return Hex.toHexString(kp.getPrivateKey().getEncoded());
    }

    /**
     * The function receives addresses in any formats.
     * @param address account or contract address in any allowed formats.
     * @return hex address
     */
    public static ByteString parseAddress(String address) {
        byte[] raw;
        if (address.startsWith("T")) {
            raw = Base58Check.base58ToBytes(address);
        } else if (address.startsWith("41")) {
            raw = Hex.decode(address);
        } else if (address.startsWith("0x")) {
            raw = Hex.decode(address.substring(2));
        } else {
            try {
                raw = Hex.decode(address);
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid address: " + address);
            }
        }
        return ByteString.copyFrom(raw);
    }

    public static ByteString parseHex(String hexString) {
        byte[] raw = Hex.decode(hexString);
        return ByteString.copyFrom(raw);
    }

    public static String toHex(byte[] raw) {
        return Hex.toHexString(raw);
    }

    public static String toHex(ByteString raw) {
        return Hex.toHexString(raw.toByteArray());
    }

    public Transaction signTransaction(TransactionExtention txnExt, SECP256K1.KeyPair kp) {
        SECP256K1.Signature sig = SECP256K1.sign(Bytes32.wrap(txnExt.getTxid().toByteArray()), kp);
        Transaction signedTxn =
                txnExt.getTransaction().toBuilder().addSignature(ByteString.copyFrom(sig.encodedBytes().toArray())).build();
        return signedTxn;
    }

    public Transaction signTransaction(Transaction txn, SECP256K1.KeyPair kp) {
        SHA256.Digest digest = new SHA256.Digest();
        digest.update(txn.getRawData().toByteArray());
        byte[] txid = digest.digest();
        SECP256K1.Signature sig = SECP256K1.sign(Bytes32.wrap(txid), kp);
        Transaction signedTxn = txn.toBuilder().addSignature(ByteString.copyFrom(sig.encodedBytes().toArray())).build();
        return signedTxn;
    }

    /**
     * Resolve the result code from TransactionReturn objects.
     * @param code the result code.
     * @return the corresponding message.
     */
    private String resolveResultCode(int code) {
        String responseCode = "";
        switch (code) {
            case 0:
                responseCode = "SUCCESS";
            case 1:
                responseCode = "SIGERROR";
            case 2:
                responseCode = "CONTRACT_VALIDATE_ERROR";
            case 3:
                responseCode = "CONTRACT_EXE_ERROR";
            case 4:
                responseCode = "BANDWITH_ERROR";
            case 5:
                responseCode = "DUP_TRANSACTION_ERROR";
            case 6:
                responseCode = "TAPOS_ERROR";
            case 7:
                responseCode = "TOO_BIG_TRANSACTION_ERROR";
            case 8:
                responseCode = "TRANSACTION_EXPIRATION_ERROR";
            case 9:
                responseCode = "SERVER_BUSY";
            case 10:
                responseCode = "NO_CONNECTION";
            case 11:
                responseCode = "NOT_ENOUGH_EFFECTIVE_CONNECTION";
            case 20:
                responseCode = "OTHER_ERROR";
        }
        return responseCode;
    }

    /**
     * broadcast a transaction with the binding account.
     * @param Transaction a signed transaction ready to be broadcasted
     * @return a TransactionReturn object contains the broadcasting result
     * @throws RuntimeException if broadcastin fails
     */
    public TransactionReturn broadcastTransaction(Transaction txn) throws RuntimeException{
        TransactionReturn ret = blockingStub.broadcastTransaction(txn);
        if (!ret.getResult()) {
            String message = resolveResultCode(ret.getCodeValue()) + ", " + ret.getMessage();
            throw new RuntimeException(message);
        } else {
            return ret;
        }
    }

    public Transaction signTransaction(TransactionExtention txnExt) {
        return signTransaction(txnExt, keyPair);
    }

    public Transaction signTransaction(Transaction txn) {
        return signTransaction(txn, keyPair);
    }

    /**
     * Transfer TRX. amount in SUN
     * @param fromAddress owner address
     * @param toAddress receive balance
     * @param amount transfer amount
     * @return TransactionExtention
     * @throws IllegalException if fail to transfer
     */
    public TransactionExtention transfer(String fromAddress, String toAddress, long amount) throws IllegalException {

        ByteString rawFrom = parseAddress(fromAddress);
        ByteString rawTo = parseAddress(toAddress);

        TransferContract req = TransferContract.newBuilder()
                .setOwnerAddress(rawFrom)
                .setToAddress(rawTo)
                .setAmount(amount)
                .build();
        TransactionExtention txnExt = blockingStub.createTransaction2(req);

        if(SUCCESS != txnExt.getResult().getCode()){
            throw new IllegalException(txnExt.getResult().getMessage().toStringUtf8());
        }

        return txnExt;
    }

    /**
     * Transfers TRC10 Asset
     * @param fromAddress owner address
     * @param toAddress receive balance
     * @param tokenId asset name
     * @param amount transfer amount
     * @return TransactionExtention
     * @throws IllegalException if fail to transfer trc10
     */
    public TransactionExtention transferTrc10(String fromAddress, String toAddress, int tokenId, long amount) throws IllegalException {

        ByteString rawFrom = parseAddress(fromAddress);
        ByteString rawTo = parseAddress(toAddress);
        byte[] rawTokenId = Integer.toString(tokenId).getBytes();

        TransferAssetContract req = TransferAssetContract.newBuilder()
                .setOwnerAddress(rawFrom)
                .setToAddress(rawTo)
                .setAssetName(ByteString.copyFrom(rawTokenId))
                .setAmount(amount)
                .build();

        TransactionExtention txnExt = blockingStub.transferAsset2(req);

        if(SUCCESS != txnExt.getResult().getCode()){
            throw new IllegalException(txnExt.getResult().getMessage().toStringUtf8());
        }

        return txnExt;
    }

    /**
     * Freeze balance to get energy or bandwidth, for 3 days
     * @param ownerAddress owner address
     * @param frozenBalance frozen balance
     * @param frozenDuration frozen duration
     * @param resourceCode Resource type, can be 0("BANDWIDTH") or 1("ENERGY")
     * @return TransactionExtention
     * @throws IllegalException if fail to freeze balance
     */
    public TransactionExtention freezeBalance(String ownerAddress, long frozenBalance, long frozenDuration, int resourceCode) throws IllegalException {

        ByteString rawFrom = parseAddress(ownerAddress);
        FreezeBalanceContract freezeBalanceContract=
                FreezeBalanceContract.newBuilder()
                        .setOwnerAddress(rawFrom)
                        .setFrozenBalance(frozenBalance)
                        .setFrozenDuration(frozenDuration)
                        .setResourceValue(resourceCode)
                        .build();
        TransactionExtention txnExt = blockingStub.freezeBalance2(freezeBalanceContract);

        if(SUCCESS != txnExt.getResult().getCode()){
            throw new IllegalException(txnExt.getResult().getMessage().toStringUtf8());
        }

        return txnExt;
    }

    /**
     * Unfreeze balance to get TRX back
     * @param ownerAddress owner address
     * @param resourceCode Resource type, can be 0("BANDWIDTH") or 1("ENERGY")
     * @return TransactionExtention
     * @throws IllegalException if fail to unfreeze balance
     */
    public TransactionExtention unfreezeBalance(String ownerAddress, int resourceCode) throws IllegalException {

        UnfreezeBalanceContract unfreeze =
                UnfreezeBalanceContract.newBuilder()
                        .setOwnerAddress(parseAddress(ownerAddress))
                        .setResourceValue(resourceCode)
                        .build();

        TransactionExtention txnExt = blockingStub.unfreezeBalance2(unfreeze);

        if(SUCCESS != txnExt.getResult().getCode()){
            throw new IllegalException(txnExt.getResult().getMessage().toStringUtf8());
        }

        return txnExt;
    }

    /**
     * Vote for witnesses
     * @param ownerAddress owner address
     * @param votes <vote address, vote count>
     * @return TransactionExtention
     * IllegalNumException if fail to vote witness
     */
    public TransactionExtention voteWitness(String ownerAddress, HashMap<String, String> votes) throws IllegalException {
        ByteString rawFrom = parseAddress(ownerAddress);
        VoteWitnessContract voteWitnessContract = createVoteWitnessContract(rawFrom, votes);
        TransactionExtention txnExt = blockingStub.voteWitnessAccount2(voteWitnessContract);

        if(SUCCESS != txnExt.getResult().getCode()){
            throw new IllegalException(txnExt.getResult().getMessage().toStringUtf8());
        }

        return txnExt;
    }

    /**
     * Query the latest block information
     * @return Block
     * @throws IllegalException if fail to get now block
     */
    public Block getNowBlock() throws IllegalException {
        Block block = blockingStub.getNowBlock(EmptyMessage.newBuilder().build());
        if(!block.hasBlockHeader()){
            throw new IllegalException("Fail to get latest block.");
        }
        return block;
    }

    /**
     * Returns the Block Object corresponding to the 'Block Height' specified (number of blocks preceding it)
     * @param blockNum The block height
     * @return Block
     * @throws IllegalException if the parameters are not correct
     */
    public Block getBlockByNum(long blockNum) throws IllegalException {
        NumberMessage.Builder builder = NumberMessage.newBuilder();
        builder.setNum(blockNum);
        Block block = blockingStub.getBlockByNum(builder.build());

        if(!block.hasBlockHeader()){
            throw new IllegalException();
        }
        return block;
    }

    /**
     * Get some latest blocks
     * @param num Number of latest blocks
     * @return BlockListExtention
     * @throws IllegalException if the parameters are not correct
     */
    public BlockListExtention getBlockByLatestNum(long num) throws IllegalException {
        NumberMessage numberMessage = NumberMessage.newBuilder().setNum(num).build();
        BlockListExtention blockListExtention = blockingStub.getBlockByLatestNum2(numberMessage);

        if(blockListExtention.getBlockCount() == 0){
            throw new IllegalException();
        }
        return blockListExtention;
    }

    /**
     * Get current API node’ info
     * @return NodeInfo
     * @throws IllegalException if fail to get nodeInfo
     */
    public NodeInfo getNodeInfo() throws IllegalException {
        NodeInfo nodeInfo = blockingStub.getNodeInfo(EmptyMessage.newBuilder().build());

        if(nodeInfo.getBlock().isEmpty()){
            throw new IllegalException("Fail to get node info.");
        }
        return nodeInfo;
    }

    /**
     * List all nodes that current API node is connected to
     * @return NodeList
     * @throws IllegalException if fail to get node list
     */
    public NodeList listNodes() throws IllegalException {
        NodeList nodeList = blockingStub.listNodes(EmptyMessage.newBuilder().build());

        if(nodeList.getNodesCount() == 0){
            throw new IllegalException("Fail to get node list.");
        }
        return nodeList;
    }

    /**
     * Get transactionInfo from block number
     * @param blockNum The block height
     * @return TransactionInfoList
     * @throws IllegalException no transactions or the blockNum is incorrect
     */
    public TransactionInfoList getTransactionInfoByBlockNum(long blockNum) throws IllegalException {
        NumberMessage numberMessage = NumberMessage.newBuilder().setNum(blockNum).build();
        TransactionInfoList transactionInfoList = blockingStub.getTransactionInfoByBlockNum(numberMessage);
        if(transactionInfoList.getTransactionInfoCount() == 0){
            throw new IllegalException("no transactions or the blockNum is incorrect.");
        }

        return transactionInfoList;
    }

    /**
     * Query the transaction fee, block height by transaction id
     * @param txID Transaction hash, i.e. transaction id
     * @return TransactionInfo
     * @throws IllegalException if the parameters are not correct
     */
    public TransactionInfo getTransactionInfoById(String txID) throws IllegalException {
        ByteString bsTxid = parseAddress(txID);
        BytesMessage request = BytesMessage.newBuilder()
                .setValue(bsTxid)
                .build();
        TransactionInfo transactionInfo = blockingStub.getTransactionInfoById(request);

        if(transactionInfo.getBlockTimeStamp() == 0){
            throw new IllegalException();
        }
        return transactionInfo;
    }

    /**
     * Get account info by address
     * @param address address, default hexString
     * @return Account
     */
    public Account getAccount(String address) {
        ByteString bsAddress = parseAddress(address);
        AccountAddressMessage accountAddressMessage = AccountAddressMessage.newBuilder()
                .setAddress(bsAddress)
                .build();
        Account account = blockingStub.getAccount(accountAddressMessage);
        return account;
    }

    /**
     * List all witnesses that current API node is connected to
     * @return WitnessList
     */
    public WitnessList listWitnesses() {
        WitnessList witnessList = blockingStub
                .listWitnesses(EmptyMessage.newBuilder().build());
        return witnessList;
    }

    //All other solidified APIs start

    /**
     * Get solid account info by address
     * @param address address, default hexString
     * @return Account
     */
    public Account getAccountSolidity(String address) {
        ByteString bsAddress = parseAddress(address);
        AccountAddressMessage accountAddressMessage = AccountAddressMessage.newBuilder()
                .setAddress(bsAddress)
                .build();
        Account account = blockingStubSolidity.getAccount(accountAddressMessage);
        return account;
    }

    /**
     * Query the latest solid block information
     * @return BlockExtention
     * @throws IllegalException if fail to get now block
     */
    public BlockExtention getNowBlockSolidity() throws IllegalException {
        BlockExtention blockExtention = blockingStubSolidity.getNowBlock2(EmptyMessage.newBuilder().build());

        if(!blockExtention.hasBlockHeader()){
            throw new IllegalException("Fail to get latest block.");
        }
        return blockExtention;
    }

    /**
     * Get transaction receipt info from a transaction id, must be in solid block
     * @param txID Transaction hash, i.e. transaction id
     * @return Transaction
     * @throws IllegalException if the parameters are not correct
     */
    public Transaction getTransactionByIdSolidity(String txID) throws IllegalException {
        ByteString bsTxid = parseAddress(txID);
        BytesMessage request = BytesMessage.newBuilder()
                .setValue(bsTxid)
                .build();
        Transaction transaction = blockingStubSolidity.getTransactionById(request);

        if(transaction.getRetCount() == 0) {
            throw new IllegalException();
        }
        return transaction;
    }

    /**
     * Get the rewards that the voter has not received
     * @param address address, default hexString
     * @return NumberMessage
     */
    public NumberMessage getRewardSolidity(String address) throws IllegalException {
        ByteString bsAddress = parseAddress(address);
        BytesMessage bytesMessage = BytesMessage.newBuilder()
                .setValue(bsAddress)
                .build();
        NumberMessage numberMessage = blockingStubSolidity.getRewardInfo(bytesMessage);
        return numberMessage;
    }
    //All other solidified APIs end

    public static VoteWitnessContract createVoteWitnessContract(ByteString ownerAddress,
                                                                HashMap<String, String> votes) {
        VoteWitnessContract.Builder builder = VoteWitnessContract.newBuilder();
        builder.setOwnerAddress(ownerAddress);
        for (String addressBase58 : votes.keySet()) {
            String voteCount = votes.get(addressBase58);
            long count = Long.parseLong(voteCount);
            VoteWitnessContract.Vote.Builder voteBuilder = VoteWitnessContract.Vote.newBuilder();
            ByteString voteAddress = parseAddress(addressBase58);
            if (voteAddress == null) {
                continue;
            }
            voteBuilder.setVoteAddress(voteAddress);
            voteBuilder.setVoteCount(count);
            builder.addVotes(voteBuilder.build());
        }
        return builder.build();
    }


    /*public void transferTrc20(String from, String to, String cntr, long feeLimit, long amount, int precision) {
        System.out.println("============ TRC20 transfer =============");

        // transfer(address _to,uint256 _amount) returns (bool)
        // _to = TVjsyZ7fYF3qLF6BQgPmTEZy1xrNNyVAAA
        // _amount = 10 * 10^18
        Function trc20Transfer = new Function("transfer",
            Arrays.asList(new Address(to),
                new Uint256(BigInteger.valueOf(amount).multiply(BigInteger.valueOf(10).pow(precision)))),
            Arrays.asList(new TypeReference<Bool>() {}));

        String encodedHex = FunctionEncoder.encode(trc20Transfer);
        TriggerSmartContract trigger =
            TriggerSmartContract.newBuilder()
                .setOwnerAddress(TronClient.parseAddress(from))
                .setContractAddress(TronClient.parseAddress(cntr)) // JST
                .setData(TronClient.parseHex(encodedHex))
                .build();

        System.out.println("trigger:\n" + trigger);

        TransactionExtention txnExt = blockingStub.triggerContract(trigger);
        System.out.println("txn id => " + TronClient.toHex(txnExt.getTxid().toByteArray()));
        System.out.println("contsant result :" + txnExt.getConstantResult(0));

        Transaction unsignedTxn = txnExt.getTransaction().toBuilder()
            .setRawData(txnExt.getTransaction().getRawData().toBuilder().setFeeLimit(feeLimit))
            .build();

        Transaction signedTxn = signTransaction(unsignedTxn);

        System.out.println(signedTxn.toString());
        TransactionReturn ret = blockingStub.broadcastTransaction(signedTxn);
        System.out.println("======== Result ========\n" + ret.toString());
    }*/

    /**
     * Obtain a {@code Contract} object via an address
     * @param contractAddress smart contract address
     * @return the smart contract obtained from the address
     * @throws Exception if contract address does not match
     */
    public Contract getContract(String contractAddress) {
        ByteString rawAddr = parseAddress(contractAddress);
        BytesMessage param =
            BytesMessage.newBuilder()
            .setValue(rawAddr)
            .build();

            SmartContract cntr = blockingStub.getContract(param);

            Contract contract =
                new Contract.Builder()
                .setCntrAddr(cntr.getContractAddress())
                .setBytecode(cntr.getBytecode())
                .setName(cntr.getName())
                .setAbi(cntr.getAbi())
                .setOriginEnergyLimit(cntr.getOriginEnergyLimit())
                .setConsumeUserResourcePercent(cntr.getConsumeUserResourcePercent())
                .build();

        return contract;
    }

    /**
     * Check whether a given method is in the contract.
     * @param cntr the smart contract.
     * @param function the smart contract function.
     * @return ture if function exists in the contract.
     */
    private boolean isFuncInContract(Contract cntr, Function function) {
        List<ContractFunction> functions = cntr.getFunctions();
        for (int i = 0; i < functions.size(); i++) {
            if (functions.get(i).getName().equalsIgnoreCase(function.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * call function without signature and broadcasting
     * @param ownerAddr the caller
     * @param cntr the contract
     * @param function the function called
     * @return TransactionExtention
     */
    private TransactionExtention callWithoutBroadcast(String ownerAddr, Contract cntr, Function function) {
        cntr.setOwnerAddr(parseAddress(ownerAddr));
            String encodedHex = FunctionEncoder.encode(function);
            // Make a TriggerSmartContract contract
            TriggerSmartContract trigger =
                TriggerSmartContract.newBuilder()
                .setOwnerAddress(cntr.getOwnerAddr())
                .setContractAddress(cntr.getCntrAddr())
                .setData(parseHex(encodedHex))
                .build();

            // System.out.println("trigger:\n" + trigger);

            TransactionExtention txnExt = blockingStub.triggerConstantContract(trigger);
            // System.out.println("txn id => " + toHex(txnExt.getTxid().toByteArray()));

            return txnExt;
    }

    /**
     * make a constant call - no broadcasting
     * @param ownerAddr the current caller.
     * @param contractAddr smart contract address.
     * @param function contract function.
     * @return TransactionExtention.
     * @throws RuntimeException if function cannot be found in the contract.
     */
    public TransactionExtention constantCall(String ownerAddr, String contractAddr, Function function) throws Exception{
        Contract cntr = getContract(contractAddr);
        if (isFuncInContract(cntr, function)) {
            return callWithoutBroadcast(ownerAddr, cntr, function);
        } else {
            throw new RuntimeException("Function not found in the contract");
        }
    }

    /**
     * make a trigger call. Trigger call consumes energy and bandwidth.
     * @param ownerAddr the current caller
     * @param contractAddr smart contract address
     * @param function contract function
     * @return transaction builder. Users may set other fields, e.g. feeLimit
     * @throws RuntimeException if function cannot be found in the contract
     */
    public TransactionBuilder triggerCall(String ownerAddr, String contractAddr, Function function) throws Exception {
        Contract cntr = getContract(contractAddr);
        if (isFuncInContract(cntr, function)) {
            TransactionExtention txnExt = callWithoutBroadcast(ownerAddr, cntr, function);
            return new TransactionBuilder(txnExt.getTransaction());
        } else {
            throw new RuntimeException("Function not found in the contract");
        }
    }
    
}
