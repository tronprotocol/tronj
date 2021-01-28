package demo.smartContract;

/**
 * The {@code SmartContractDemo} class includes demos of common 
 * smart contract related operations.
 */

import org.tron.tronj.abi.datatypes.Function;
import org.tron.tronj.abi.datatypes.Utf8String;
import org.tron.tronj.abi.TypeReference;
import org.tron.tronj.abi.FunctionEncoder;
import org.tron.tronj.abi.FunctionReturnDecoder;
import org.tron.tronj.client.contract.Contract;
import org.tron.tronj.client.contract.ContractFunction;
import org.tron.tronj.client.transaction.TransactionBuilder;
import org.tron.tronj.client.TronClient;
import org.tron.tronj.proto.Chain.Transaction;
import org.tron.tronj.proto.Common.SmartContract.ABI;
import org.tron.tronj.proto.Response.TransactionExtention;
import org.tron.tronj.proto.Response.TransactionReturn;
import org.tron.tronj.utils.Base58Check;
import org.tron.tronj.utils.Numeric;

import com.google.protobuf.ByteString;

import java.util.Arrays;
import java.util.Collections;

public class SmartContractDemo {
    private final TronClient client = TronClient.ofNile("7c3a547f37cf82c7bd7cead99e53fd9a7d3bf6a3c8bd3c8541ad572322d16e42");

    /**
     * deploy a smart contract with its bytecode and abi.
     * 
     * Except from the contract's abi and bytecode, other attributes
     * of a contract have default values. You may choose to set values to 
     * any other attributes base on needs. The optional attributes are commented out
     * in this demo, refer to {@link org.tron.tronj.client.contract.Contract}.
     * 
     * Notice that call_token_value is the amount of TRC-10 token to be deposited.
     * 
     * The demo contract is:
     * 
     * pragma solidity >=0.4.16 <0.8.0;
     * 
     * contract SimpleStorage {
     *     uint storedData;
     *     function set(uint x) public {
     *         storedData = x;
     *     }
     *     function get() public view returns (uint) {
     *         return storedData;
     *     }
     * }
     * 
     * @throws RuntimeException if deployment duplicating / owner and origin address don't match
     * @throws InvalidProtocolBufferException if the input is not valid JSON format or there are unknown fields in the input
     */
    public void deploySmartContract() {
        try {
            String bytecode = "608060405234801561001057600080fd5b50d380156" +
                           "1001d57600080fd5b50d2801561002a57600080fd5" +
                           "b5060dd806100396000396000f3fe6080604052348" +
                           "015600f57600080fd5b50d38015601b57600080fd5" +
                           "b50d28015602757600080fd5b5060043610604a576" +
                           "0003560e01c806360fe47b114604f5780636d4ce63" +
                           "c14607a575b600080fd5b607860048036036020811" +
                           "015606357600080fd5b81019080803590602001909" +
                           "291905050506096565b005b608060a0565b6040518" +
                           "082815260200191505060405180910390f35b80600" +
                           "08190555050565b6000805490509056fea26474726" +
                           "f6e58206304a660fbed31eff4077ef91f51652f50c" +
                           "642cdd52d505857b0b3f17e51e1a864736f6c634300050e0031";

            String abi = "{\n" +
                "\t\"entrys\": [{\n" +
                "\t\t\"inputs\": [],\n" +
                "\t\t\"name\": \"get\",\n" +
                "\t\t\"outputs\": [{\n" +
                "\t\t\t\"internalType\": \"uint256\",\n" +
                "\t\t\t\"name\": \"\",\n" +
                "\t\t\t\"type\": \"uint256\"\n" +
                "\t\t}],\n" +
                "\t\t\"stateMutability\": \"view\",\n" +
                "\t\t\"type\": \"function\"\n" +
                "\t}, {\n" +
                "\t\t\"inputs\": [{\n" +
                "\t\t\t\"internalType\": \"uint256\",\n" +
                "\t\t\t\"name\": \"x\",\n" +
                "\t\t\t\"type\": \"uint256\"\n" +
                "\t\t}],\n" +
                "\t\t\"name\": \"set\",\n" +
                "\t\t\"outputs\": [],\n" +
                "\t\t\"stateMutability\": \"nonpayable\",\n" +
                "\t\t\"type\": \"function\"\n" +
                "\t}]\n" +
                "}";

            Contract cntr = new Contract.Builder()
                                    .setClient(client)
                                    .setOwnerAddr(client.parseAddress("TFRgpvvNTe8bwC666D6orYhEkCcYsbax8U"))
                                    .setOriginAddr(client.parseAddress("TFRgpvvNTe8bwC666D6orYhEkCcYsbax8U"))
                                    .setBytecode(ByteString.copyFrom(Numeric.hexStringToByteArray(bytecode)))
                                    .setAbi(abi)
                                    // .setCallValue()
                                    // .setName()
                                    // .setConsumeUserResourcePercent()
                                    // .setOriginEnergyLimit()
                                    .build();
        
            TransactionBuilder builder = cntr.deploy();
            //use the following method with parameters to call if has any TRC-10 deposit
            //TransactionBuilder builder = cntr.deploy(tokenId, callTokenValue);
            builder.setFeeLimit(1000000000L);
            builder.setMemo("Let's go!");
            //sign transaction
            Transaction signedTxn = client.signTransaction(builder.build());
            System.out.println(signedTxn.toString());
            //broadcast transaction
            TransactionReturn ret = client.broadcastTransaction(signedTxn);
            System.out.println("======== Result ========\n" + ret.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * get a smart contract from its address, return a org.tron.tronj.client.contract.Contract object.
     */
    public void getSmartContract() {
        try {
            Contract cntr = client.getContract("THi2qJf6XmvTJSpZHc17HgQsmJop6kb3ia");
            System.out.println("Contract name: " + cntr.getName());
            // System.out.println("Contract ABI: " + cntr.getAbi());
            System.out.println("Contract functions: " + cntr.getFunctions().size());
            for (ContractFunction cf : cntr.getFunctions()) {
                System.out.println(cf.toString());
            }
        } catch (Exception e) {
            System.out.println("error: " + e);
        }
    }

    /**
     * This is a constant call demo.
     * <p>the example function is: function name() public view returns (string).
     * A constant call does not require signature or broadcasting. This demos shows the stpes 
     * to  </p>
     * 
     * 
     */
    public void constantCallDemo() {
        Function name = new Function("name",
                Collections.emptyList(), Arrays.asList(new TypeReference<Utf8String>() {}));

        TransactionExtention txnExt = client.constantCall("TVjsyZ7fYF3qLF6BQgPmTEZy1xrNNyVAAA", 
                "TF17BgPaZYbz8oxbjhriubPDsA7ArKoLX3", name);
        //Convert constant result to human readable text
        String result = Numeric.toHexString(txnExt.getConstantResult(0).toByteArray());
        System.out.println((String)FunctionReturnDecoder.decode(result, name.getOutputParameters()).get(0).getValue());
    }

}