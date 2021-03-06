//package alpaca
//
//import akka.NotUsed
//import akka.actor.ActorSystem
//import akka.stream.ActorMaterializer
//import akka.stream.scaladsl.{Sink, Source, SourceQueueWithComplete}
//import alpaca.client.{PolygonStreamingClient, StreamingClient}
//import alpaca.dto.request.OrderRequest
//import alpaca.dto.streaming.Alpaca.{
//  AlpacaAccountAndTradeUpdates,
//  AlpacaTradeUpdatesSubscribe
//}
//import alpaca.dto.streaming.Polygon.{
//  PolygonQuoteSubscribe,
//  PolygonTradeSubscribe
//}
//import alpaca.dto.streaming.{StreamMessage, StreamingMessage}
//import org.scalatest.{AsyncFunSuite, BeforeAndAfterEach, FunSuite}
//
//import scala.concurrent.{Await, ExecutionContext, Future}
//import scala.concurrent.duration._
//import scala.util.{Failure, Success}
//import com.typesafe.scalalogging.Logger
//import cats._
//import cats.implicits._
//
////Functional tests. Need a paper api key for these to work.
//class AlpacaIntegrationTests extends AsyncFunSuite with BeforeAndAfterEach {
//  override implicit val executionContext = ExecutionContext.Implicits.global
//  implicit val sys = ActorSystem()
//  implicit val mat = ActorMaterializer()
//  val logger = Logger(classOf[AlpacaIntegrationTests])
//  var alpaca: Alpaca = _
//
//  override def beforeEach() {
//    alpaca = Alpaca(Some(true), Some(""), Some(""))
//    super.beforeEach() // To be stackable, must call super.beforeEach
//  }
//
//  test("Get account") {
//
//    val account = alpaca.getAccount.unsafeToFuture()
//    account.onComplete {
//      case Failure(exception) =>
//        logger.error("Could not get account", exception)
//      case Success(value) =>
//        if (value.account_blocked) {
//          println("Account is currently restricted from trading.")
//        }
//        println(s"${value.buying_power} is available as buying power.")
//    }
//
//    account.map(ac => assert(!ac.pattern_day_trader))
//  }
//
////  test("Get trade updates") {
////    val stream = alpaca.getStream().subscribeAlpaca("trade_updates")
////    stream._2.runWith(Sink.foreach(x => {
////      println(new String(x.data))
////      println(new String(x.subject))
////    }))
////    Thread.sleep(5000)
////
////    val order =
////      Await.result(
////        alpaca
////          .placeOrder(OrderRequest("GOOG", "1", "buy", "market", "day"))
////          .unsafeToFuture(),
////        10 seconds)
////
////    Thread.sleep(10000)
////    null
////  }
////
////  test("Stream -> get quote updates.") {
////    val listOfQuotes = List("T.AAPL", "T.GOOG", "T.SNAP")
////    val stream = alpaca.getStream().sub(listOfQuotes)
////    stream.foreach(x => {
////      x._2._2.runWith(Sink.foreach(x => {
////        println(new String(x.data))
////        println(new String(x.subject))
////      }))
////    })
////    Thread.sleep(10000)
////  }
////
////  test("Get Assets") {
////    val activeAssets = alpaca.getAssets(Some("active")).unsafeToFuture()
////    activeAssets.onComplete {
////      case Failure(exception) =>
////        logger.error("Could not retrieve assets.", exception)
////      case Success(values) =>
////        values
////          .filter(asset => asset.exchange.equalsIgnoreCase("NASDAQ"))
////          .foreach(println)
////    }
////  }
////
//  test("Get Bars") {
//    val bars = alpaca.getBars("day", List("AAPL"), limit = Some("5"))
//    bars.unsafeToFuture().map(bar => assert(bar.head._2.nonEmpty))
//  }
////
//  test("Get Clock") {
//    val clock = alpaca.getClock.unsafeToFuture()
//    clock.map(x => assert(!x.timestamp.isEmpty))
//  }
////
////  test("Get Calendar") {
////    val date = "2018-12-01"
////    val calendar = alpaca.getCalendar(Some(date), Some(date)).unsafeToFuture()
////    calendar.onComplete {
////      case Failure(exception) =>
////        println("Could not get calendar." + exception.getMessage)
////      case Success(value) =>
////        val calendar = value.head
////        println(
////          s"The market opened at ${calendar.open} and closed at ${calendar.close} on ${date}.")
////    }
////  }
////
////  test("Get Position") {
////    val aaplPosition = alpaca.getPosition("AAPL")
////    aaplPosition.unsafeToFuture().onComplete {
////      case Failure(exception) =>
////        println("Could not get position." + exception.getMessage)
////      case Success(value) =>
////        println(s"${value.qty}")
////    }
////  }
////
////  test("Get Positions") {
////    alpaca.getPositions.unsafeToFuture().onComplete {
////      case Failure(exception) =>
////        println("Could not get position." + exception.getMessage)
////      case Success(value) =>
////        println(s"${value.size}")
////    }
////  }
//
//  test("Close Position") {
//    for {
//      posit <- alpaca.closePosition("AAPL").unsafeToFuture()
//    } yield assert(posit.id != null)
//  }
//
//  test("Close All Position") {
//    for {
//      posit <- alpaca.closeAllPositions.unsafeToFuture()
//    } yield assert(true == true)
//  }
////
//  test("Place buy order") {
//    val order =
//      Await.result(
//        alpaca
//          .placeOrder(OrderRequest("MSFT", "10", "buy", "market", "day"))
//          .unsafeToFuture(),
//        10 seconds)
//    assert(order != null)
//
//  }
////
////  test("Place sell order") {
////    val order =
////      Await.result(
////        alpaca
////          .placeOrder(OrderRequest("AAPL", "1", "sell", "market", "day"))
////          .unsafeToFuture(),
////        10 seconds)
////    assert(order != null)
////  }
////
//  test("Test stream polygon") {
//    val stream: PolygonStreamingClient = alpaca.polygonStreamingClient
//
//    val str: (SourceQueueWithComplete[StreamMessage],
//              Source[StreamMessage, NotUsed]) = stream
//      .subscribe(PolygonQuoteSubscribe("AAPL"))
//
//    str._2
//      .runWith(Sink.foreach(x => println(x)))
//    Thread.sleep(15000)
//    str._1.complete()
//    println("-----------------------------------------")
//    str._2
//      .runWith(Sink.foreach(x => println(x)))
//    Thread.sleep(5000)
//    assert(true)
//  }
////
//  test("Test stream alpaca") {
//    val stream = alpaca.alpacaStreamingClient
//    implicit val sys = ActorSystem()
//    implicit val mat = ActorMaterializer()
//    val str = stream
//      .subscribe(AlpacaAccountAndTradeUpdates())
//    str._2
//      .runWith(Sink.foreach(x => println(x)))
//
//    Await.result(
//      alpaca
//        .placeOrder(OrderRequest("AAPL", "10", "buy", "market", "day"))
//        .unsafeToFuture(),
//      10 seconds)
////    Await.result(
////      alpaca
////        .placeOrder(OrderRequest("GOOG", "10", "buy", "market", "day"))
////        .unsafeToFuture(),
////      10 seconds)
//    Thread.sleep(15000)
//    assert(true)
//
//  }
////
////  test("Polygon trade test") {
////    alpaca.getHistoricalTrades("AAPL", "2018-2-2", None, Some(5))
////  }
////
////  test("Polygon hist trade test") {
////    val ht =
////      alpaca.getHistoricalTradesAggregate("AAPL",
////                                          "minute",
////                                          Some("4-1-2018"),
////                                          Some("4-12-2018"),
////                                          Some(5))
////    ht.unsafeToFuture().onComplete {
////      case Failure(exception) =>
////        println("Could not get position." + exception.getMessage)
////      case Success(value) =>
////        println(s"${value}")
////    }
////  }
//
//}
