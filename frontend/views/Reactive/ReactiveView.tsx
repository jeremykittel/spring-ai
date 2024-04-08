import {useState} from "react";
import {Subscription} from "@hilla/frontend";
import {TextField} from "@hilla/react-components/TextField";
import {Button} from "@hilla/react-components/Button";
import {getClockCancellable} from "Frontend/generated/ReactiveEndpoint";
import { Notification } from '@hilla/react-components/Notification.js';

export default function ReactiveView() {
    const [serverTime, setServerTime] = useState("");
    const [subscription, setSubscription] = useState<Subscription<string> | undefined>();
    const toggleServerClock = async () => {
        if (subscription) {
            subscription.cancel();
            setSubscription(undefined);
            Notification.show(`Ended:  ${serverTime}`, {
                position: 'bottom-center',
            });
        } else {
            setSubscription(getClockCancellable().onNext((time) => {
                setServerTime(time);
            }));
            Notification.show(`Started:  ${serverTime}`, {
                position: 'bottom-center',
            });
        }
    }

    return (
        <section className="flex p-m gap-m items-end">
            <TextField label="Server time" value={serverTime} readonly style={{ width: "350px" }}/>
            <Button onClick={toggleServerClock}>Toggle server clock</Button>
        </section>
    );
}