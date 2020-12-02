package org.tron.tronj.abi.datatypes.generated;

import java.util.List;
import org.tron.tronj.abi.datatypes.StaticArray;
import org.tron.tronj.abi.datatypes.Type;

/**
 * Auto generated code.
 * <p><strong>Do not modifiy!</strong>
 * <p>Please use org.tron.tronj.codegen.AbiTypesGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 */
public class StaticArray30<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray30(List<T> values) {
        super(30, values);
    }

    @Deprecated
    @SafeVarargs
    public StaticArray30(T... values) {
        super(30, values);
    }

    public StaticArray30(Class<T> type, List<T> values) {
        super(type, 30, values);
    }

    @SafeVarargs
    public StaticArray30(Class<T> type, T... values) {
        super(type, 30, values);
    }
}
