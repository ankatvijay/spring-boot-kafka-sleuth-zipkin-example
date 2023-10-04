package com.ankat.suplier;

import com.ankat.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.kstream.TransformerSupplier;
import org.apache.kafka.streams.processor.ProcessorContext;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Slf4j
public class MessageTransformerSupplier implements TransformerSupplier<String, String, KeyValue<String, String>> {

    @Override
    public Transformer<String, String, KeyValue<String, String>> get() {
        return new MessageTransformer();
    }

    private static class MessageTransformer implements Transformer<String, String, KeyValue<String, String>> {
        private ProcessorContext context;

        @Override
        public void init(ProcessorContext context) {
            this.context = context;
        }

        @Override
        public KeyValue<String, String> transform(String key, String value) {
            Headers headers = context.headers();
            Optional<Header> internalHeader = StreamSupport.stream(headers.spliterator(), false).filter(header -> "trace_id".equals(header.key())).findFirst();
            if (internalHeader.isPresent()) {
                headers.add("trace_id", internalHeader.get().value());
                System.out.printf("%s  INFO [streams-service,%s,%s] --- %s : Message with key: %s and value: %s on topic: %s in partition: %d\n", LocalDateTime.now().format(Constant.DATE_TIME_FORMATTER), new String(internalHeader.get().value()),new String(internalHeader.get().value()), this.getClass().getName(), key, value, context.topic(), context.partition());
            }
            return KeyValue.pair(key, value);
        }

        @Override
        public void close() {
        }
    }
}
