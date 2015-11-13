package co.agileventure.akka;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.OnSuccess;
import akka.pattern.Patterns;
import akka.util.Timeout;

public class ConcurrentAkkaApp {

    public static void main(String[] args) {
        System.out.println("Creating actor system");
        ActorSystem system = ActorSystem.create("system");

        Props props = Props.create(AnalyticsCoordinatorActor.class);
        ActorRef coordinator = system.actorOf(props);

        System.out.println("Sending analytics message");
        FileAnalysisMessage msg = new FileAnalysisMessage("data/access_log.txt");

        final ExecutionContext ec = system.dispatcher();

        Timeout timeout = new Timeout(5, TimeUnit.SECONDS);
        Future<Object> future = Patterns.ask(coordinator, msg, timeout);

        future.onSuccess(new OnSuccess<Object>() {
            @Override
            public void onSuccess(Object result) throws Throwable {
                // TODO Auto-generated method stub
                System.out.println("Result received:" + result);
            }
        }, ec);

    }

}
