package co.agileventure.akka;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class AnalyticsCoordinatorActor extends UntypedActor {

    private HashMap<String, Long> ipMap = new HashMap();
    private long fileLineCount;
    private long processedCount;
    private ActorRef analyticsSender = null;

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof FileAnalysisMessage) {

            List<String> lines = FileUtils.readLines(new File(
                    ((FileAnalysisMessage) message).getFileName()));

            fileLineCount = lines.size();
            processedCount = 0;

            //Guarda una referencia al originador del mensaje de analitica
            analyticsSender = this.getSender();

            for (String line : lines) {
                // Crea un nuevo actor para procesar la linea del archivo log
                Props props = Props.create(LogLineProcessor.class);
                ActorRef lineProcessorActor = this.getContext().actorOf(props);
                // Direcciona al nuevo actor la linea del archivo log para su
                // procesamiento
                lineProcessorActor.tell(new LogLineMessage(line), this.getSelf());
            }

        } else if (message instanceof LineProcessingResult) {

            // Se recibe el mensaje con el resultado de procesamiento de una
            // linea del archivo log
            String ip = ((LineProcessingResult) message).getIpAddress();
            Long count = null;
            if ((count = ipMap.get(ip)) == null) {
                count = 0L;
            }
            ipMap.put(ip, ++count);
            processedCount++;

            if (fileLineCount == processedCount) {
                //Sending done message
                analyticsSender.tell(new String("Done" + ipMap), ActorRef.noSender());
            }

        } else {
            // Ignora mensaje
            this.unhandled(message);
        }

    }

}
