import {useEffect, useState} from "react";
import {MessageList, MessageListItem} from "@hilla/react-components/MessageList";
import {AiService} from 'Frontend/generated/endpoints.js';
import {MessageInput} from "@hilla/react-components/MessageInput";

export default function ChatView() {

    const [messages, setMessages] = useState<MessageListItem[]>([]);

    const getStoredMessages = () => {
        let storedMessages = sessionStorage.getItem('messages');
        try {
            return storedMessages ? JSON.parse(storedMessages) : [];
        } catch (error) {
            console.error("Error parsing stored messages:", error);
            return [];
        }
    }

    const storeMessages = (messages: MessageListItem[]) => {
        sessionStorage.setItem('messages', JSON.stringify(messages));
    }

    useEffect(() => {
        setMessages(getStoredMessages());
    }, []);

    useEffect(() => {
        storeMessages(messages)
    }, [messages]);

    async function sendMessage(message: string) {
        setMessages(messages => [...messages, {
            text: message,
            userName: 'You',
            time: new Date().toLocaleTimeString(),
            userImg: "images/profile-user.png",
            theme: 'current-user',
        }]);

        const response = await AiService.chat(message);
        setMessages(messages => [...messages, {
            text: response,
            userName: 'Assistant',
            userImg: "images/bot.png",
            time: new Date().toLocaleTimeString()
        }]);
    }

    return (
        <div className="p-m flex flex-col h-full box-border">
            <MessageList items={messages} className="flex-grow"/>
            <MessageInput onSubmit={e => sendMessage(e.detail.value)}/>
        </div>
    );
}