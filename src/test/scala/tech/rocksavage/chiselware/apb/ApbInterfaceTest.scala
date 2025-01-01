package tech.rocksavage.chiselware.apb

import chiseltest.ChiselScalatestTester
import chiseltest.formal.{BoundedCheck, Formal}
import org.scalatest.flatspec.AnyFlatSpec

class ApbInterfaceTest
    extends AnyFlatSpec
    with ChiselScalatestTester
    with Formal {

  "ApbInterface" should "formally verify" in {

    val addrWidth: Int = 32
    val dataWidth: Int = 32

    val p = ApbParams(addrWidth, dataWidth)

    verify(new ApbInterface(p, true), Seq(BoundedCheck(100)))

  }
}
