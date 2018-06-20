package samples

/**
  * Created by nancr on 2016/3/31.
  */
object ResultTest {
  def main(args: Array[String]) {
    val txt = "2014-04-30 13:46:04\t2014-04-30 13:55:10\t45\t\t07\t0\t8.3\t0\t11\t1.0\t1500.0\t0.0\t0.0\t0\t\t\"0\"\t0\t\"0               \"    01\t4406026\t4406024\t44Guangdong\t广东"
    val len = txt.split("\t").length
    println("长度=" + len)
  }
}
