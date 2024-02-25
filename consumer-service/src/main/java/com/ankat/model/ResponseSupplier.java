package com.ankat.model;

import lombok.RequiredArgsConstructor;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class ResponseSupplier {
    private final CompletableFuture<String> completableFuture;

}
