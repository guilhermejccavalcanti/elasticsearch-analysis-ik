package org.wltea.analyzer.help;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;

public class Sleep {

    public static final ESLogger logger = Loggers.getLogger("ik-analyzer");

    public enum Type {

        MSEC(), SEC(), MIN(), HOUR()
    }

    ;

    ;

    public static void sleep(Type type, int num) {
        try {
            switch(type) {
                case MSEC:
                    Thread.sleep(num);
                    return;
                case SEC:
                    Thread.sleep(num * 1000L);
                    return;
                case MIN:
                    Thread.sleep(num * 60 * 1000L);
                    return;
                case HOUR:
                    Thread.sleep(num * 60 * 60 * 1000L);
                    return;
                default:
                    System.err.println("?????????MSEC,SEC,MIN,HOUR??");
                    return;
            }
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
