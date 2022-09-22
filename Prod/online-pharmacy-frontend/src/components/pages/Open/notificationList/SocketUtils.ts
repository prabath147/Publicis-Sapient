import { showNotification } from "@mantine/notifications";
import { ERROR_COLOR, MESSAGE_COLOR, WARNING_COLOR } from "./NotificationConstants";

const baseTopicURL = '/topic/messages/';

export function connectToSocket(stompClient, token: string) {
    stompClient.connect({
        "token": token,
    }, () => {
        stompClient.subscribe(baseTopicURL, (data) => {
            const jsonData = JSON.parse(data.body);
            showNotification({
                message:jsonData.message, 
                color: jsonData.severity === "WARNING" ? WARNING_COLOR : jsonData.severity === "MESSAGE" ? MESSAGE_COLOR: ERROR_COLOR
            });
        });
    });

    return stompClient;
}

export function disconnectSocket(stompClient) {
    if (stompClient != null) {
        stompClient.disconnect();
    }
}