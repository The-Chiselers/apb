// (c) 2024 Rocksavage Technology, Inc.
// This code is licensed under the Apache Software License 2.0 (see LICENSE.MD)

// File: ApbParams.scala
package tech.rocksavage.chiselware.apb

/** Default parameter settings for ApbBundle
  *
  * @constructor
  *   default parameter settings
  * @param PDATA_WIDTH
  *   specifies the data width of the APB bus
  * @param PADDR_WIDTH
  *   specifies the address width of the APB bus
  * @author
  *   Warren Savage
  * @version 1.0
  *
  * @see
  *   [[http://www.rocksavage.tech]] for more information
  */
case class ApbParams(
    PDATA_WIDTH: Int = 32,
    PADDR_WIDTH: Int = 32
) {

  require(PDATA_WIDTH >= 1, "PDATA_WIDTH must be at least 1 bit")
  require(PADDR_WIDTH >= 1, "PADDR_WIDTH must be at least 1 bit")
}
