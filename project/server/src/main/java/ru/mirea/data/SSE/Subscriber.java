package ru.mirea.data.SSE;

import lombok.*;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.FluxSink;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Subscriber {

    private String login;

    private TypesConnect type;

    private String lvlSch;//sch

    private String lvlGr;//gr

    private String lvlMore1;//role

    private String lvlMore2;//more

    private FluxSink<ServerSentEvent> fluxSink;

    public Subscriber(FluxSink<ServerSentEvent> fluxSink) {
        this.fluxSink = fluxSink;
    }

    public Subscriber(String login) {
        this.login = login;
    }
}