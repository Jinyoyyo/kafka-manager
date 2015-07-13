package me.bliss.kafka.core.model;

/**
 *  kafka offset model
 *
 * @author lanjue
 * @version $Id: me.bliss.kafka.core.model, v 0.1 7/13/15
 *          Exp $
 */
public class KafkaPartitionOffset {

    private int id;

    private long earliest;

    private long latest;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getEarliest() {
        return earliest;
    }

    public void setEarliest(long earliest) {
        this.earliest = earliest;
    }

    public long getLatest() {
        return latest;
    }

    public void setLatest(long latest) {
        this.latest = latest;
    }

    @Override
    public String toString() {
        return new StringBuffer("id: ").append(id)
                .append(" earliest ").append(earliest)
                .append(" latest ").append(latest)
                .toString();
    }
}
