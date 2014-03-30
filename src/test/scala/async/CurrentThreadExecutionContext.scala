package async

import scala.concurrent.ExecutionContext

object CurrentThreadExecutionContext extends ExecutionContext {

  def execute(runnable: Runnable): Unit = runnable.run()

  def reportFailure(t: Throwable): Unit = t.printStackTrace()

}
