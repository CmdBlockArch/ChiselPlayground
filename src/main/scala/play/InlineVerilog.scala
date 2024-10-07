package play

import chisel3._
import chisel3.util._
import chisel3.reflect.DataMirror

import scala.collection.immutable.SeqMap

object InlineVerilog {
  private def genVerilog(moduleName: String, input: Seq[(String, Element)], output: Seq[(String, Element)], code: String): String = {
    val inputPorts = input.map{ case (name, comp) => s"input [${comp.getWidth - 1}:0] $name" }.mkString(",\n")
    val outputPorts = output.map{ case (name, comp) => s"output [${comp.getWidth - 1}:0] $name" }.mkString(",\n")
    s"""
     |module $moduleName (
     |$inputPorts,
     |$outputPorts
     |);
     |$code
     |endmodule
    """.stripMargin
  }

  def apply(moduleName: String)(input: Seq[(String, Element)], output: Seq[(String, Element)], code: String): BlackBox = {
    val inputPorts = input.map{ case (name, comp) => (name, Input(UInt(comp.getWidth.W))) }
    val outputPorts = output.map{ case (name, comp) => (name, Output(UInt(comp.getWidth.W))) }
    val verilogModule = Module(new BlackBox() with HasBlackBoxInline {
      override def desiredName = moduleName
      val io = IO(new Record {
        val elements = SeqMap((inputPorts ++ outputPorts).map{
          case (name, comp) => name -> DataMirror.internal.chiselTypeClone(comp)
        }: _*)
      })
      setInline(s"$moduleName.sv", genVerilog(moduleName, input, output, code))
    })
    input.foreach{ case (name, comp) => verilogModule.io.elements(name) := comp }
    output.foreach{ case (name, comp) => comp := verilogModule.io.elements(name) }
    verilogModule
  }
}
