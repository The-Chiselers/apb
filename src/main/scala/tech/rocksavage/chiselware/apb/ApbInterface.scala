// (c) 2024 Rocksavage Technology, Inc.
// This code is licensed under the Apache Software License 2.0 (see LICENSE.MD)
// File: ApbInterface.scala
package tech.rocksavage.chiselware.apb
import chisel3._
import chisel3.util._

class ApbInterface(
    p: ApbParams,
    formal: Boolean = false
) extends Module {
  val io = IO(new Bundle {
    val apb = new ApbBundle(p)
    val mem = new MemoryBundle(p)
  })
  // State register
  val state = RegInit(ApbState.IDLE)

//  override protected def implicitClock: Clock = io.apb.PCLK
//  override protected def implicitReset: Reset = io.apb.PRESETn

  // Default outputs
  io.apb.PREADY  := false.B
  io.apb.PSLVERR := false.B
  io.apb.PRDATA  := 0.U

  // Memory interface defaults
  io.mem.addr  := 0.U
  io.mem.wdata := 0.U
  io.mem.write := false.B
  io.mem.read  := false.B

  // State machine
  switch(state) {
    is(ApbState.IDLE) {
      when(io.apb.PSEL && !io.apb.PENABLE) {
        state := ApbState.SETUP
      }
    }
    is(ApbState.SETUP) {
      when(io.apb.PSEL && io.apb.PENABLE) {
        state := ApbState.ACCESS
      }
    }
    is(ApbState.ACCESS) {
      io.apb.PREADY := true.B
      when(io.apb.PWRITE) {
        // Write operation
        io.mem.addr  := io.apb.PADDR
        io.mem.wdata := io.apb.PWDATA
        io.mem.write := true.B
      } otherwise {
        // Read operation
        io.mem.addr   := io.apb.PADDR
        io.mem.read   := true.B
        io.apb.PRDATA := io.mem.rdata
      }
      when(!io.apb.PSEL) {
        state := ApbState.IDLE
      }
    }
  }

  def implication(a: Bool, b: Bool): Bool = {
    !a || b
  }

  if (formal) {
    when(io.apb.PSEL) {
      // Assertions to verify APB protocol compliance
      // 1. PREADY should only be high in the ACCESS state
      assert(
        implication(io.apb.PREADY, state === ApbState.ACCESS),
        "PREADY should only be high in ACCESS state"
      )
      // 2. PSLVERR should only be high in the ACCESS state
      assert(
        implication(io.apb.PSLVERR, state === ApbState.ACCESS),
        "PSLVERR should only be high in ACCESS state"
      )
      // 3. PENABLE should only be high in the SETUP and ACCESS states
      assert(
        implication(
          io.apb.PENABLE,
          state === ApbState.SETUP || state === ApbState.ACCESS
        ),
        "PENABLE should only be high in SETUP and ACCESS states"
      )
      // 4. PSEL should be high in SETUP and ACCESS states
      assert(
        implication(
          io.apb.PSEL,
          state === ApbState.SETUP || state === ApbState.ACCESS
        ),
        "PSEL should be high in SETUP and ACCESS states"
      )
      // 5. PWRITE should remain stable during the transfer
      val pwriteStable = RegNext(io.apb.PWRITE)
      assert(
        implication(state =/= ApbState.IDLE, io.apb.PWRITE === pwriteStable),
        "PWRITE should remain stable during the transfer"
      )
      // 6. PADDR should remain stable during the transfer
      val paddrStable = RegNext(io.apb.PADDR)
      assert(
        implication(state =/= ApbState.IDLE, io.apb.PADDR === paddrStable),
        "PADDR should remain stable during the transfer"
      )
      // 7. PWDATA should remain stable during write transfers
      val pwdataStable = RegNext(io.apb.PWDATA)
      assert(
        implication(
          state =/= ApbState.IDLE && io.apb.PWRITE,
          io.apb.PWDATA === pwdataStable
        ),
        "PWDATA should remain stable during write transfers"
      )
      // 8. PRDATA should only be valid during read transfers when PREADY is high
      assert(
        implication(
          io.apb.PRDATA =/= 0.U,
          state === ApbState.ACCESS && io.apb.PREADY && !io.apb.PWRITE
        ),
        "PRDATA should only be valid during read transfers when PREADY is high"
      )
    }

  }
}
