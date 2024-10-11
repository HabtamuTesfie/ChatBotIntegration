package play.libs.concurrent;

import org.apache.pekko.actor.ActorSystem;
import scala.concurrent.ExecutionContext;
import scala.concurrent.ExecutionContextExecutor;

//--------------------------------------------------------------------------------
/**
 * Provides a custom execution context from a Pekko dispatcher.
 */
//--------------------------------------------------------------------------------
public abstract class CustomExecutionContext implements ExecutionContextExecutor {
    private final ExecutionContext executionContext;

    //--------------------------------------------------------------------------------
    /**
     * Creates a custom execution context.
     *
     * @param actorSystem the Pekko actor system.
     * @param name the dispatcher name.
     */
    //--------------------------------------------------------------------------------
    public CustomExecutionContext(ActorSystem actorSystem, String name) {
        this.executionContext = actorSystem.dispatchers().lookup(name);
    }

    //--------------------------------------------------------------------------------
    /**
     * Prepares the execution context.
     *
     * @return the prepared execution context.
     */
    //--------------------------------------------------------------------------------
    @Override
    public ExecutionContext prepare() {
        return executionContext.prepare();
    }

    //--------------------------------------------------------------------------------
    /**
     * Executes a command in the execution context.
     *
     * @param command the command to execute.
     */
    //--------------------------------------------------------------------------------
    @Override
    public void execute(Runnable command) {
        executionContext.execute(command);
    }

    //--------------------------------------------------------------------------------
    /**
     * Reports a failure in the execution context.
     *
     * @param cause the cause of the failure.
     */
    //--------------------------------------------------------------------------------
    @Override
    public void reportFailure(Throwable cause) {
        executionContext.reportFailure(cause);
    }
}
