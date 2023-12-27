package timedelayqueue;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

/**
 * A message with same characteristics as PubSubMessage but with the addition of a time-out duration.
 * It is removed from databases once current time is equal or greater than the time-out
 * duration from when it is first added.
 * The content of the message is in JSON format to accommodate a variety of message types (e.g., TweetData)
 * <p>
 * Rep Invariant:
 *      inherits all rep invariants of PubSubMessage
 *      lifetime > 0
 *      isTransient = true
 * AF(TransientPubSubMessage) = a time-stamped message with parameters on sender, recipient,
 *                              message type, content, and its transiency.
 */
public class TransientPubSubMessage extends PubSubMessage {
    private final int     lifetime;
    private final boolean isTransient = true;

    /**
     * Constructor for TransientPubSubMessage with explicit parameter for time-out duration
     * and other implicit args
     *
     * @param sender id of the message sender, is not null
     * @param receiver id of the message receiver, is not null
     * @param content content of the message, is not null
     * @param lifetime time-out duration. The time the message is to stay in a database
     *                 lifetime > 0
     *
     */
    public TransientPubSubMessage(UUID sender, UUID receiver, String content, int lifetime) {
        super(sender, receiver, content);
        this.lifetime = lifetime;
    }

    /**
     * Constructor for TransientPubSubMessage with explicit parameters
     * for time-out duration, timestamp, type, message id
     *
     * @param id id of the message is not null
     * @param timestamp timestamp when the message is created,
     *                  is not null
     * @param sender id of the message sender, is not null
     * @param receiver id of the message receiver, is not null
     * @param content message content in JSON format, is not null
     * @param type message type, can be Twitter message or normal message
     * @param lifetime time-out duration. The time the message is to stay in a database
     *                 lifetime > 0
     */
    public TransientPubSubMessage(UUID id, Timestamp timestamp, UUID sender, UUID receiver, String content,
                                  MessageType type, int lifetime) {
        super(id, timestamp, sender, receiver, content, type);
        this.lifetime = lifetime;
    }


    /**
     * Constructor for TransientPubSubMessage with explicit parameters
     *
     * @param id id of the message is not null
     * @param timestamp timestamp when the message is created,
     *                  is not null
     * @param sender id of the message sender, is not null
     * @param receiver id of the message receiver, is not null
     * @param content message content in JSON format, is not null
     * @param type message type, can be Twitter message or normal message
     * @param lifetime time-out duration. The time the message is to stay in a database
     *                 lifetime > 0
     */
    public TransientPubSubMessage(UUID id, Timestamp timestamp, UUID sender, List<UUID> receiver,
                                  String content, MessageType type, int lifetime) {
        super(id, timestamp, sender, receiver, content, type);
        this.lifetime = lifetime;

    }

    /**
     * Constructor for TransientPubSubMessage with implicit parameters
     * intended for more than one recipient
     *
     * @param sender id of the message sender, is not null
     * @param receiver id of the message receiver, is not null
     * @param content message content in JSON format, is not null
     * @param lifetime time-out duration. The time the message is to stay in a database
     *                 lifetime > 0
     */
    public TransientPubSubMessage(UUID sender, List<UUID> receiver, String content, int lifetime) {
        super(sender, receiver, content);
        this.lifetime = lifetime;
    }

    /**
     * Getter method for lifetime
     *
     * @return lifetime of the message
     */
    public int getLifetime() {
        return lifetime;
    }

    /**
     * Getter method for isTransient
     *
     * @return true
     */
    @Override
    public boolean isTransient() {
        return isTransient;
    }
}
