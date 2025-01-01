// (c) 2024 Rocksavage Technology, Inc.
// This code is licensed under the Apache Software License 2.0 (see LICENSE.MD)
// File: ApbInterface.scala
package tech.rocksavage.chiselware.apb
import chisel3._
import chisel3.util._

class ApbInterface(p: ApbParams) extends Module {
  val io = IO(new Bundle {
    val apb = new ApbBundle(p)
    val mem = new MemoryBundle(p)
  })

  // State register
  val state = RegInit(ApbState.IDLE)

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
}
