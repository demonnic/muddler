package muddler

class Echo {
  def echo(msg, level="INFO") {
    println "MUDDLER: [${level.toUpperCase()}]: $msg"
  }
  def error(msg, Exception ex) {
    this.echo(msg, "ERROR")
    println ex.toString()
    System.exit(42)
  }
}
