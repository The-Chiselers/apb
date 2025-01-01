// (c) 2024 Rocksavage Technology, Inc.
// This code is licensed under the Apache Software License 2.0 (see LICENSE.MD)
// File: MemoryBundle.scala
package tech.rocksavage.chiselware.apb
import chisel3._

class MemoryBundle(p: ApbParams) extends Bundle {
  val addr  = Output(UInt(p.PADDR_WIDTH.W)) // Address
  val wdata = Output(UInt(p.PDATA_WIDTH.W)) // Write data
  val rdata = Input(UInt(p.PDATA_WIDTH.W))  // Read data
  val write = Output(Bool())                // Write enable
  val read  = Output(Bool())                // Read enable
}
