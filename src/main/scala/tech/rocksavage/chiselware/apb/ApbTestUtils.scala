// (c) 2024 Rocksavage Technology, Inc.
// This code is licensed under the Apache Software License 2.0 (see LICENSE.MD)

// File: ApbTestUtils.scala
package tech.rocksavage.chiselware.apb

import chisel3._
import chiseltest._

object ApbTestUtils {
    def writeAPB(apb: ApbBundle, addr: UInt, data: UInt)(implicit
        clock: Clock
    ): Unit = {

        // Set up for writing to the specified APB address
        apb.PSEL.poke(1.U)    // Select APB slave
        clock.step(1)         // Simulate one clock cycle
        apb.PENABLE.poke(1.U) // Enable APB transaction
        apb.PWRITE.poke(1.U)  // Set to write mode
        apb.PADDR.poke(addr)  // Provide the target address
        apb.PWDATA.poke(data) // Provide the data to write

        clock.step(1) // Simulate second clock cycle for write setup

        apb.PSEL.poke(0.U)    // Deselect APB slave
        apb.PENABLE.poke(0.U) // Disable APB transaction
        clock.step(2)         // Step ahead for the next APB transaction
    }

    def writeApbNoDelay(apb: ApbBundle, addr: UInt, data: UInt)(implicit
        clock: Clock
    ): Unit = {
        // Set up for writing to the specified APB address
        apb.PSEL.poke(1.U) // Select APB slave
//        clock.step(1)         // Simulate one clock cycle
        apb.PENABLE.poke(1.U) // Enable APB transaction
        apb.PWRITE.poke(1.U)  // Set to write mode
        apb.PADDR.poke(addr)  // Provide the target address
        apb.PWDATA.poke(data) // Provide the data to write

        clock.step(1) // Simulate second clock cycle for write setup

        apb.PSEL.poke(0.U)    // Deselect APB slave
        apb.PENABLE.poke(0.U) // Disable APB transaction
    }

    def readAPB(apb: ApbBundle, addr: UInt)(implicit clock: Clock): BigInt = {
        clock.setTimeout(1000)
        // Set up for reading from the specified APB address
        apb.PSEL.poke(1.U)    // Select APB slave
        clock.step(1)         // Simulate one clock cycle
        apb.PENABLE.poke(1.U) // Enable APB transaction
        apb.PWRITE.poke(0.U)  // Set to read mode
        apb.PADDR.poke(addr)  // Provide the target address

        clock.step(1) // Simulate the second clock cycle to allow the read

        val readValue = apb.PRDATA.peekInt() // Capture the data being read
        clock.step(1) // Step for the read operation

        apb.PSEL.poke(0.U)    // Deselect APB slave
        apb.PENABLE.poke(0.U) // Disable APB transaction
        clock.step(2)         // Step ahead for the next APB transaction

        readValue // Return the read data
    }
}
