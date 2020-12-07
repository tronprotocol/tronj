# tronj

The TRON client library.

Tronj document: https://tronjdocument.readthedocs.io/en/latest/

## How to use

### Gradle Setting

Add repo setting:

```groovy
repositories {
    maven {
        url  "https://dl.bintray.com/starsakary/tronj"
    }
}
```

Then add `abi` as dependency.

```groovy
dependencies {
    ....

    implementation 'org.tron.tronj:abi:0.1.0'
    implementation 'org.tron.tronj:client:0.1.0'

    ....
}
```

### Maven Settings

Use maven repo setting from [Bintray](https://bintray.com/beta/#/starsakary/tronj/abi/0.1.0?tab=overview).

```xml
<dependency>
  <groupId>org.tron.tronj</groupId>
  <artifactId>abi</artifactId>
  <version>0.1.0</version>
  <type>pom</type>
</dependency>
```

### Demo Code

Refer `demo` project.

```java
/*
import java.math.BigInteger;
import java.util.*;

import FunctionEncoder;
import org.tron.tronj.abi.datatypes.*;
import Bytes10;
import Uint256;
import Uint32;
*/


// Function(name, input, output)
Function function =
        new Function(
                "sam",
                Arrays.asList(
                        new DynamicBytes("dave".getBytes()),
                        new Bool(true),
                        new Address("T9yKC9LCoVvmhaFxKcdK9iL18TUWtyFtjh"),
                        new DynamicArray<>(
                                new Uint(BigInteger.ONE),
                                new Uint(BigInteger.valueOf(2)),
                                new Uint(BigInteger.valueOf(3)))),
                Collections.emptyList());
String encodedHex = FunctionEncoder.encode(function);

/*
465c405b
0000000000000000000000000000000000000000000000000000000000000080
0000000000000000000000000000000000000000000000000000000000000001
00000000000000000000000000052b08330e05d731e38c856c1043288f7d9744
00000000000000000000000000000000000000000000000000000000000000c0
0000000000000000000000000000000000000000000000000000000000000004
6461766500000000000000000000000000000000000000000000000000000000
0000000000000000000000000000000000000000000000000000000000000003
0000000000000000000000000000000000000000000000000000000000000001
0000000000000000000000000000000000000000000000000000000000000002
0000000000000000000000000000000000000000000000000000000000000003
*/
```
