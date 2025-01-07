# ApbBundle Module

## Overview

The `ApbBundle` module is a Chisel-based implementation of the Advanced Peripheral Bus (APB) interface, designed to simplify the creation and management of APB-compliant hardware designs.

## Features

- **APB Signal Definition**: Easily define the standard APB signals, including `PSEL`, `PENABLE`, `PWRITE`, `PADDR`, `PWDATA`, `PRDATA`, `PREADY`, and `PSLVERR`.
- **Configurable Data and Address Widths**: Supports configurable data and address widths through the `ApbParams` case class.
- **Slave Perspective**: Designed from the perspective of an APB slave, making it ideal for peripheral implementations.
- **Parameter Validation**: Ensures that data and address widths are valid through compile-time checks.

## Usage

### Defining APB Parameters

To define the parameters for the APB interface, use the `ApbParams` case class. This class allows you to specify the data width (`PDATA_WIDTH`) and address width (`PADDR_WIDTH`).

```scala
val apbParams = ApbParams(PDATA_WIDTH = 32, PADDR_WIDTH = 32)
```

### Creating an APB Bundle

To create an APB bundle, instantiate the `ApbBundle` class with the desired parameters. This will generate the necessary signals for APB communication.

```scala
val apbBundle = new ApbBundle(apbParams)
```

### Integrating with APB Slaves

The `ApbBundle` can be integrated with APB slaves by connecting the signals to the appropriate logic in your design. Below is an example of how to connect the `ApbBundle` to a simple APB slave.

```scala
class SimpleApbSlave(p: ApbParams) extends Module {
  val io = IO(new Bundle {
    val apb = new ApbBundle(p)
  })

  val reg = RegInit(0.U(p.PDATA_WIDTH.W))

  // Handle APB write transactions
  when(io.apb.PSEL && io.apb.PENABLE && io.apb.PWRITE) {
    reg := io.apb.PWDATA
  }

  // Handle APB read transactions
  io.apb.PRDATA := reg
  io.apb.PREADY := true.B
  io.apb.PSLVERR := false.B
}
```

### Example: APB Slave with Register

The following example demonstrates how to use the `ApbBundle` in a simple APB slave that contains a single register.

```scala
class ApbRegisterSlave(p: ApbParams) extends Module {
  val io = IO(new Bundle {
    val apb = new ApbBundle(p)
  })

  val register = RegInit(0.U(p.PDATA_WIDTH.W))

  // APB write logic
  when(io.apb.PSEL && io.apb.PENABLE && io.apb.PWRITE) {
    register := io.apb.PWDATA
  }

  // APB read logic
  io.apb.PRDATA := register
  io.apb.PREADY := true.B
  io.apb.PSLVERR := false.B
}
```

## Conclusion

The `ApbBundle` module is a powerful tool for creating APB-compliant interfaces in Chisel-based hardware designs. It simplifies the process of defining and managing APB signals, making it easier to integrate with other components in a SoC design. Whether you're designing a simple peripheral or a complex memory-mapped I/O system, the `ApbBundle` module provides a solid foundation for APB communication.