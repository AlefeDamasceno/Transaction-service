package com.example.Transaction_Service.producer;

import com.example.Transaction_Service.Enum.StatusTransacao;
import com.example.Transaction_Service.dto.TransacaoResponse;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class TransactionProducer {

    private final StreamBridge streamBridge;

    public TransactionProducer(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void sendTransaction(TransacaoResponse response){
        if (response.status().equals(StatusTransacao.APROVADA)){

            streamBridge.send("approved-out", response);

        } else {
            streamBridge.send("rejected-out", response);

        }
    }
}
