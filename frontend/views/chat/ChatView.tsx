import React, {useEffect, useState} from "react";
import {MessageList, MessageListItem} from "@hilla/react-components/MessageList";
import {AiService} from 'Frontend/generated/endpoints.js';
import {MessageInput} from "@hilla/react-components/MessageInput";
import {Button} from "@hilla/react-components/Button";


/**
 * Sets and retrieves a value from session storage.
 *
 * @param {string} key - The key used to identify the stored value.
 * @param {any} initialValue - The initial value if no value is found in session storage.
 * @returns {[any, React.Dispatch<React.SetStateAction<any>>]} - The stored value and a setter function to update the stored value.
 */
function useSessionStorage<String>(key: string, initialValue: String): [String, React.Dispatch<React.SetStateAction<String>>] {
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

/**
 * ChatView component represents a chat interface where users can send and receive messages.
 */
export default function ChatView() {
    const [messages, setMessages]
        = useSessionStorage<MessageListItem[]>('messages', []);

    /**
     * Add a message to the existing list of messages.
     *
     * @param {MessageListItem} message - The message item to be added.
     * @returns {void}
     */
    const addMessage = (message: MessageListItem): void => {
        setMessages(messages => [...messages, message]);
    }

    /**
     * Adds a user message to the chat.
     *
     * @param {string} text - The text of the user message.
     */
    const addUserMessage = (text: string) => {
        addMessage({
            text,
            userName: 'You',
            time: new Date().toLocaleTimeString(),
            userImg: "images/profile-user.png",
            theme: 'current-user',
        });
    }

    /**
     * Adds a message from the bot to the message list.
     *
     * @param {string} response - The response from the bot.
     * @returns {void}
     */
    const addBotMessage = (response: string): void => {
        addMessage({
            text: response,
            userName: 'Assistant',
            userImg: "images/bot.png",
            time: new Date().toLocaleTimeString(),
        });
    }

    /**
     * Sends a message to the chat and gets a response from the AI service.
     *
     * @param {string} message - The message to be sent to the chat.
     * @returns {Promise<void>} - A promise that resolves once the message is sent and the response is received.
     */
    const sendMessage = async (message: string): Promise<void> => {
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