// (c) 2024 Rocksavage Technology, Inc.
// This code is licensed under the Apache Software License 2.0 (see LICENSE.MD)

// File: ApbBundle.scala
package tech.rocksavage.chiselware.apb

import chisel3._

class ApbBundle(p: ApbParams) extends Bundle {
  val PCLK    = Input(Clock())
  val PRESETn = Input(AsyncReset())
  val PSEL    = Input(Bool())                 // Peripheral select
  val PENABLE = Input(Bool())                 // Enable signal
  val PWRITE  = Input(Bool())                 // Read/Write signal
  val PADDR   = Input(UInt(p.PADDR_WIDTH.W))  // Address
  val PWDATA  = Input(UInt(p.PDATA_WIDTH.W))  // Write data
  val PRDATA  = Output(UInt(p.PDATA_WIDTH.W)) // Read data
  val PREADY  = Output(Bool())                // Ready signal
  val PSLVERR = Output(Bool())                // Slave error signal
}
