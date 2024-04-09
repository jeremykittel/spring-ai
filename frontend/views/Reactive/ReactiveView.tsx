import {useState} from "react";
import {Subscription} from "@hilla/frontend";
import {TextField} from "@hilla/react-components/TextField";
import {Button} from "@hilla/react-components/Button";
import {getAmqpMessageCancellable} from "Frontend/generated/ReactiveEndpoint";
import {Notification} from '@hilla/react-components/Notification.js';

export default function ReactiveView() {
    const [serverMessage, setServerMessage] = useState("");
    const [subscription, setSubscription] = useState<Subscription<string> | undefined>();
    const toggleServerMessages = async () => {
        if (subscription) {
            subscription.cancel();
            setSubscription(undefined);
            Notification.show('Subscription Ended.', {
                position: 'bottom-center',
                theme: 'error'
            });
        } else {
            setSubscription(getAmqpMessageCancellable().onNext((message) => {
                setServerMessage(message);
            }));
            Notification.show('Subscription started.', {
                position: 'bottom-center',
                theme: 'success'
            });
        }
    }

    return (
        <section className="flex p-m gap-m items-end">
            <TextField label="Server Message" value={serverMessage} readonly style={{width: "350px"}}/>
            <Button onClick={toggleServerMessages}>Toggle server messages</Button>
        </section>
    );
}