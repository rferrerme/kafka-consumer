object Util {

  // Provides mechanism to call certain function when the JVM is shutting down
  def addShutdownHook(mainThread: Thread)(func: () => Unit): Unit = {
    Runtime.getRuntime.addShutdownHook(new Thread() {
      override def run() {
        System.out.println("Shutting down...")
        // Call func
        func()
        // Wait until main thread is done
        try {
          mainThread.join()
        }
        catch {
          case e: InterruptedException => e.printStackTrace()
        }
      }
    })
  }

}
