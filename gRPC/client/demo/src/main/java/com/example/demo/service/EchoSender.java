package com.example.demo.service;

import net.devh.boot.grpc.client.inject.GrpcClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.echo.EchoRequest;
import ru.yandex.practicum.grpc.echo.EchoResponse;
import ru.yandex.practicum.grpc.echo.EchoServiceGrpc;


@Service
public class EchoSender {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @GrpcClient("echo") // внедрение с параметром "echo" — это отсылка к блоку конфигурации
    EchoServiceGrpc.EchoServiceBlockingStub echoService;

    @Scheduled(initialDelay = 1000, fixedDelay = 1000)
    public void echo() {
        log.info("echo send");
        // отправка сообщения с помощью готового к использованию метода
        EchoResponse echoResponse = echoService.echo(EchoRequest.newBuilder().build());
        log.info("echo response received");
    }
}