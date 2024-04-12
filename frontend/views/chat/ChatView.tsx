import {useEffect, useState} from "react";
import {MessageList, MessageListItem} from "@hilla/react-components/MessageList";
import {AiService} from 'Frontend/generated/endpoints.js';
import {MessageInput} from "@hilla/react-components/MessageInput";
import {Button} from "@hilla/react-components/Button";

function useSessionStorage<T>(key: string, initialValue: T): [T, React.Dispatch<React.SetStateAction<T>>] {
    const [value, setValue] = useState(() => {
        let storedValue = sessionStorage.getItem(key);
        try {
            return storedValue ? JSON.parse(storedValue) : initialValue;
        } catch (error) {
            console.error(`Error parsing stored ${key}:`, error);
            return initialValue;
        }
    });

    useEffect(() => {
        sessionStorage.setItem(key, JSON.stringify(value));
    }, [key, value]);

    return [value, setValue];
}

export default function ChatView() {
    const [messages, setMessages] = useSessionStorage<MessageListItem[]>('messages', []);

    const addMessage = (message: MessageListItem) => {
        setMessages(messages => [...messages, message]);
    }

    const addUserMessage = (text: string) => {
        addMessage({
            text,
            userName: 'You',
            time: new Date().toLocaleTimeString(),
            userImg: "images/profile-user.png",
            theme: 'current-user',
        });
    }

    const addBotMessage = (response: string) => {
        addMessage({
            text: response,
            userName: 'Assistant',
            userImg: "images/bot.png",
            time: new Date().toLocaleTimeString(),
        });
    }

    const sendMessage = async (message: string) => {
        addUserMessage(message);
        const response = await AiService.chat(message) as string;
        addBotMessage(response);
    }

    return (
        <div className="p-m flex flex-col h-full box-border">
            <Button onClick={() => setMessages([])} theme="secondary error small" className="self-end m-l"
                    disabled={messages.length <= 0}>Clear Chat</Button>
            <MessageList items={messages} className="flex-grow"/>
            <MessageInput onSubmit={e => sendMessage(e.detail.value)}/>
        </div>
    );
}