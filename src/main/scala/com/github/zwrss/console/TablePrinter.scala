package com.github.zwrss.console

object TablePrinter {

  private def line(colSizes: Seq[Int]): String = {
    "+" + colSizes.map("-" * _).mkString("+") + "+"
  }

  private def printRow(row: Seq[String], colSizes: Seq[Int]): String = {
    val inner = row.zip(colSizes).map {
      case (row, size) =>
        row + (" " * (size - row.size))
    }.mkString("|")

    "|" + inner + "|"
  }

  def format(headers: Seq[String], rows: Seq[Seq[String]]): String = {
    rows.foreach { row =>
      assert(headers.size == row.size)
    }

    val colSizes: Seq[Int] = headers.indices.toList.map { i =>
      val toCompare: Seq[String] = rows.map(_ apply i) :+ headers(i)
      toCompare.map(_.size).max
    }

    line(colSizes) + "\n" +
    printRow(headers, colSizes) + "\n" +
    line(colSizes) + "\n" +
    rows.map(printRow(_, colSizes)).mkString("\n") + "\n" +
    line(colSizes) + "\n"

  }

}
