package co.agileventure.akka;

import akka.actor.UntypedActor;

public class LogLineProcessor extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof LogLineMessage) {

            System.out.println("Line: " + ((LogLineMessage) message).getData());
            int idx = ((LogLineMessage) message).getData().indexOf('-');
            if (idx != -1) {
                String ipAddress = ((LogLineMessage) message).getData()
                        .substring(0, idx).trim();
                this.getSender().tell(new LineProcessingResult(ipAddress),
                        this.getSelf());
            }
        } else {
            // Ignora mensaje diferente a LogLineMessage
            this.unhandled(message);
        }

    }
}
