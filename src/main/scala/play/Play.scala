package play

import chisel3._
import chisel3.util._

class Play extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(32.W))
    val b = Input(UInt(32.W))
    val cin = Input(Bool())

    val res = Output(UInt(33.W))
  })

  val cout = Wire(Bool())
  val sum = Wire(UInt(32.W))

  val adder = InlineVerilog("adder")(
    Seq("a" -> io.a, "b" -> io.b, "cin" -> io.cin),
    Seq("cout" -> cout, "s" -> sum),
    "assign {cout, s} = a + b + {31'b0, cin};"
  )

  io.res := Cat(cout, sum)
}
