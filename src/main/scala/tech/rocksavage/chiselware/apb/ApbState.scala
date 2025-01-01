// (c) 2024 Rocksavage Technology, Inc.
// This code is licensed under the Apache Software License 2.0 (see LICENSE.MD)
// File: ApbState.scala
package tech.rocksavage.chiselware.apb
import chisel3._

object ApbState extends ChiselEnum {
  val IDLE, SETUP, ACCESS = Value
}
