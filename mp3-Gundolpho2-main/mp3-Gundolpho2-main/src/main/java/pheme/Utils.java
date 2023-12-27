package pheme;

import timedelayqueue.PubSubMessage;
import java.util.List;
import java.util.UUID;

/**
    Custom Helper class for corresponding between client-server in JSON format
 */
public class Utils {
    public static class RequestInstance {
        int requestId;
        String operation;
        Parameters parameters; // other relevant info needed to perform the operation

        /**
         *
         * @param requestId id of the request, is a positive non-zero number
         * @param operation the operation to be performed; case-sensitive
         *                  and must correspond to a specific operation in PhemeService.java
         *                  is not null
         * @param parameters other relevant information required to perform operation
         *                   can have null fields.
         */
        public RequestInstance(int requestId, String operation, Parameters parameters) {
            this.requestId = requestId;
            this.operation = operation;
            this.parameters = parameters;
        }

        /**
         * other relevant information required to perform the specific operation,
         * can have null fields.
         */
        public static class Parameters {
            UUID userID;
            String userName;
            String password;
            String twitterUserName;
            String pattern;
            PubSubMessage msg;
            UUID msgID;
            List<UUID> userList;
            UUID user;

            /**
             * Custom constructor for addUser operation in PhemeService
             *
             * @param userID id of user, is not null
             * @param userName username, is not null
             * @param hashPassword hashed password (using blowfish cipher)
             */
            public Parameters(UUID userID, String userName, String hashPassword) {
                this.userID = userID;
                this.userName = userName;
                this.password = hashPassword;
            }

            /**
             * Custom constructor for removeUser operation in PhemeService
             *
             * @param userName username, is not null
             * @param hashPassword hashed password (using blowfish cipher)
             */
            public Parameters(String userName, String hashPassword) {
                this.userName = userName;
                this.password = hashPassword;
            }

            /**
             * Custom constructor for addSubscription operation (without pattern)
             * in PhemeService
             *
             * @param userName username, is not null
             * @param hashPassword hashed password (using blowfish cipher)
             * @param twitterUserName name of twitter user to subscribe to, is not null
             */
            public Parameters(String userName, String hashPassword, String twitterUserName) {
                this.userName = userName;
                this.password = hashPassword;
                this.twitterUserName = twitterUserName;
            }

            /**
             * Custom constructor for addSubscription operation (with pattern)
             * in PhemeService
             *
             * @param userName username, is not null
             * @param hashPassword hashed password (using blowfish cipher)
             * @param twitterUserName name of twitter user to subscribe to, is not null
             * @param pattern pattern to subscribe to
             */
            public Parameters(String userName, String hashPassword, String twitterUserName, String pattern) {
                this.userName = userName;
                this.password = hashPassword;
                this.twitterUserName = twitterUserName;
                this.pattern = pattern;
            }

            /**
             * Custom constructor for sendMessage operation in PhemeService
             *
             * @param userName username, is not null
             * @param hashPassword hashed password (using blowfish cipher)
             * @param msg message to be sent to specified user(s), is not null
             */
            public Parameters(String userName, String hashPassword, PubSubMessage msg) {
                this.userName = userName;
                this.password = hashPassword;
                this.msg = msg;
            }

            /**
             * Custom constructor for isDelivered operation in PhemeService
             * checking for a list of recipients
             *
             * @param msgID id of the message to check delivery for
             * @param userList list of supposed message recipients
             */
            public Parameters(UUID msgID, List<UUID> userList) {
                this.msgID = msgID;
                this.userList = userList;
            }

            /**
             * Custom constructor for isDelivered operation in PhemeService
             * only checking for one recipient
             *
             * @param msgID id of the message to check delivery for
             * @param user supposed message recipient
             */
            public Parameters(UUID msgID, UUID user) {
                this.msgID = msgID;
                this.user = user;
            }

            /**
             * Custom constructor for isUser operation in PhemeService
             *
             * @param userName username, is not null
             */
            public Parameters(String userName) {
                this.userName = userName;
            }
        }
    }

    /**
     * Custom class for constructing a reply in JSON format
     * to send back to the client after fulfilling a request,
     * parsed using GSON
     */
    public static class ReplyInstance {
        private int requestId;
        private boolean success;
        private Object response;

        /**
         * Custom constructor for composing a reply to a client in JSON format
         *
         * @param requestId id of the request, is not null
         * @param success true if the specified operation in request is performed
         *                false if not performed because authentication failed
         * @param response the result of the operation, depends on the type of operation
         *                 performed
         */
        public ReplyInstance(int requestId, boolean success, Object response) {
            this.requestId = requestId;
            this.success = success;
            this.response = response;
        }
    }
}
