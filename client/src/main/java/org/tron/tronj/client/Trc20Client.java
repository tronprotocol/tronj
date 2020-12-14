package org.tron.tronj.client;

/**
 * The {@code Trc20Client} class wraps the TRC-20 standard function calls defined in TIP-20.
 * 
 * <p> The {@code Trc20Client} class includes already written smart contract functions. You may call 
 * these functions simply by the methods inside the {@code TronClient} class.</p>
 * 
 * @since jdk13.0.2+8
 * @see org.tron.tronj.client.TronClient
 * @see org.tron.tronj.abi.datatypes.Function
 */

import org.tron.tronj.abi.datatypes.Address;
import org.tron.tronj.abi.datatypes.Function;
import org.tron.tronj.abi.datatypes.generated.Uint8;
import org.tron.tronj.abi.datatypes.generated.Uint256;
import org.tron.tronj.abi.datatypes.Utf8String;
import org.tron.tronj.abi.TypeReference;
import org.tron.tronj.client.TronClient;
import org.tron.tronj.proto.Response.TransactionExtention;

import java.util.Arrays;
import java.util.Collections;

 public class Trc20Client {
     private TronClient client;

     public Trc20Client(String grpcEndpoint, String grpcEndpointSolidity, String hexPrivateKey) {
         client = new TronClient(grpcEndpoint, grpcEndpointSolidity, hexPrivateKey);
     }

     public static Trc20Client ofMainnet(String hexPrivateKey) {
         return new Trc20Client("grpc.trongrid.io:50051", "grpc.trongrid.io:50052",hexPrivateKey);
     }

     public static  Trc20Client ofShasta(String hexPrivateKey) {
         return new Trc20Client("grpc.shasta.trongrid.io:50051", "grpc.shasta.trongrid.io:50052", hexPrivateKey);
     }

     public static Trc20Client ofNile(String hexPrivateKey) {
        return new Trc20Client("47.252.19.181:50051", "47.252.19.181:50061", hexPrivateKey);
     }

     /**
      * call function name() public view returns (string)
      * @return A TransactionExtention object contains constant result to resolve
      */
     public TransactionExtention getName(String callerAddr, String cntrAddr) throws Exception{
        Function name = new Function("name",
                Collections.emptyList(), Arrays.asList(new TypeReference<Utf8String>() {}));

        TransactionExtention txnExt = client.constantCall(callerAddr, cntrAddr, name);
        return txnExt;
     }

     /**
      * call function symbol() public view returns (string)
      * @return A TransactionExtention object contains constant result to resolve
      */
      public TransactionExtention getSymbol(String callerAddr, String cntrAddr) throws Exception {
        Function symbol = new Function("symbol",
                Collections.emptyList(), Arrays.asList(new TypeReference<Utf8String>() {}));

        TransactionExtention txnExt = client.constantCall(callerAddr, cntrAddr, symbol);
        return txnExt;
      }

      /**
       * call function decimals() public view returns (uint8)
       * @return A TransactionExtention object contains constant result to resolve
       */
      public TransactionExtention getDecimals(String callerAddr, String cntrAddr) throws Exception {
        Function getName = new Function("decimals",
                Collections.emptyList(), Arrays.asList(new TypeReference<Uint8>() {}));
        
        TransactionExtention txnExt = client.constantCall(callerAddr, cntrAddr, getName);
        return txnExt;
      }

      /**
       * call function totalSupply() public view returns (uint256)
       * @return A TransactionExtention object contains constant result to resolve
       */
      public TransactionExtention getTotalSupply(String callerAddr, String cntrAddr) throws Exception {
        Function getName = new Function("totalSupply",
                Collections.emptyList(), Arrays.asList(new TypeReference<Uint256>() {}));

        TransactionExtention txnExt = client.constantCall(callerAddr, cntrAddr, getName);
        return txnExt;
      }

      /**
       * call function balanceOf(address _owner) public view returns (uint256 balance)
       * @return A TransactionExtention object contains constant result to resolve
       */
      public TransactionExtention getBalanceOf(String ownerAddr, String callerAddr, String cntrAddr) throws Exception {
        Function getName = new Function("totalSupply",
                Arrays.asList(new Address(ownerAddr)), Arrays.asList(new TypeReference<Uint256>() {}));

        TransactionExtention txnExt = client.constantCall(callerAddr, cntrAddr, getName);
        return txnExt;
      }
 }