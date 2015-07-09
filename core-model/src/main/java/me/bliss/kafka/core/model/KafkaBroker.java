package me.bliss.kafka.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * kafka broker model,include host,port,version
 *
 * @author lanjue
 * @version $Id: me.bliss.kafka.core.model, v 0.1 7/9/15
 *          Exp $
 */
@JsonIgnoreProperties({"jmx_port","timestamp"})
public class KafkaBroker {

    private String host;

    private int port;

    private int version;

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

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override public String toString() {
        return new StringBuffer()
                .append("host : ").append(host)
                .append(" port : ").append(port)
                .append(" version: ").append(version)
                .toString();
    }
}
