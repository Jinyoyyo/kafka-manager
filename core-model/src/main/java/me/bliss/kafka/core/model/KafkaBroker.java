package me.bliss.kafka.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * kafka broker model,include host,port,version
 *
 * @author lanjue
 * @version $Id: me.bliss.kafka.core.model, v 0.1 7/9/15
 *          Exp $
 */
@JsonIgnoreProperties({ "jmx_port", "timestamp" ,"version"})
public class KafkaBroker {

    private int id;

    private String host;

    private int port;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override public String toString() {
        return new StringBuffer()
                .append(" id: ").append(id)
                .append(" host : ").append(host)
                .append(" port : ").append(port)
                .toString();
    }
}
